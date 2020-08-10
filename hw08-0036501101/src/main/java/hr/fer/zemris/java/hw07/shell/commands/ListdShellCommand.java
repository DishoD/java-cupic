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
 * Lists all the directories on the stack.
 * 
 * @author Disho
 *
 */
public class ListdShellCommand implements ShellCommand {
	/**
	 * command name
	 */
	private static final String COMMAND_NAME = "listd";
	/**
	 * command description
	 */
	private static final List<String> COMMAND_DESCRIPTION;

	private static final String SHARED_DATA_KEY = "cdstack";

	static {
		List<String> l = new ArrayList<>();
		l.add("Lists all the directories on the stack.");

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
			env.writeln("The stack is empty. No directories to list.");
			return ShellStatus.CONTINUE;
		}
		
		for(int i = stack.size() - 1; i >= 0; --i) {
			env.writeln(stack.get(i).toString());
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
