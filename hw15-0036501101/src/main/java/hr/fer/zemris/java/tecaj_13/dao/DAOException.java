package hr.fer.zemris.java.tecaj_13.dao;

/**
 * Something went wrong in execution of the DAO methods.
 * 
 * @author Disho
 *
 */
public class DAOException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Initializes the exception with the given message and cause.
	 * 
	 * @param message exception message
	 * @param cause exception cause
	 */
	public DAOException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Initializes the exception with the given message.
	 * 
	 * @param message exception message
	 */
	public DAOException(String message) {
		super(message);
	}
}