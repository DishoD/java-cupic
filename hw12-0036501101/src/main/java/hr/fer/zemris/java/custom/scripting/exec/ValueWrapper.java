package hr.fer.zemris.java.custom.scripting.exec;

import java.util.function.BiFunction;

/**
 * Represents a value wrapper of any object. The stored value can be null.
 * 
 * @author Disho
 *
 */
public class ValueWrapper {
	/**
	 * stored value
	 */
	private Object value;

	/**
	 * values name
	 */
	private final String name;

	/**
	 * Sumation functions
	 */
	private static final BiFunction<Integer, Integer, Integer> INTEGER_ADDER = (a1, a2) -> Integer.valueOf(a1 + a2);
	private static final BiFunction<Double, Double, Double> DOUBLE_ADDER = (a1, a2) -> Double.valueOf(a1 + a2);

	/**
	 * subtraction functions
	 */
	private static final BiFunction<Integer, Integer, Integer> INTEGER_SUBTRACTOR = (a1, a2) -> Integer
			.valueOf(a1 - a2);
	private static final BiFunction<Double, Double, Double> DOUBLE_SUBTRACTOR = (a1, a2) -> Double.valueOf(a1 - a2);

	/**
	 * product functions
	 */
	private static final BiFunction<Integer, Integer, Integer> INTEGER_MULTIPLIER = (a1, a2) -> Integer
			.valueOf(a1 * a2);
	private static final BiFunction<Double, Double, Double> DOUBLE_MULTIPLIER = (a1, a2) -> Double.valueOf(a1 * a2);

	/**
	 * division functions
	 */
	private static final BiFunction<Integer, Integer, Integer> INTEGER_QUOTIENT = (a1, a2) -> Integer.valueOf(a1 / a2);
	private static final BiFunction<Double, Double, Double> DOUBLE_QUOTIENT = (a1, a2) -> Double.valueOf(a1 / a2);

	/**
	 * compare functions
	 */
	private static final BiFunction<Integer, Integer, Integer> INTEGER_COMPARATOR = (a1, a2) -> Integer.compare(a1, a2);
	private static final BiFunction<Double, Double, Double> DOUBLE_COMPARATOR = (a1,
			a2) -> (double) Double.compare(a1, a2);

	/**
	 * Initializes the wrapper with the given value
	 * 
	 * @param value
	 *            object to be stored as value (can be null)
	 * @param name
	 *            value's name, if null it is given no name
	 */
	public ValueWrapper(Object value, String name) {
		this.value = value;
		this.name = name;
	}

	/**
	 * Adds the given object to the current value if both can be interpreted as a
	 * number.
	 * 
	 * @param incValue
	 *            value to be added, can be null (in that case interpreted as 0)
	 * @throws IllegalArgumentException
	 *             if incValue can't be interpreted as a number
	 */
	public void add(Object incValue) {
		Number currentValue = promoteToNumber(value, "current value");
		Number otherValue = promoteToNumber(incValue, "decValue");

		this.value = executeOperation(currentValue, otherValue, INTEGER_ADDER, DOUBLE_ADDER);
	}

	/**
	 * Subtracts the given object from the current value if both can be interpreted
	 * as a number.
	 * 
	 * @param decValue
	 *            value to be subtracted, can be null (in that case interpreted as
	 *            0)
	 * @throws IllegalArgumentException
	 *             if decValue can't be interpreted as a number
	 */
	public void subtract(Object decValue) {
		Number currentValue = promoteToNumber(value, "current value");
		Number otherValue = promoteToNumber(decValue, "decValue");

		this.value = executeOperation(currentValue, otherValue, INTEGER_SUBTRACTOR, DOUBLE_SUBTRACTOR);
	}

