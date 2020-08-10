package hr.fer.zemris.java.hw07.shell.commands;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.ShellCommand;
import hr.fer.zemris.java.hw07.shell.ShellStatus;

/**
 * Pops the directory from the top of the stack and sets it as a current
 * directory.
 * 
 * @author Disho
 *
 */
public class PopdShellCommand implements ShellCommand {
	/**
	 * command name
	 */
	private static final String COMMAND_NAME = "popd";
	/**
	 * command description
	 */
	private static final List<String> COMMAND_DESCRIPTION;

	private static final String SHARED_DATA_KEY = "cdstack";

	static {
		List<String> l = new ArrayList<>();
		l.add("Pops the directory from the top of the stack and sets it as a current directory.");

		COMMAND_DESCRIPTION = Collections.unmodifiableList(l);
	}

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		if(!arguments.isEmpty()) {
			env.writeln("Command doesn't take any arguments.");
			return ShellStatus.CONTINUE;
		}
		
		@SuppressWarnings("unchecked")
		Stack<Path> stack = (Stack<Path>)env.getSharedData(SHARED_DATA_KEY);
		
		if(stack == null || stack.isEmpty()) {
			env.writeln("The stack is empty. Can't change directory.");
			return ShellStatus.CONTINUE;
		}
		
		env.setCurrentDirectory(stack.pop());
		
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
