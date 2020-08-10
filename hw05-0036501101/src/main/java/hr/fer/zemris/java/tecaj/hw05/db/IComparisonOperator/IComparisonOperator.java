package hr.fer.zemris.java.tecaj.hw05.db.IComparisonOperator;

/**
 * Provides an interface that tests a relation of two string literals according
 * to the comparison operator.
 * 
 * @author Hrvoje Ditrih
 *
 */
@FunctionalInterface
public interface IComparisonOperator {
	/**
	 * Tests a relation of two string literals according to the comparison operator.
	 * 
	 * @param value1
	 *            first string literal
	 * @param value2
	 *            second string literal
	 * @return true if relation of two literals is according to the comparison
	 *         operator, false otherwise
	 */
	boolean satisfied(String value1, String value2);
}
