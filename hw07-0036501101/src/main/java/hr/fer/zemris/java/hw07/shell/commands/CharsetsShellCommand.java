package hr.fer.zemris.java.hw07.shell.commands;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.ShellCommand;
import hr.fer.zemris.java.hw07.shell.ShellStatus;

/**
 * Prints all system's avaliable charsets.
 * 
 * @author Disho
 *
 */
public class CharsetsShellCommand implements ShellCommand {
	/**
	 * command name
	 */
	private static final String COMMAND_NAME = "charsets";
	/**
	 * command description
	 */
	private static final List<String> COMMAND_DESCRIPTION;

	static {
		List<String> l = new ArrayList<>();
		l.add("Prints all system's avaliable charsets. Takes no arguemnts");

		COMMAND_DESCRIPTION = Collections.unmodifiableList(l);
	}

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		if (!arguments.isEmpty()) {
			env.writeln("This command doesn't take any arguments.");
			return ShellStatus.CONTINUE;
		}

		env.writeln("All system's avaliable charsets:");
		for (String charset : Charset.availableCharsets().keySet()) {
			env.writeln(charset);
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
