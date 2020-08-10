package hr.fer.zemris.java.hw07.shell.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.ShellCommand;
import hr.fer.zemris.java.hw07.shell.ShellStatus;

/**
 * Terminates the shell environment.
 * 
 * @author Disho
 *
 */
public class ExitShellCommand implements ShellCommand {
	private static final String COMMAND_NAME = "exit";
	private static final List<String> COMMAND_DESCRIPTION;
	
	static {
		List<String> l = new ArrayList<>();
		l.add("Terminates the shell environment.");
		
		COMMAND_DESCRIPTION = Collections.unmodifiableList(l);
	}

	/**
	 * Initializes the command.
	 */
	public ExitShellCommand() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		if(arguments.isEmpty()) 
			return ShellStatus.TERMINATE;
		
		
		env.writeln("exit command doesn't take any arguments");
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
