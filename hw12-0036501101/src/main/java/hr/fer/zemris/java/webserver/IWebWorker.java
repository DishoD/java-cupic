package hr.fer.zemris.java.webserver;

/**
 * A web worker interface.
 * 
 * @author Disho
 *
 */
public interface IWebWorker {
	/**
	 * Processes the web request on the given context.
	 * 
	 * @param context request context
	 * @throws Exception
	 */
	public void processRequest(RequestContext context) throws Exception;
}