	/**
	 * Multiplies the given object with the current value if both can be interpreted
	 * as a number.
	 * 
	 * @param mulValue
	 *            value to be multiplied with, can be null (in that case interpreted
	 *            as 0)
	 * @throws IllegalArgumentException
	 *             if mulValue can't be interpreted as a number
	 */
	public void multiply(Object mulValue) {
		Number currentValue = promoteToNumber(value, "current value");
		Number otherValue = promoteToNumber(mulValue, "mulValue");

		this.value = executeOperation(currentValue, otherValue, INTEGER_MULTIPLIER, DOUBLE_MULTIPLIER);
	}

	/**
	 * Divides the given object with the current value if both can be interpreted as
	 * a number.
	 * 
	 * @param divValue
	 *            value to be divided with
	 * @throws IllegalArgumentException
	 *             if divValue can't be interpreted as a number or is zero
	 */
	public void divide(Object divValue) {
		Number currentValue = promoteToNumber(value, "current value");
		Number otherValue = promoteToNumber(divValue, "divValue");

		if (otherValue.doubleValue() == 0)
			throw new IllegalArgumentException("divValue is zero. Can't divide by zero.");

		this.value = executeOperation(currentValue, otherValue, INTEGER_QUOTIENT, DOUBLE_QUOTIENT);
	}

	/**
	 * Compares the given value (if it can be interpreted as a number) with the
	 * current value.
	 * 
	 * @param withValue
	 *            value to be comared with, can be null (in that case interpreted as
	 *            0)
	 * @return 0 if numbers are equal, negativ value if current value is less than
	 *         given value, positive number otherwise
	 */
	public int numCompare(Object withValue) {
		Number currentValue = promoteToNumber(value, "current value");
		Number otherValue = promoteToNumber(withValue, "withValue");

		return ((Number) executeOperation(currentValue, otherValue, INTEGER_COMPARATOR, DOUBLE_COMPARATOR)).intValue();

	}

	/**
	 * Executes the operation on the given parameters and returns the result. If
	 * both Numbers are Integers, the integerOperation will be used. If at least one
	 * Number is Double, the doubleOperation will be used.
	 * 
	 * @param currentValue
	 *            currently stored object in wrapper promoted to a number
	 * @param otherValue
	 *            other object promoted to the number
	 * @param integerOperation
	 *            operation to execute of both are Integers, and the result is
	 *            Integer
	 * @param doubleOperation
	 *            operation to execute of both are Doubles, and the result is Double
	 * @return result of the operation
	 */
	private Object executeOperation(Number currentValue, Number otherValue,
			BiFunction<Integer, Integer, Integer> integerOperation,
			BiFunction<Double, Double, Double> doubleOperation) {

		if (currentValue instanceof Integer && otherValue instanceof Integer) {
			return integerOperation.apply((Integer) currentValue, (Integer) otherValue);
		} else {
			return doubleOperation.apply(currentValue.doubleValue(), otherValue.doubleValue());
		}
	}

	/**
	 * Returns value's name.
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the value
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(Object value) {
		this.value = value;
	}

	/**
	 * Promotes the given object to the Number object. If given object is null it is
	 * promoted to the Integer of value 0; If given string it is parsed to the
	 * appropriate Integer or Double value or exception is thrown if it can't be
	 * parsed into a number. If given Integer or Double it will just be returned.
	 * For all other object types will throw an exception with a message.
	 * 
	 * @param obj
	 *            object to be promoted to a number
	 * @param messagePrefix
	 *            exception message will start with this string
	 * @return number of the given object
	 */
	private Number promoteToNumber(Object obj, String messagePrefix) {
		if (obj == null)
			return Integer.valueOf(0);

		if (obj instanceof Double || obj instanceof Integer)
			return (Number) obj;

		if (obj instanceof String) {
			String value = (String) obj;
			try {
				return Integer.parseInt(value);
			} catch (NumberFormatException ignorable) {

			}

			try {
				return Double.parseDouble(value);
			} catch (NumberFormatException ignorable) {

			}
		}

		throw new IllegalArgumentException(messagePrefix
				+ " must be of type String, Integer, Double or null. If string, it must be parsable into a number.");
	}
	
	@Override
	public String toString() {
		if(value == null) return "null";
		
		return value.toString();
	}
}
