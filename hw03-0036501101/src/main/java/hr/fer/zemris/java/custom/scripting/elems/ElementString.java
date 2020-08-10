package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Represents a string.
 * 
 * @author Hrvoje Ditrih
 *
 */
public class ElementString extends Element {
	private String value;

	/**
	 * Initializes an element with a given text.
	 * 
	 * @param value
	 */
	public ElementString(String value) {
		super();
		this.value = value;
	}
	
	@Override
	public String asText() {
		return  "\"" + value.replace("\\", "\\\\").replace("\"", "\\\"").replace("\r", "\\r")
				.replace("\n", "\\n").replace("\t", "\\t") + "\"";
	}
	
	/**
	 * @return string text
	 */
	public String getValue() {
		return value;
	}
}
