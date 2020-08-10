package hr.fer.zemris.java.p12.dao;

/**
 * Something went wrong with accessing the database or passed illegal parameters.
 * 
 * @author Disho
 *
 */
public class DAOException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * initializes the exception
	 */
	public DAOException() {
	}


	/**
	 * initializes the exception with the given message
	 * @param message
	 */
	public DAOException(String message) {
		super(message);
	}
	
	
	/**
	 * Initializes the exception with the given cause.
	 * 
	 * @param cause exception cause
	 */
	public DAOException(Throwable cause) {
		super(cause);
	}
}