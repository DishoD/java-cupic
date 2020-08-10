package hr.fer.zemris.java.hw07.shell;

/**
 * Exception thrown by shell when something goes wrong
 * with INPUT/OUTPUT actions.
 * 
 * @author Disho
 *
 */
@SuppressWarnings("serial")
public class ShellIOException extends RuntimeException {

	/**
	 * Initializes the exception.
	 */
	public ShellIOException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Initializes the exception with the given message.
	 * @param message exception message
	 */
	public ShellIOException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

}
