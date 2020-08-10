package hr.fer.zemris.java.hw07.shell;

/**
 * Exception thrown by shell environments when they have been terminated and
 * can't function anymore.
 * 
 * @author Disho
 *
 */
@SuppressWarnings("serial")
public class ShellTerminatedException extends RuntimeException {

	/**
	 * Initializes the exception.
	 */
	public ShellTerminatedException() {
		
	}

	/**
	 * Initializes the exception with the given message.
	 * 
	 * @param message exception message
	 */
	public ShellTerminatedException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}
}
