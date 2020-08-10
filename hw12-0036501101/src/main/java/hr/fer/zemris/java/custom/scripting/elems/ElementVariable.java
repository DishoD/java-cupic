package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Represents a variable name.
 * 
 * @author Hrvoje Ditrih
 *
 */
public class ElementVariable extends Element{
	private String name;

	/**
	 * Initializes element with a given name.
	 * @param name
	 */
	public ElementVariable(String name) {
		this.name = name;
	}
	
	@Override
	public String asText() {
		return name;
	}
}
