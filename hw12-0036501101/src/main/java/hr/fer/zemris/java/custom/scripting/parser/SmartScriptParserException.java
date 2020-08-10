package hr.fer.zemris.java.custom.scripting.parser;

/**
 * Exception thrown by SmartScriptParser when something went wrong.
 * 
 * @author Hrvoje Ditrih
 *
 */
@SuppressWarnings("serial")
public class SmartScriptParserException extends RuntimeException {

	/**
	 * Initializes an exception.
	 */
	public SmartScriptParserException() {
		super();
	}

	/**
	 * Initializes an exception with a given message.
	 * 
	 * @param message
	 *            problem description
	 */
	public SmartScriptParserException(String message) {
		super(message);
	}

}
