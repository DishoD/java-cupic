package hr.fer.zemris.java.custom.collections;

/**
 * Exception that signals that operation was run on an empty stack.
 * 
 * @author Hrvoje Ditrih
 *
 */
@SuppressWarnings("serial")
public class EmptyStackException extends RuntimeException {

	/**
	 * Constructs new EmptyStackException with an given message.
	 * 
	 * @param message
	 */
	public EmptyStackException(String message) {
		super(message);
	}
	
	/**
	 * Constructs new EmptyStackException.
	 */
	public EmptyStackException() {
		
	}
}
