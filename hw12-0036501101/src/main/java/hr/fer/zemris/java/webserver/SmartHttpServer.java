package hr.fer.zemris.java.webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import hr.fer.zemris.java.custom.scripting.exec.SmartScriptEngine;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.webserver.RequestContext.RCCookie;

/**
 * A simple http server that reads from properties file and
 * sets up itself.
 * 
 * @author Disho
 *
 */
public class SmartHttpServer {
	/**
	 * servers ip address loaded from properties file under "server.address"
	 */
	@SuppressWarnings("unused")
	private String address;
	/**
	 * servers domain name loaded from properties file under "server.domainName"
	 */
	private String domainName;
	/**
	 * servers port loaded from properties file under "server.port"
	 */
	private int port;
	/**
	 * number of worker threads loaded from properties file under "server.workerThreads"
	 */
	private int workerThreads;
	/**
	 * session timeout in milliseconds loaded from properties file under "server.timeout"
	 */
	private int sessionTimeout;
	/**
	 * dictionary of pairs (file extension, mime-type)
	 */
	private Map<String, String> mimeTypes;
	/**
	 * server thread, runs the server
	 */
	private ServerThread serverThread;
	/**
	 * thread pool of client workers
	 */
	private ExecutorService threadPool;
	/**
	 * path to a root
	 */
	private Path documentRoot;
	/**
	 * is server running
	 */
	private volatile boolean running = false;
	
	/**
	 * dictionary of pairs (path, web worker)
	 */
	private Map<String, IWebWorker> workersMap = new HashMap<>();
	/**
	 * session killer, periodically removes expired clients from the session map
	 */
	private Thread sessionKiller = new SessionKiller();

	/**
	 * dictinary of pairs (SID, clients session)
	 */
	private Map<String, SessionMapEntry> sessions = new ConcurrentHashMap<>();
	/**
	 * used for generating random SIDs
	 */
	private Random sessionRandom = new Random();

	/**
	 * Represents data for a session of one client.
	 * 
	 * @author Disho
	 *
	 */
	private static class SessionMapEntry {
		/**
		 * clients sid
		 */
		@SuppressWarnings("unused")
		private String sid;
		/**
		 * session host
		 */
		private String host;
		/**
		 * time in milliseconds until this session is valid
		 */
		long validUntil;
		/**
		 * clients session parameters
		 */
		Map<String, String> map = new ConcurrentHashMap<>();

		/**
		 * Initializes the object with given parameters.
		 * 
		 * @param sid clients sid
		 * @param host session host
		 * @param validUntil time in milliseconds until this session is valid
		 */
		public SessionMapEntry(String sid, String host, long validUntil) {
			this.sid = sid;
			this.host = host;
			this.validUntil = validUntil;
		}
	}

	/**
	 * Sets up the server according to the properties in the given properties file.
	 * 
	 * @param configFile properties file
	 * @throws IOException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public SmartHttpServer(Path configFile) throws IOException {
		Properties serverProperties = new Properties();
		serverProperties.load(Files.newInputStream(configFile));

		address = serverProperties.getProperty("server.address");
		domainName = serverProperties.getProperty("server.domainName");
		port = Integer.parseInt(serverProperties.getProperty("server.port"));
		workerThreads = Integer.parseInt(serverProperties.getProperty("server.workerThreads"));
		sessionTimeout = Integer.parseInt(serverProperties.getProperty("session.timeout")) * 1000;
		documentRoot = Paths.get(serverProperties.getProperty("server.documentRoot"));

		Path mimePath = Paths.get(serverProperties.getProperty("server.mimeConfig"));
		Properties mimeProperties = new Properties();
		mimeProperties.load(Files.newInputStream(mimePath));
		mimeTypes = new HashMap(mimeProperties);

		Path workerPath = Paths.get(serverProperties.getProperty("server.workers"));
		Properties workerProperties = new Properties();
		workerProperties.load(Files.newInputStream(workerPath));
		workerProperties.forEach((k, v) -> {
			String path = (String) k;
			String fqcn = (String) v;

			Class<?> referenceToClass;
			try {
				referenceToClass = Class.forName(fqcn);
				IWebWorker worker = (IWebWorker) referenceToClass.getConstructor().newInstance();
				workersMap.put(path, worker);
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}

		});

		serverThread = new ServerThread();
	}

	/**
	 * starts the server
	 */
	protected synchronized void start() {
		if (running)
			return;
		running = true;
		serverThread.start();
		threadPool = Executors.newFixedThreadPool(workerThreads, job -> {
			Thread thread = new Thread(job);
			thread.setDaemon(true);
			return thread;
		});
		sessionKiller.setDaemon(true);
		sessionKiller.start();
	}

