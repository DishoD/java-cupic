package hr.fer.zemris.java.custom.scripting.exec;

/**
 * Exception indicating something went wrong while execution of the
 * smart script parser engine.
 * 
 * @author Disho
 *
 */
@SuppressWarnings("serial")
public class SmartScriptEngineException extends RuntimeException {

	/**
	 * Initialize the exception
	 */
	public SmartScriptEngineException() {
	}

	/**
	 * Initialize the exception with the given message
	 * 
	 * @param message exception message
	 */
	public SmartScriptEngineException(String message) {
		super(message);
	}

}
