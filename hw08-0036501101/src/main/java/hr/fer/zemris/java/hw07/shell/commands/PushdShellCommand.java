package hr.fer.zemris.java.hw07.shell.commands;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.ShellCommand;
import hr.fer.zemris.java.hw07.shell.ShellStatus;

/**
 * Pushes the current directory on to the stack and changes the current
 * directory to the given one.
 * 
 * @author Disho
 *
 */
public class PushdShellCommand implements ShellCommand {
	/**
	 * command name
	 */
	private static final String COMMAND_NAME = "pushd";
	/**
	 * command description
	 */
	private static final List<String> COMMAND_DESCRIPTION;
	
	private static final String SHARED_DATA_KEY = "cdstack";

	static {
		List<String> l = new ArrayList<>();
		l.add("Pushes the current directory on to the stack and changes the current directory to the given one.");
		l.add("arg1 - path to a directory");

		COMMAND_DESCRIPTION = Collections.unmodifiableList(l);
	}

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		List<String> args = Util.parseArguments(arguments);
		
		if(args.size() != 1) {
			env.writeln("Command expects one argument: a path to the directory.");
			return ShellStatus.CONTINUE;
		}
		
		Path path = env.getCurrentDirectory().resolve(Paths.get(args.get(0)));
		
		if(!Files.isDirectory(path)) {
			env.writeln("Given path is not a directory.");
			return ShellStatus.CONTINUE;
		}
		
		@SuppressWarnings("unchecked")
		Stack<Path> stack = (Stack<Path>)env.getSharedData(SHARED_DATA_KEY);
		
		if(stack == null) {
			stack = new Stack<>();
			stack.push(env.getCurrentDirectory());
			env.setSharedData(SHARED_DATA_KEY, stack);
		} else {
			stack.push(env.getCurrentDirectory());
		}
		
		env.setCurrentDirectory(path);

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