	/**
	 * stops the server
	 */
	protected synchronized void stop() {
		if (!running)
			return;
		running = false;
		sessionKiller.interrupt();
		threadPool.shutdown();
	}

	/**
	 * Periodically removes expired clients from the session map every 5 minutes.
	 * 
	 * @author Disho
	 *
	 */
	private class SessionKiller extends Thread {
		@Override
		public void run() {
			while (running) {
				for (Entry<String, SessionMapEntry> e : new HashMap<>(sessions).entrySet()) {
					if (System.currentTimeMillis() > e.getValue().validUntil) {
						sessions.remove(e.getKey());
					}
				}

				try {
					sleep(5 * 60 * 1000);
				} catch (InterruptedException e) {
				}
			}
		}
	}

	/**
	 * Server thread. Runs the server and accepts new clients.
	 * 
	 * @author Disho
	 *
	 */
	protected class ServerThread extends Thread {
		@Override
		public void run() {
			ServerSocket serverSocket = null;
			try {
				serverSocket = new ServerSocket(port);
				serverSocket.setSoTimeout(10000);
			} catch (IOException e) {
				System.out.println("Coudln't opent socket on the given port: " + port);
				running = false;
				return;
			}
			while (running) {
				Socket client;
				try {
					client = serverSocket.accept();
					if (!running)
						break;
					ClientWorker cw = new ClientWorker(client);
					threadPool.submit(cw);
				} catch (IOException e) {
				}
			}
			try {
				serverSocket.close();
			} catch (IOException e) {
			}
		}
	}

	/**
	 * A job that will be submitted in the thread pool that handels the clients request.
	 * 
	 * @author Disho
	 *
	 */
	private class ClientWorker implements Runnable, IDispatcher {
		/**
		 * clients socket
		 */
		private Socket csocket;
		/**
		 * sockets tcp input stream
		 */
		private PushbackInputStream istream;
		/**
		 * sockets tcp output stream
		 */
		private OutputStream ostream;
		/**
		 * http version
		 */
		private String version;
		/**
		 * http request method
		 */
		private String method;
		/**
		 * request host
		 */
		private String host;
		/**
		 * request parameters
		 */
		private Map<String, String> params = new HashMap<>();
		/**
		 * temporary parameters
		 */
		private Map<String, String> tempParams = new HashMap<>();
		/**
		 * clients session parameters
		 */
		private Map<String, String> permPrams = new HashMap<>();
		/**
		 * response cookies
		 */
		private List<RCCookie> outputCookies = new ArrayList<>();
		/**
		 * clients sid
		 */
		private String SID;
		/**
		 * request context
		 */
		private RequestContext context;

		/**
		 * Initializes the job with the given socket.
		 * 
		 * @param csocket clients socket
		 */
		public ClientWorker(Socket csocket) {
			this.csocket = csocket;
		}

