package hr.fer.zemris.java.hw07.shell.commands;

/**
 * Contains StringBuilder and group strings for the NameBulder.
 * 
 * @author Disho
 *
 */
public interface NameBuilderInfo {
	/**
	 * Returns a StringBuilder.
	 * 
	 * @returna StringBuilder
	 */
	StringBuilder getStringBuilder();

	/**
	 * Returns a string associated with the given group.
	 * 
	 * @param index group index, should be >= 0
	 * @returnstring associated with the given group
	 */ 
	String getGroup(int index);
}
