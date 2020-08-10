package hr.fer.zemris.java.custom.collections.demo;

/**
 * Enumeration for main mathematical operators.
 * 
 * @author Hrvoje Ditrih
 *
 */
public enum Operator {
	PLUS("+"), 
	MINUS("-"), 
	MULTIPLY("*"), 
	DIVIDE("/"), 
	MODULO("%"), 
	NOP("This is not an operator.");
	
	private String stringRepresentation;
	
	/**
	 * Initializes an enum with given mathematical notation.
	 * 
	 * @param stringRepresentation mathematical notation
	 */
	private Operator(String stringRepresentation) {
		this.stringRepresentation = stringRepresentation;
	}
	
	/**
	 * Get string representation of an operator.
	 * @return string representation of an operator
	 */
	public String getStringRepresentation() {
		return stringRepresentation;
	}
}
