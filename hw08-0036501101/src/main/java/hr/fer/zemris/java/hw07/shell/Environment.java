package hr.fer.zemris.java.hw07.shell;

import java.nio.file.Path;
import java.util.SortedMap;

/**
 * Represents a shell environment.
 * Gives an interface for basic a communication with the shell.
 * 
 * @author Disho
 *
 */
public interface Environment {
	/**
	 * Prompts shell to read line.
	 * 
	 * @return line as string
	 * @throws ShellIOException
	 *             if can't read line
	 */
	String readLine() throws ShellIOException;

	/**
	 * Writes given text to the shell and doesn't move print pointer to the next
	 * line.
	 * 
	 * @param text
	 *            text to write
	 * @throws ShellIOException
	 *             if can't write to shell
	 */
	void write(String text) throws ShellIOException;

	/**
	 * Writes given text to the shell and moves print pointer to the next line.
	 * 
	 * @param text
	 *            line to write
	 * @throws ShellIOException
	 *             if can't write to shell
	 */
	void writeln(String text) throws ShellIOException;

	/**
	 * Returns a map of shell commands in pairs (command name, shell command).
	 * 
	 * @return map of shell commands
	 */
	SortedMap<String, ShellCommand> commands();

	/**
	 * Returns a multiline symbol. A symbol that is printed in the beginning of the
	 * multiline prompts.
	 * 
	 * @return multiline symbol
	 */
	Character getMultilineSymbol();

	/**
	 * Sets a multiline symbol. A symbol that is printed in the beginning of the
	 * multiline prompts.
	 * 
	 * @param symbol
	 *            multiline symbol to set
	 */
	void setMultilineSymbol(Character symbol);

	/**
	 * Returns a prompts symbol. A symbol that is printed in the beginning of every
	 * shell prompt.
	 * 
	 * @return prompt symbol
	 */
	Character getPromptSymbol();

	/**
	 * Sets a prompts symbol. A symbol that is printed in the beginning of every
	 * shell prompt.
	 * 
	 * @param symbol
	 *            prompt symbol to set
	 */
	void setPromptSymbol(Character symbol);

	/**
	 * Returns a morelines symbol. A symbol that is expected at the end of every
	 * line if user wishes to continue the command in the next line.
	 * 
	 * @return morelines symbol
	 */
	Character getMorelinesSymbol();

	/**
	 * Sets a morelines symbol. A symbol that is expected at the end of every line
	 * if user wishes to continue the command in the next line.
	 * 
	 * @param symbol
	 */
	void setMorelinesSymbol(Character symbol);
	
	/**
	 * Returns a path to a currently positioned directory in shell.
	 * 
	 * @return a path
	 */
	Path getCurrentDirectory();
	
	/**
	 * Changes currently positioned directory in shell to the given path if it exists.
	 * 
	 * @param path path to set
	 */
	void setCurrentDirectory(Path path);
	
	/**
	 * Returns shell's shared data of the given key.
	 * 
	 * @param key shared data of the given key or null if it doesn't exist
	 * @return shared data
	 */
	Object getSharedData(String key);
	
	/**
	 * Sets shell's shared data for the given key.
	 * 
	 * @param key key of the shared data
	 * @param value shared data, can't be null
	 */
	void setSharedData(String key, Object value);
}
