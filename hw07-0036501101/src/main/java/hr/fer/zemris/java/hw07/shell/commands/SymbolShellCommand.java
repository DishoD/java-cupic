package hr.fer.zemris.java.hw07.shell.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.ShellCommand;
import hr.fer.zemris.java.hw07.shell.ShellStatus;

/**
 * Prints or changes PROMPT, MORELINES or MULTILINE symbol of the shell environment.
 * 
 * @author Disho
 *
 */
public class SymbolShellCommand implements ShellCommand {
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
		l.add("Prints or changes PROMPT, MORELINES or MULTILINE symbol.");
		l.add("arg1 - 'PROMPT', 'MORELINES' or 'MULTLINE'.");
		l.add("arg2 (optinal) - to what character will arg1 will be changed to.");
		
		COMMAND_DESCRIPTION = Collections.unmodifiableList(l);
	}

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		List<String> args = Util.parseArguments(arguments);
		
		if(args.size() == 1) {
			switch(args.get(0).toLowerCase()) {
			case "prompt":
				env.writeln("Symbol for prompt is '" + env.getPromptSymbol() + "'");
				break;
			case "morelines":
				env.writeln("Symbol for morelines is '" + env.getMorelinesSymbol() + "'");
				break;
			case "multiline":
				env.writeln("Symbol for multiline is '" + env.getMultilineSymbol() + "'");
				break;
			default:
				env.writeln("Illegal argument: " + args.get(0));
			}
			
			return ShellStatus.CONTINUE;
		}
		
		if(args.size() == 2) {
			if(args.get(1).length() != 1) {
				env.writeln("Arg2 must be a single character.");
				return ShellStatus.CONTINUE;
			}
			char newSymbol = args.get(1).toCharArray()[0];
			
			switch(args.get(0).toLowerCase()) {
			case "prompt":
				env.writeln("Symbol for prompt changed from '" + env.getPromptSymbol() + "'" + " to '" + newSymbol + "'");
				env.setPromptSymbol(newSymbol);
				break;
			case "morelines":
				env.writeln("Symbol for morelines changed from '" + env.getMorelinesSymbol() + "'" + " to '" + newSymbol + "'");
				env.setMorelinesSymbol(newSymbol);
				break;
			case "multiline":
				env.writeln("Symbol for multiline changed from '" + env.getMultilineSymbol() + "'" + " to '" + newSymbol + "'");
				env.setMultilineSymbol(newSymbol);
				break;
			default:
				env.writeln("Illegal argument: " + args.get(0));
			}
			
			return ShellStatus.CONTINUE;
		}
		
		env.writeln("Command excepts 1 or 2 arguemts");
		
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