		@Override
		public void run() {
			try {
				istream = new PushbackInputStream(csocket.getInputStream());
				ostream = csocket.getOutputStream();
				List<String> request = readRequest();

				if (request.size() < 1) {
					sendError(400, "Invalid request");
					return;
				}

				String firstLine = request.get(0);
				String[] params = firstLine.split("\\s+");
				if (params.length != 3) {
					sendError(400, "Invalid request");
					return;
				}

				method = params[0];
				String requestedPath = params[1];
				version = params[2];

				if (!method.equalsIgnoreCase("get")) {
					sendError(400, "Unsuportted method");
					return;
				}
				if (!version.equalsIgnoreCase("HTTP/1.0") && !version.equalsIgnoreCase("HTTP/1.1")) {
					sendError(400, "Unsuportted HTTP version");
					return;
				}

				for (String line : request) {
					if (line.toLowerCase().startsWith("host:")) {
						int start = line.indexOf(' ');
						int end = line.lastIndexOf(':');
						end = end == -1 ? line.length() : end;
						host = line.substring(start, end).trim();
						break;
					}
				}

				if(host == null) {
					host = SmartHttpServer.this.domainName;
				}

				checkSession(request);

				if (requestedPath.contains("?")) {
					params = requestedPath.split("\\?");
					String relativePath = params[0];
					String paramString = params[1];

					for (String param : paramString.split("&")) {
						String[] temp = param.split("=");
						ClientWorker.this.params.put(temp[0], temp[1]);
					}

					internalDispatchRequest(relativePath, true);
				} else {
					internalDispatchRequest(requestedPath, true);
				}

			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					csocket.close();
				} catch (IOException e) {
				}
			}

		}

		/**
		 * Handles the request for the given relative url path.
		 * 
		 * @param urlPath relative url path
		 * @param directCall if true can't access the /private folder, otherwise it can
		 * @throws Exception
		 */
		public void internalDispatchRequest(String urlPath, boolean directCall) throws Exception {
			Path path = documentRoot.resolve(urlPath.substring(1)).toAbsolutePath();

			if (!path.startsWith(documentRoot.toAbsolutePath())) {
				sendError(403, "Forbidden");
				return;
			}
			
			if (context == null) {
				context = new RequestContext(ostream, ClientWorker.this.params, permPrams, outputCookies, tempParams, this);
			}

			if (directCall) {
				if (urlPath.startsWith("/private/")) {
					sendError(404, "File not found");
					return;
				}
			} else {
				if (urlPath.startsWith("/private/")) {
					String fileName = urlPath.substring(9);
					String fileExtension = fileName.substring(fileName.lastIndexOf('.') + 1);
					if (fileExtension.equals("smscr")) {
						String text = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
						new SmartScriptEngine(new SmartScriptParser(text).getDocumentNode(), context).execute();
						ostream.flush();
						return;
					}

					String mime = mimeTypes.get(fileExtension) == null ? "application/octet-stream"
							: mimeTypes.get(fileExtension);
					context.setMimeType(mime);
					context.setStatusCode(200);

					context.write(Files.readAllBytes(path));
					ostream.flush();
					return;
				}
			}

			if (workersMap.containsKey(urlPath)) {
				workersMap.get(urlPath).processRequest(context);
				ostream.flush();
				return;
			}

			if (urlPath.startsWith("/ext/")) {
				String className = urlPath.substring(5);
				try {
					Class<?> refrenceToClass = Class.forName("hr.fer.zemris.java.webserver.workers." + className);
					IWebWorker worker = (IWebWorker) refrenceToClass.getConstructor().newInstance();
					worker.processRequest(context);
					ostream.flush();
					return;
				} catch (ClassNotFoundException e) {
					sendError(404, "File not found");
					return;
				}
			}

			if (!Files.isRegularFile(path) || !Files.isReadable(path)) {
				sendError(404, "File not found");
				return;
			}

			String fileName = path.getFileName().toString();
			String fileExtension = fileName.substring(fileName.lastIndexOf('.') + 1);
			if (fileExtension.equals("smscr")) {
				String text = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
				new SmartScriptEngine(new SmartScriptParser(text).getDocumentNode(), context).execute();
				ostream.flush();
				return;
			}

			String mime = mimeTypes.get(fileExtension) == null ? "application/octet-stream"
					: mimeTypes.get(fileExtension);
			context.setMimeType(mime);
			context.setStatusCode(200);

			context.write(Files.readAllBytes(path));
			ostream.flush();

		}
		
