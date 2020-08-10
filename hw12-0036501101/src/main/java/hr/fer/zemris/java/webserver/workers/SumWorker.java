package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * A web worker that reads parameters 'a' and 'b'
 * and shows the result of summation of those parameters in the table.
 * If given parameters aren't numbers, default values will be used: a = 1 and b = 2.
 * 
 * @author Disho
 *
 */
public class SumWorker implements IWebWorker{

	@Override
	public void processRequest(RequestContext context) throws Exception {
		String a = context.getParameter("a");
		String b = context.getParameter("b");
		int numA = 1;
		int numB = 2;
		
		try {
			numA = Integer.parseInt(a);
			context.setTemporaryParameter("a", a);
		} catch(NumberFormatException e) {
			context.setTemporaryParameter("a", "1");
		}
		
		try {
			numB = Integer.parseInt(b);
			context.setTemporaryParameter("b", b);
		} catch(NumberFormatException e) {
			context.setTemporaryParameter("b", "2");
		}
		
		context.setTemporaryParameter("zbroj", Integer.toString(numA + numB));
		context.getDispatcher().dispatchRequest("/private/calc.smscr");
	}

}
