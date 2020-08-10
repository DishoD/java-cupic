package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Represents function expression.
 * 
 * @author Hrvoje Ditrih
 *
 */
public class ElementFunction extends Element {
	private String name;

	/**
	 * Initializes an element with a given name.
	 * 
	 * @param name function name
	 */
	public ElementFunction(String name) {
		this.name = name;
	}

	@Override
	public String asText() {
		return name;
	}

	/**
	 * @return function name
	 */
	public String getName() {
		return name;
	}
}