		@Override
		public void dispatchRequest(String urlPath) throws Exception {
			internalDispatchRequest(urlPath, false);
		}

		/**
		 * Sends an error response with the given status code and error message.
		 * 
		 * @param statusCode response status code
		 * @param statusText response message
		 * @throws IOException
		 */
		private void sendError(int statusCode, String statusText) throws IOException {

			ostream.write(("HTTP/1.1 " + statusCode + " " + statusText + "\r\n" + "Server: simple java server\r\n"
					+ "Content-Type: text/plain;charset=UTF-8\r\n" + "Content-Length: 0\r\n" + "Connection: close\r\n"
					+ "\r\n").getBytes(StandardCharsets.ISO_8859_1));
			ostream.flush();

		}

		/**
		 * Reads request from the sockets input stream and generates a list of lines from the request.
		 * 
		 * @return list of lines from the request
		 * @throws IOException
		 */
		private List<String> readRequest() throws IOException {
			BufferedReader br = new BufferedReader(new InputStreamReader(istream, StandardCharsets.ISO_8859_1));
			List<String> lines = new ArrayList<>();

			while (true) {
				String line = br.readLine();
				if (line == null || line.isEmpty())
					break;
				lines.add(line);
			}

			return lines;
		}

		/**
		 * Checks a client of the given request. If a client is unknown it will
		 * give it a new random sid and remember it and its session parameters.
		 * 
		 * @param request list of lines from the request
		 */
		synchronized private void checkSession(List<String> request) {
			String tempSid = null;

			for (String line : request) {
				if (line.startsWith("Cookie:")) {
					line = line.substring(7).trim();
					for (String cookie : line.split(";")) {
						String[] nv = cookie.split("=");
						if (nv[0].equalsIgnoreCase("sid")) {
							tempSid = nv[1].substring(1, nv[1].length() - 1);
							break;
						}
					}
				}
			}

			if (tempSid == null || !sessions.containsKey(tempSid)) {
				createNewSessionClient();
				return;
			}

			SessionMapEntry sessionEntry = sessions.get(tempSid);
			if (!sessionEntry.host.equals(this.host)) {
				createNewSessionClient();
				return;
			}

			if (System.currentTimeMillis() > sessionEntry.validUntil) {
				sessions.remove(tempSid);
				createNewSessionClient();
				return;
			}

			sessionEntry.validUntil = System.currentTimeMillis() + sessionTimeout;
			this.permPrams = sessionEntry.map;
		}

		private void createNewSessionClient() {
			SID = generateSid();
			long validUntil = System.currentTimeMillis() + sessionTimeout;

			SessionMapEntry sessionEntry = new SessionMapEntry(SID, host, validUntil);
			sessions.put(SID, sessionEntry);
			outputCookies.add(new RCCookie("sid", SID, null, host, "/"));
			this.permPrams = sessionEntry.map;
		}

		/**
		 * Generates a random sid of 20 uppercase letters.
		 * 
		 * @return
		 */
		private String generateSid() {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < 20; ++i) {
				sb.append(Character.valueOf((char) (sessionRandom.nextInt(26) + 65)));
			}
			return sb.toString();
		}
	}

	/**
	 * Main method. Runs the program.
	 * 
	 * @param args command-line arguments
	 */
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Expected one argument: a path to a server properties file.");
			return;
		}

		Path path = Paths.get(args[0]);

		if (!Files.isReadable(path)) {
			System.out.println("Given file " + path.toAbsolutePath() + " is not readable.");
			return;
		}

		SmartHttpServer server;
		try {
			server = new SmartHttpServer(path);
			server.start();
			System.out.println("Server started.");

			Scanner sc = new Scanner(System.in);

			while (true) {
				System.out.println("Enter exit to kill the web server.");
				String input = sc.next();
				if (input.equalsIgnoreCase("exit")) {
					server.stop();
					System.out.println("Server will close in approximately 10 seconds.");
					break;
				}
			}
			
			sc.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
