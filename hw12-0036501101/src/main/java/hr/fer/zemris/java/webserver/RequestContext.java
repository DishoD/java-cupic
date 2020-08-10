package hr.fer.zemris.java.webserver;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a http request context and generates the response.
 * 
 * @author Disho
 *
 */
public class RequestContext {
	/**
	 * output stream to which the response will be written
	 */
	private OutputStream outputStream;
	/**
	 * response charset
	 */
	private Charset charset = StandardCharsets.UTF_8;

	/**
	 * response encoding
	 */
	private String encoding = "UTF-8";
	/**
	 * response status code
	 */
	private int statusCode = 200;
	/**
	 * response message
	 */
	private String statusText = "OK";
	/**
	 * response mime-type
	 */
	private String mimeType = "text/html";

	/**
	 * request parameters
	 */
	private Map<String, String> parameters;
	/**
	 * request temporary parameters
	 */
	private Map<String, String> temporaryParameters = new HashMap<>();
	/**
	 * clients session parameters
	 */
	private Map<String, String> persistentParameters;
	/**
	 * response cookies
	 */
	private List<RCCookie> outputCookies;
	/**
	 * this requests dispatcher
	 */
	private IDispatcher dispatcher;

	private boolean headerGenerated;

	/**
	 * Represents a set-cookie field of the response.
	 * 
	 * @author Disho
	 *
	 */
	public static class RCCookie {
		/**
		 * max-age attribute
		 */
		public final Integer maxAge;
		/**
		 * name attribute
		 */
		public final String name;
		/**
		 * value attribute
		 */
		public final String value;
		/**
		 * domain attribute
		 */
		public final String domain;
		/**
		 * path attribute
		 */
		public final String path;

		/**
		 * Initializes the cookie with the given parameters.
		 * 
		 * @param name name attribute, cannot be null
		 * @param value value attribute, cannot be null
		 * @param maxAge max-age attribute, can be null
		 * @param domain domain attribute, can be null
		 * @param path path attribute, can be null
		 */
		public RCCookie(String name, String value, Integer maxAge, String domain, String path) {
			Objects.requireNonNull(name, "name must not be null");
			Objects.requireNonNull(value, "name must not be null");

			this.maxAge = maxAge;
			this.name = name;
			this.value = value;
			this.domain = domain;
			this.path = path;
		}
	}

	/**
	 * Initializes the object with the given parameters.
	 * 
	 * @param outputStream output stream to which the response will be written
	 * @param parameters request parameters
	 * @param persistentParameters clients session parameters
	 * @param outputCookies response cookies
	 */
	public RequestContext(OutputStream outputStream, Map<String, String> parameters,
			Map<String, String> persistentParameters, List<RCCookie> outputCookies) {
		Objects.requireNonNull(outputStream, "outputStream must not be null");
		this.outputStream = outputStream;
		this.outputStream = outputStream;

		if (parameters == null) {
			this.parameters = Collections.unmodifiableMap(new HashMap<>());
		} else {
			this.parameters = Collections.unmodifiableMap(parameters);
		}

		if (persistentParameters == null) {
			this.persistentParameters = new HashMap<>();
		} else {
			this.persistentParameters = persistentParameters;
		}

		if (outputCookies == null) {
			this.outputCookies = new ArrayList<>();
		} else {
			this.outputCookies = outputCookies;
		}

	}
	
	/**
	 * Initializes the object with the given parameters.
	 * 
	 * @param outputStream output stream to which the response will be written
	 * @param parameters request parameters
	 * @param persistentParameters clients session parameters
	 * @param outputCookies response cookies
	 * @param temporaryParameters request temporary parameters
	 * @param dispatcher client worker dispatcher
	 */
	public RequestContext(OutputStream outputStream, Map<String, String> parameters,
			Map<String, String> persistentParameters, List<RCCookie> outputCookies,
			Map<String, String> temporaryParameters, IDispatcher dispatcher) {
		this(outputStream, parameters, persistentParameters, outputCookies);
		
		Objects.requireNonNull(temporaryParameters, "temporaryParameters can't be null");
		Objects.requireNonNull(dispatcher, "dispatcher cannot be null");
		
		this.dispatcher = dispatcher;
		this.temporaryParameters = temporaryParameters;
	}

	/**
	 * Appends the given bytes as the requests response.
	 * 
	 * @param data bytes to be written
	 * @return this request context
	 * @throws IOException
	 */
	public RequestContext write(byte[] data) throws IOException {
		if (!headerGenerated) {
			generateHeader();
		}

		outputStream.write(data);

		return this;
	}

	/**
	 * Appends the given text as the requests response.
	 * 
	 * @param text text to be written
	 * @return this request context
	 * @throws IOException
	 */
	public RequestContext write(String text) throws IOException {
		return write(text.getBytes(charset));
	}

