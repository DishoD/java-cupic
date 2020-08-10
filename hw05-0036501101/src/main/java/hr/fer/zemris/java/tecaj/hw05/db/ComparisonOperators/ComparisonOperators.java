package hr.fer.zemris.java.tecaj.hw05.db.ComparisonOperators;

import hr.fer.zemris.java.tecaj.hw05.db.IComparisonOperator.IComparisonOperator;

/**
 * Class that contains comparison operator objects that can check an relation
 * between two string literals. Objects of this class cannot be instantiated.
 * Meant only for usage of public static members.
 * 
 * @author Hrvoje Ditrih
 *
 */
public final class ComparisonOperators {

	/**
	 * Private constructor so that objects of this class can't be instantiated.
	 * Meant only for usage of public static members.
	 * 
	 */
	private ComparisonOperators() {

	}

	/**
	 * Checks whether the first string literal is alphabetically less than the
	 * second one.
	 */
	public static IComparisonOperator LESS = (s1, s2) -> s1.compareTo(s2) < 0;

	/**
	 * Checks whether the first string literal is alphabetically less than or equals
	 * the second one.
	 */
	public static IComparisonOperator LESS_OR_EQUALS = (s1, s2) -> s1.compareTo(s2) <= 0;

	/**
	 * Checks whether the first string literal is alphabetically greater than the
	 * second one.
	 */
	public static IComparisonOperator GREATER = (s1, s2) -> s1.compareTo(s2) > 0;

	/**
	 * Checks whether the first string literal is alphabetically greater or equals
	 * than the second one.
	 */
	public static IComparisonOperator GREATER_OR_EQUALS = (s1, s2) -> s1.compareTo(s2) >= 0;

	/**
	 * Checks whether the first string literal is equal the second one.
	 */
	public static IComparisonOperator EQUALS = (s1, s2) -> s1.equals(s2);

	/**
	 * Checks whether the first string literal is not equal the second one.
	 */
	public static IComparisonOperator NOT_EQUALS = (s1, s2) -> !s1.equals(s2);

	/**
	 * Checks whether the first string literal is like the second one. Second string
	 * literal can contain at most one wildcard * which represents a sequence of
	 * zero or more characters which can be ignored in comparison of literals.
	 * Eg. "Something" is LIKE "*thing",
	 * 
	 */
	public static IComparisonOperator LIKE = (s1, s2) -> {
		if(s2.equals("*")) return true;
		
		String[] strings = s2.split("\\*");

		if (s2.startsWith("*")) {
			if (strings.length > 2 || s2.endsWith("*"))
				throw new IllegalArgumentException("value2 must contain at most one wildcard *");

			return s1.endsWith(strings[1]);
		}

		if (s2.endsWith("*")) {
			if (strings.length > 1)
				throw new IllegalArgumentException("value2 must contain at most one wildcard *");

			return s1.startsWith(strings[0]);
		}

		if (strings.length == 1)
			return s1.equals(strings[0]);

		if (strings.length == 2) {
			if (!s1.startsWith(strings[0]))
				return false;
			return s1.substring(strings[0].length()).endsWith(strings[1]);
		}

		throw new IllegalArgumentException("value2 must contain at most one wildcard *");
	};
}
