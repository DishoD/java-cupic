package hr.fer.zemris.java.hw07.shell;

import java.util.List;

/**
 * Represents a shell command.
 * 
 * @author Disho
 *
 */
public interface ShellCommand {
	/**
	 * Executes the command on the given shell environment.
	 * 
	 * @param env
	 *            shell environment
	 * @param arguments
	 *            arguments passed to the command
	 * @return shell status as defined by ShellStatus enum
	 */
	ShellStatus executeCommand(Environment env, String arguments);

	/**
	 * Name of the shell command.
	 * 
	 * @return Name of the shell command
	 */
	String getCommandName();

	/**
	 * Returns a description of the shell command. Appropriate for the HELP command.
	 * The method will return a list of string, every string is meant to be printed
	 * in its own line.
	 * 
	 * @return list of lines describing a shell command
	 */
	List<String> getCommandDescription();
}
