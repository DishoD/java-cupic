package hr.fer.zemris.java.hw07.shell.commands;

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
 * Changes currently positioned directory of the shell.
 * 
 * @author Disho
 *
 */
public class CdShellCommand implements ShellCommand {
	/**
	 * command name
	 */
	private static final String COMMAND_NAME = "symbol";
	/**
	 * command description
	 */
	private static final List<String> COMMAND_DESCRIPTION;
	
	static {
		List<String> l = new ArrayList<>();
		l.add("Changes currently positioned directory of the shell.");
		l.add("arg1 - path to be current directory. It can be relative or absolute.");
		
		COMMAND_DESCRIPTION = Collections.unmodifiableList(l);
	}
	
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		List<String> args = Util.parseArguments(arguments);
		
		if(args.size() != 1) {
			env.writeln("Command expects one argument.");
			return ShellStatus.CONTINUE;
		}
		
		Path path = Paths.get(args.get(0));
		path = env.getCurrentDirectory().resolve(path);
		
		if(Files.isDirectory(path)) {
			env.setCurrentDirectory(path);
		} else {
			env.writeln("Invalid given path.");
		}
		
		
		return ShellStatus.CONTINUE;
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
