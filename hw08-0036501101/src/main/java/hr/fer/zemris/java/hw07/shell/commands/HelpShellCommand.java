package hr.fer.zemris.java.hw07.shell.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.ShellCommand;
import hr.fer.zemris.java.hw07.shell.ShellStatus;

/**
 * Lists all available command or prints some command's description.
 * 
 * @author Disho
 *
 */
public class HelpShellCommand implements ShellCommand{
	/**
	 * command name
	 */
	private static final String COMMAND_NAME = "help";
	/**
	 * command description
	 */
	private static final List<String> COMMAND_DESCRIPTION;
	
	static {
		List<String> l = new ArrayList<>();
		l.add("Lists all available command.");
		l.add("arg1 (optional) - command name. Will print commands description");
		
		COMMAND_DESCRIPTION = Collections.unmodifiableList(l);
	}

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		List<String> args = Util.parseArguments(arguments);
		
		if(args.isEmpty()) {
			env.writeln("type 'help COMMAND_NAME' to get a description of a command");
			env.writeln("List of all commands:");
			
			for(String name : env.commands().keySet()) {
				env.writeln(name);
			}
			
			return ShellStatus.CONTINUE;
		}
		
		if(args.size() == 1) {
			String name = args.get(0);
			ShellCommand command = env.commands().get(name);
			
			if(command == null) {
				env.writeln("Uknknown command: '" + name + "'");
			} else {
				for(String s : command.getCommandDescription()) {
					env.writeln(s);
				}
			}
			
			return ShellStatus.CONTINUE;
		}
		
		env.writeln("Too many arguments.");
		
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
