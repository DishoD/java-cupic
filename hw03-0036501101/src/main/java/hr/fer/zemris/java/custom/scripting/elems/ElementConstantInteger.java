package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Represents constant integer expression.
 * 
 * @author Hrvoje Ditrih
 *
 */
public class ElementConstantInteger extends Element {
	private int value;

	/**
	 * Initializes an element with a given value.
	 * 
	 * @param value integer value
	 */
	public ElementConstantInteger(int value) {
		this.value = value;
	}

	@Override
	public String asText() {
		return Integer.toString(value);
	}
}
