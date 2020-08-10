package hr.fer.zemris.java.hw03.prob1;

/**
 * Enumeration of token types for a simple lexer.
 * 
 * @author Hrvoje Ditrih
 *
 */
public enum TokenType {
	/**
	 * End of file
	 */
	EOF,
	
	/**
	 * Consists only of letters.
	 */
	WORD, 
	
	/**
	 * Consists only of numbers
	 */
	NUMBER, 
	
	/**
	 * Everything that isn't a letter, a number or a whitespace
	 */
	SYMBOL
}
