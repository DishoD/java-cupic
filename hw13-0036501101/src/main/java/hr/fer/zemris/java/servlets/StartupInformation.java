package hr.fer.zemris.java.servlets;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * On a server startup this listener remembers the current (startup) time (in milliseconds) in
 * the application parameter 'startTime'.
 *  
 * @author Disho
 *
 */
@WebListener
public class StartupInformation implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent e) {
		
	}

	@Override
	public void contextInitialized(ServletContextEvent e) {
		e.getServletContext().setAttribute("startTime", System.currentTimeMillis());
	}

}