	/**
	 * Generates the header of this response.
	 * (Called before user write method.)
	 * 
	 * @throws IOException
	 */
	private void generateHeader() throws IOException {
		String nl = "\r\n";
		StringBuilder sb = new StringBuilder();

		sb.append("HTTP/1.1 ").append(statusCode).append(" ").append(statusText).append(nl);
		sb.append("Content-Type: ").append(mimeType);

		if (mimeType.startsWith("text/")) {
			sb.append("; charset=").append(encoding);
		}
		sb.append(nl);

		for (RCCookie cookie : outputCookies) {
			generateCookieLine(cookie, sb, nl);
		}

		sb.append(nl);

		byte[] text = sb.toString().getBytes(StandardCharsets.ISO_8859_1);
		outputStream.write(text);

		headerGenerated = true;
	}

	/**
	 * Generates 'Set-cookie: ...' line of the response.
	 * 
	 * @param cookie cookie which attributes will be used
	 * @param sb
	 * @param nl
	 */
	private void generateCookieLine(RCCookie cookie, StringBuilder sb, String nl) {
		sb.append("Set-Cookie: ");
		sb.append(cookie.name).append("=\"").append(cookie.value).append('"');

		if (cookie.domain != null) {
			sb.append("; Domain=").append(cookie.domain);
		}
		if (cookie.path != null) {
			sb.append("; Path=").append(cookie.path);
		}
		if (cookie.maxAge != null) {
			sb.append("; Max-Age=").append(cookie.maxAge);
		}
		
		sb.append("; HttpOnly");
		sb.append(nl);
	}

	/**
	 * Get this requests parameter.
	 * 
	 * @param name parameters name
	 * @return parameters value if it exists, null otherwise
	 */
	public String getParameter(String name) {
		return parameters.get(name);
	}

	/**
	 * @return List of this request parameter names.
	 */
	public Set<String> getParameterNames() {
		return Collections.unmodifiableSet(parameters.keySet());
	}
	
	/**
	 * Get this client session parameter.
	 * 
	 * @param name parameters name
	 * @return parameters value if it exists, null otherwise
	 */
	public String getPersistentParameter(String name) {
		return persistentParameters.get(name);
	}
	
	/**
	 * @return List of clients session parameter names.
	 */
	public Set<String> getPersistentParameterNames() {
		return Collections.unmodifiableSet(persistentParameters.keySet());
	}

	/**
	 * Sets clients session parameter.
	 * 
	 * @param name parameter name
	 * @param value parameter value
	 */
	public void setPersistentParameter(String name, String value) {
		persistentParameters.put(name, value);
	}

	/**
	 * Removes clients session parameter.
	 * 
	 * @param name parameters name
	 */
	public void removePersistentParameter(String name) {
		persistentParameters.remove(name);
	}
	
	/**
	 * Get this requests temporary parameter.
	 * 
	 * @param name parameters name
	 * @return parameters value if it exists, null otherwise
	 */
	public String getTemporaryParameter(String name) {
		return temporaryParameters.get(name);
	}

	/**
	 * @return list of this requests temporary parameter names
	 */
	public Set<String> getTemporaryParameterNames() {
		return Collections.unmodifiableSet(temporaryParameters.keySet());
	}
	
	/**
	 * Sets this requests temporary parameter.
	 * 
	 * @param name parameter name
	 * @param value parameter value
	 */
	public void setTemporaryParameter(String name, String value) {
		temporaryParameters.put(name, value);
	}
	
	/**
	 * Removes temporary parameter.
	 * 
	 * @param name parameters name
	 */
	public void removeTemporaryParameter(String name) {
		temporaryParameters.remove(name);
	}

	/**
	 * Add an output cookie that will be written in the response header.
	 * 
	 * @param cookie output cookie
	 */
	public void addRCCookie(RCCookie cookie) {
		outputCookies.add(cookie);
	}

	/**
	 * Throws a IllegalStateException if the header has been written.
	 * 
	 * @param field will be inserted in the exception message: 'Header constructed. Cannot change $field$ anymore.'
	 */
	private void headerGeneratedExceptionThrower(String field) {
		if (headerGenerated)
			throw new IllegalStateException("Header constructed. Cannot change " + field + " anymore.");
	}

	/**
	 * Sets the encoding of this requests response.
	 * 
	 * @param encoding
	 *            the encoding to set
	 */
	public void setEncoding(String encoding) {
		headerGeneratedExceptionThrower("encoding");

		if (!Charset.isSupported(encoding))
			throw new IllegalCharsetNameException("This charset is not supported: " + encoding);

		this.encoding = encoding;
		charset = Charset.forName(encoding);
	}

	/**
	 * @param statusCode
	 *            the statusCode to set
	 */
	public void setStatusCode(int statusCode) {
		headerGeneratedExceptionThrower("statusCode");

		this.statusCode = statusCode;
	}

	/**
	 * Sets the status text of this requests response.
	 * @param statusText
	 *            the statusText to set
	 */
	public void setStatusText(String statusText) {
		headerGeneratedExceptionThrower("statusText");

		this.statusText = statusText;
	}

	/**
	 * Sets the mime-type of this requests response.
	 * 
	 * @param mimeType
	 *            the mimeType to set
	 */
	public void setMimeType(String mimeType) {
		headerGeneratedExceptionThrower("mimeType");

		this.mimeType = mimeType;
	}
	
	/**
	 * Get the dispatcher.
	 * 
	 * @return dispatcher
	 */
	public IDispatcher getDispatcher() {
		return dispatcher;
	}
}
