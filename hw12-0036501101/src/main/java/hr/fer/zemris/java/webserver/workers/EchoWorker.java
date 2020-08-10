package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * A web worker that prints pairs of the
 * parameter names and values in the table.
 * 
 * @author Disho
 *
 */
public class EchoWorker implements IWebWorker{

	@Override
	public void processRequest(RequestContext context) throws Exception {
		context.setMimeType("text/html");
		
		context.write("<HTML>\n");
		context.write("<style>\n" + 
				 	  "table, th, td {\n" + 
				 	  "    border: 1px solid black;\n" + 
				 	  "    border-collapse: collapse;\n" + 
				 	  "}\n" + 
					  "</style>"
		);
		context.write("<table>\n");
		context.write("<tr> <th>NAME</th> <th>VALUE</th> </tr>\n");
		
		for(String name : context.getParameterNames()) {
			context.write(String.format("<tr> <td>%s</td> <td>%s</td> </tr>\n", name, context.getParameter(name)));
		}
		
		context.write("</HTML>\n");
		context.write("</table>\n");
	}

}
