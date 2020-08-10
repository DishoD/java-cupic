package hr.fer.zemris.java.hw07.shell.commands;

import java.io.IOException;
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
 * Creates given director structure.
 * 
 * @author Disho
 *
 */
public class MkdirShellCommand implements ShellCommand{
	/**
	 * command name
	 */
	private static final String COMMAND_NAME = "mkdir";
	/**
	 * command description
	 */
	private static final List<String> COMMAND_DESCRIPTION;
	
	
	static {
		List<String> l = new ArrayList<>();
		l.add("Creates given director structure.");
		l.add("arg1 - single directory or directory structure");
		
		COMMAND_DESCRIPTION = Collections.unmodifiableList(l);
	}

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		List<String> args = Util.parseArguments(arguments);
		
		if(args.size() != 1) {
			env.writeln("Command expects one argument: single directory or directory structure.");
		}
		
		Path path = env.getCurrentDirectory().resolve(Paths.get(args.get(0)));
		
		try {
			Files.createDirectories(path);
		} catch (IOException e) {
			env.writeln("Error ocurred during command execution: " + e.getMessage());
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
