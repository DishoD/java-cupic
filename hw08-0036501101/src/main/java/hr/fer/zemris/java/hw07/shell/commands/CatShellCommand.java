package hr.fer.zemris.java.hw07.shell.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.ShellCommand;
import hr.fer.zemris.java.hw07.shell.ShellStatus;

/**
 * Prints given file to the shell using one of the system's available charsets.
 * 
 * @author Disho
 *
 */
public class CatShellCommand implements ShellCommand{
	/**
	 * command name
	 */
	private static final String COMMAND_NAME = "cat";
	/**
	 * command description
	 */
	private static final List<String> COMMAND_DESCRIPTION;
	
	static {
		List<String> l = new ArrayList<>();
		l.add("Prints given file to the shell.");
		l.add("arg1 - path to a file to print");
		l.add("arg2 (optional) - charset to use for interpreting bytes. If not given, systems default is used.");
		
		COMMAND_DESCRIPTION = Collections.unmodifiableList(l);
	}

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		List<String> args = Util.parseArguments(arguments);
		
		if(args.isEmpty()) {
			env.writeln("cat expects at least one argument: a path to the file to print to the shell");
			return ShellStatus.CONTINUE;
		}
		
		Path path = env.getCurrentDirectory().resolve(Paths.get(args.get(0)));
		
		if(args.size() == 1) {
			writeToShell(env, path, Charset.defaultCharset());
			return ShellStatus.CONTINUE;
		}
		
		if(args.size() == 2) {
			String cs = args.get(1);
			
			if(!Charset.isSupported(cs)) {
				env.writeln("Charset '" + cs +"' is not supported by the system.");
				return ShellStatus.CONTINUE;
			}
			
			writeToShell(env, path, Charset.forName(cs));
			return ShellStatus.CONTINUE;
		}
		
		env.writeln("Too many arguemtns.");
		return ShellStatus.CONTINUE;
	}
	
	/**
	 * Writes given file to the shell environment with the given charset.
	 * 
	 * @param env shell environment to write to
	 * @param path file to print
	 * @param charset charset to use
	 */
	private static void writeToShell(Environment env, Path path, Charset charset) {
		if(!Files.exists(path)) {
			env.writeln("The file '" + path + "' doesn't exist.");
			return;
		}
		
		if(!Files.isRegularFile(path)) {
			env.writeln("Path '" + path + "' is not a file.");
			return;
		}
		
		if(!Files.isReadable(path)) {
			env.writeln("The file '" + path + "' is not readable.");
			return;
		}
		
		try(BufferedReader is = Files.newBufferedReader(path, charset)) {
			is.lines().forEach(env::writeln);
		} catch (IOException e) {
			env.writeln("Couldn't open '" + path + "' file.");
		} catch (UncheckedIOException e) {
			env.writeln("Error occured durning execution. Try cat with a different charset.");
		}
		
		return;
	}

	@Override
	public String getCommandName() {
		return COMMAND_NAME;
	}

	@Override
	public List<String> getCommandDescription() {
		return COMMAND_DESCRIPTION;
	}

}
