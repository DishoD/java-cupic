package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * A web worker that reads bgcolor parameter and changes the
 * background color of the Home '/index2.html' page.
 * 
 * @author Disho
 *
 */
public class BgColorWorker implements IWebWorker {

	@Override
	public void processRequest(RequestContext context) throws Exception {
		String color = context.getParameter("bgcolor");
		
		boolean valid = true;
		
		if(color != null && color.length() == 6) {
			try {
				Integer.parseInt(color, 16);
			} catch(NumberFormatException e) {
				valid = false;
			}
			
			if(valid) {
				context.setPersistentParameter("bgcolor", color);
				context.setTemporaryParameter("cc", "Background color changed!");
				context.getDispatcher().dispatchRequest("/index2.html");
				return;
			}
		}
		
		context.setTemporaryParameter("cc", "Background color not changed!");
		context.getDispatcher().dispatchRequest("/index2.html");
		
	}

}
