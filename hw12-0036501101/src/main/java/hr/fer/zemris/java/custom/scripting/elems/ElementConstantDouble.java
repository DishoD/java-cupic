package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Represents constant double expression.
 * 
 * @author Hrvoje Ditrih
 *
 */
public class ElementConstantDouble extends Element {
	private double value;

	/**
	 * Initializes an element with a given decimal number.
	 * 
	 * @param value decimal number
	 */
	public ElementConstantDouble(double value) {
		super();
		this.value = value;
	}

	@Override
	public String asText() {
		return Double.toString(value);
	}

	/**
	 * @return decimal number
	 */
	public double getValue() {
		return value;
	}
}
