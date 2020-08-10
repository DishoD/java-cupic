package hr.fer.zemris.java.hw03.prob1;

/**
 * Defines an lexer state.
 * 
 * @author Hrvoje Ditrih
 *
 */
public enum LexerState {
	/**
	 * Reads WORDS, NUMBERS and SYMBOLS until '#'
	 */
	BASIC, 
	
	/**
	 * Reads everything as WORDS until SYMBOL '#'
	 */
	EXTENDED
}
