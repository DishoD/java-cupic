package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * A web worker that reads clients session bgcolor parameter
 * and sets the background color of the '/index2.html' page.
 * 
 * @author Disho
 *
 */
public class Home implements IWebWorker {

	@Override
	public void processRequest(RequestContext context) throws Exception {
		String color = context.getPersistentParameter("bgcolor");
		if(color == null) {
			color = "7F7F7F";
		}
		
		context.setTemporaryParameter("background", color);
		context.getDispatcher().dispatchRequest("/private/home.smscr");
	}

}
