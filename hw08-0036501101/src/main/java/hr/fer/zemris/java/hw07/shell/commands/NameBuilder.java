package hr.fer.zemris.java.hw07.shell.commands;

/**
 * Interface for building filenames for the massrename shell command.
 * 
 * @author Disho
 *
 */
public interface NameBuilder {
	/**
	 * Appends appropriate string to the StringBuilder from the given info.
	 * 
	 * @param info information for building a filename
	 */
	void execute(NameBuilderInfo info);
}
