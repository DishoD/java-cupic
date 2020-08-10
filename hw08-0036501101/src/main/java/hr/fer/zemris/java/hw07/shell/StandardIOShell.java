package hr.fer.zemris.java.hw07.shell;

import java.io.InputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.SortedMap;
import java.util.TreeMap;

import hr.fer.zemris.java.hw07.shell.commands.CatShellCommand;
import hr.fer.zemris.java.hw07.shell.commands.CdShellCommand;
import hr.fer.zemris.java.hw07.shell.commands.CharsetsShellCommand;
import hr.fer.zemris.java.hw07.shell.commands.CopyShellCommand;
import hr.fer.zemris.java.hw07.shell.commands.CptreeShellCommand;
import hr.fer.zemris.java.hw07.shell.commands.DropdShellCommand;
import hr.fer.zemris.java.hw07.shell.commands.ExitShellCommand;
import hr.fer.zemris.java.hw07.shell.commands.HelpShellCommand;
import hr.fer.zemris.java.hw07.shell.commands.HexdumpShellCommand;
import hr.fer.zemris.java.hw07.shell.commands.ListdShellCommand;
import hr.fer.zemris.java.hw07.shell.commands.LsShellCommand;
import hr.fer.zemris.java.hw07.shell.commands.MassrenameShellCommand;
import hr.fer.zemris.java.hw07.shell.commands.MkdirShellCommand;
import hr.fer.zemris.java.hw07.shell.commands.PopdShellCommand;
import hr.fer.zemris.java.hw07.shell.commands.PushdShellCommand;
import hr.fer.zemris.java.hw07.shell.commands.PwdShellCommand;
import hr.fer.zemris.java.hw07.shell.commands.RmtreeShellCommand;
import hr.fer.zemris.java.hw07.shell.commands.SymbolShellCommand;
import hr.fer.zemris.java.hw07.shell.commands.TreeShellCommand;

/**
 * A shell environment that reads from standard input and writes to the standard output.
 * 
 * @author Disho
 *
 */
public class StandardIOShell implements Environment {
	/**
	 * input stream of the shell
	 */
	private static final InputStream INPUT_STREAM  = System.in;
	/**
	 * output stream of the shell
	 */
	private static final PrintStream PRINT_STREAM = System.out;
	/**
	 * flag that tells if the shell has been terminated
	 */
	private static boolean isShellTerminated = false;
	/**
	 * used for user input
	 */
	private static Scanner SC = new Scanner(INPUT_STREAM);
	/**
	 * only existing instance of this class
	 */
	private static StandardIOShell INSTANCE = null;
	
	/**
	 * greeting message of this shell
	 */
	private static final String GREETING_MESSGE = "Welcome to MyShell v 1.0";
	/**
	 * prompt symbol
	 */
	private static Character promptSymbol = '>';
	/**
	 * morelines symbol
	 */
	private static Character morelinesSymbol = '\\';
	/**
	 * multiline symbol
	 */
	private static Character multilineSymbol = '|';
	
	/**
	 * map of all shell commands
	 */
	private static final SortedMap<String, ShellCommand> COMMANDS;
	
	/**
	 * shared data of the shell
	 */
	private static Map<String, Object> SHARED_DATA = new HashMap<>();
	
	/**
	 * currently positioned directory in shell
	 */
	private static Path currentPath = Paths.get(".");
	
	//initialization of the COMMANDs
	static {
		SortedMap<String, ShellCommand> m = new TreeMap<>();
		m.put("exit", new ExitShellCommand());
		m.put("symbol", new SymbolShellCommand());
		m.put("help", new HelpShellCommand());
		m.put("charsets", new CharsetsShellCommand());
		m.put("cat", new CatShellCommand());
		m.put("ls", new LsShellCommand());
		m.put("tree", new TreeShellCommand());
		m.put("copy", new CopyShellCommand());
		m.put("mkdir", new MkdirShellCommand());
		m.put("hexdump", new HexdumpShellCommand());
		m.put("pwd", new PwdShellCommand());
		m.put("cd", new CdShellCommand());
		m.put("pushd", new PushdShellCommand());
		m.put("popd", new PopdShellCommand());
		m.put("listd", new ListdShellCommand());
		m.put("dropd", new DropdShellCommand());
		m.put("rmtree", new RmtreeShellCommand());
		m.put("cptree", new CptreeShellCommand());
		m.put("massrename", new MassrenameShellCommand());
		
		COMMANDS = Collections.unmodifiableSortedMap(m);
	}
	
	
	/**
	 * Initializes the shell environment.
	 */
	private StandardIOShell() {}
	
