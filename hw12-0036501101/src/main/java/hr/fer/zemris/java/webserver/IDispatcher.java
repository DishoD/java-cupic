package hr.fer.zemris.java.webserver;

/**
 * The dispatcher interface.
 * 
 * @author Disho
 *
 */
public interface IDispatcher {
	/**
	 * Processes the given url request.
	 * 
	 * @param urlPath url path of the request
	 * @throws Exception
	 */
	void dispatchRequest(String urlPath) throws Exception;
}
