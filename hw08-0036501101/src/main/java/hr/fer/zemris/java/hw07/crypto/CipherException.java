package hr.fer.zemris.java.hw07.crypto;

/**
 * Used by program Crypto when Cipher throws an exception.
 * 
 * @author Disho
 *
 */
@SuppressWarnings("serial")
public class CipherException extends Exception {

	/**
	 * Initializes an exception
	 */
	public CipherException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Initializes an exception with the given message.
	 * 
	 * @param message exception message
	 */
	public CipherException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

}
