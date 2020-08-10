package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * Exception representing that something went wrong within a SmartScriptLexer.
 * 
 * @author Hrvoje Ditrih
 *
 */
@SuppressWarnings("serial")
public class SmartLexerException extends RuntimeException{

	/**
	 * Initializes an exception.
	 */
	SmartLexerException() {
		super();
	}

	/**
	 * Initializes an exception with an given message.
	 * 
	 * @param message exception message
	 */
	SmartLexerException(String message) {
		super(message);
	}

	
}
