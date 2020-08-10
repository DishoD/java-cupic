package hr.fer.zemris.java.tecaj.hw05.db.QueryParser;

/**
 * Exception thrown by QueryParser when something went wrong.
 * 
 * @author Hrvoje Ditrih
 *
 */
@SuppressWarnings("serial")
public class ParserException extends RuntimeException {

	/**
	 * Initializes an exception.
	 */
	public ParserException() {
		super();
	}

	/**
	 * Initializes an exception with a given message.
	 * 
	 * @param message
	 *            problem description
	 */
	public ParserException(String message) {
		super(message);
	}

}
