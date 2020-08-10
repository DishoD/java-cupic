package hr.fer.zemris.oopj.hw17.galerija.servlets;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import hr.fer.zemris.oopj.hw17.galerija.DB.PhotoDB;
import hr.fer.zemris.oopj.hw17.galerija.DB.PhotoDBProvider;

/**
 * Initializes the server. Sets the PhotoDBProvider with the initialized
 * PhotoDB of the appropriate photo descriptor.
 * 
 * 
 * @author Disho
 *
 */
@WebListener
public class Initializator implements ServletContextListener{

	@Override
	public void contextDestroyed(ServletContextEvent e) {
	}

	@Override
	public void contextInitialized(ServletContextEvent e) {
		String descriptorPath = e.getServletContext().getRealPath("WEB-INF/opisnik.txt");
		try {
			PhotoDBProvider.setPhotoDB(new PhotoDB(descriptorPath));
			
			String pathT = e.getServletContext().getRealPath("WEB-INF/thumbnails");
			if(!Files.exists(Paths.get(pathT))) {
				Files.createDirectory(Paths.get(pathT));
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

}
