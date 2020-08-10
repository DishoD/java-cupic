package hr.fer.zemris.java.tecaj.hw05.db.QueryParser;

/**
 * Exception representing that something went wrong within a lexer.
 * 
 * @author Hrvoje Ditrih
 *
 */
@SuppressWarnings("serial")
public class LexerException extends RuntimeException {

	/**
	 * Initializes an exception.
	 */
	LexerException() {
		super();
	}

	/**
	 * Initializes an exception with an given message.
	 * 
	 * @param message
	 *            exception message
	 */
	LexerException(String message) {
		super(message);
	}

}
