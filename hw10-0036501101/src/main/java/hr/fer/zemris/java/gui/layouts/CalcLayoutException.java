package hr.fer.zemris.java.gui.layouts;

/**
 * Exception thrown by CalcLayout when illegal constraint is given when adding an component.
 * 
 * @author Disho
 *
 */
@SuppressWarnings("serial")
public class CalcLayoutException extends RuntimeException {

	/**
	 * Initializes the exception.
	 */
	public CalcLayoutException() {
	}

	/**
	 * Initializes the exception with the given message.
	 * 
	 * @param message exception message
	 */
	public CalcLayoutException(String message) {
		super(message);
	}

}
