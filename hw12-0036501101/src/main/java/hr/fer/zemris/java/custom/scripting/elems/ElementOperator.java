package hr.fer.zemris.java.custom.scripting.elems;


/**
 * Represents mathematical operator.
 * 
 * @author Hrvoje Ditrih
 *
 */
public class ElementOperator extends Element {
	private String symbol;

	/**
	 * Initializes a element with a given symbol.
	 * 
	 * @param symbol symbol of an operator
	 */
	public ElementOperator(String symbol) {
		this.symbol = symbol;
	}
	
	@Override
	public String asText() {
		return symbol;
	}
	
	/**
	 * @return symbol of an operator
	 */
	public String getSymbol() {
		return symbol;
	}
	
}