	/**
	 * Returns an instance of StandardIOShell.
	 * 
	 * @return instance of StandardIOShell
	 * @throws ShellTerminatedException if StandardIOShell has been terminated
	 */
	public static StandardIOShell getInstance() {
		if(isShellTerminated)
			throw new ShellTerminatedException();
		
		if(INSTANCE == null) {
			INSTANCE = new StandardIOShell();
		}
		
		return INSTANCE;
	}
	
	/**
	 * Terminates the SandardIOShell. Once it is terminated, you will not be able to use it anymore.
	 */
	public void terminate() {
		if(isShellTerminated)
			throw new ShellTerminatedException();
		
		isShellTerminated = true;
		SC.close();
		SC = null;
		INSTANCE = null;
	}
	
	/**
	 * Prompts a user.
	 * 
	 * @return users prompt
	 */
	public String prompt() {
		write(promptSymbol + " ");
		
		StringBuilder sb =  new StringBuilder();
		String line = SC.nextLine();
		
		while(true) {
			if(line.endsWith(morelinesSymbol + "")) {
				sb.append(line.substring(0, line.length() - 1));
			} else {
				sb.append(line);
				break;
			}
			
			write(multilineSymbol + " ");
			line = SC.nextLine();
		}
		
		return sb.toString().trim();
	}
	
	/**
	 * Prints this shell's environment greeting message.
	 * 
	 * @throws ShellIOException if couldn't write to the shell
	 */
	public void printGreetingMessage() throws ShellIOException {
		writeln(GREETING_MESSGE);
	}

	@Override
	public String readLine() throws ShellIOException {
		if(isShellTerminated)
			throw new ShellTerminatedException();
		
		try {
			return SC.nextLine();
		} catch(Exception e) {
			throw new ShellIOException("Couldn't write to the shell.");
		}
	}

	@Override
	public void write(String text) throws ShellIOException {
		if(isShellTerminated)
			throw new ShellTerminatedException();
		
		try {
			PRINT_STREAM.print(text);
		} catch (Exception ex) {
			throw new ShellIOException("Couldn't write to the shell.");
		}
	}

	@Override
	public void writeln(String text) throws ShellIOException {
		if(isShellTerminated)
			throw new ShellTerminatedException();
		
		try {
			PRINT_STREAM.println(text);
		} catch (Exception ex) {
			throw new ShellIOException("Couldn't write to the shell.");
		}
	}

	@Override
	public SortedMap<String, ShellCommand> commands() {
		if(isShellTerminated)
			throw new ShellTerminatedException();
		
		return COMMANDS;
	}

	@Override
	public Character getMultilineSymbol() {
		if(isShellTerminated)
			throw new ShellTerminatedException();
		
		return multilineSymbol;
	}

	@Override
	public void setMultilineSymbol(Character symbol) {
		if(isShellTerminated)
			throw new ShellTerminatedException();
		
		multilineSymbol = symbol;
	}

	@Override
	public Character getPromptSymbol() {
		if(isShellTerminated)
			throw new ShellTerminatedException();
		
		return promptSymbol;
	}

	@Override
	public void setPromptSymbol(Character symbol) {
		if(isShellTerminated)
			throw new ShellTerminatedException();
		
		promptSymbol = symbol;
	}

	@Override
	public Character getMorelinesSymbol() {
		if(isShellTerminated)
			throw new ShellTerminatedException();
		
		return morelinesSymbol;
	}

	@Override
	public void setMorelinesSymbol(Character symbol) {
		if(isShellTerminated)
			throw new ShellTerminatedException();
		
		morelinesSymbol = symbol;
	}

	@Override
	public Path getCurrentDirectory() {
		if(isShellTerminated)
			throw new ShellTerminatedException();
		
		return currentPath.toAbsolutePath().normalize();
	}

	@Override
	public void setCurrentDirectory(Path path) {
		if(isShellTerminated)
			throw new ShellTerminatedException();
		
		Objects.requireNonNull(path, "path cannot be null");
		if(!Files.exists(path)) {
			throw new IllegalArgumentException("path doesn't exits");
		}
		
		if(!Files.isDirectory(path)) {
			throw new IllegalArgumentException("path is not a directory");
		}
		
		currentPath = path;
	}

	@Override
	public Object getSharedData(String key) {
		if(isShellTerminated)
			throw new ShellTerminatedException();
		
		return SHARED_DATA.get(key);
	}

	@Override
	public void setSharedData(String key, Object value) {
		if(isShellTerminated)
			throw new ShellTerminatedException();
		
		Objects.requireNonNull(key, "key cannot be null");
		Objects.requireNonNull(value, "value cannot be null");
		
		SHARED_DATA.put(key, value);
	}

}
