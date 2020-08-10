package hr.fer.zemris.oopj.hw17.galerija.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.oopj.hw17.galerija.DB.PhotoDBProvider;
import hr.fer.zemris.oopj.hw17.galerija.DB.PhotoInfo;

/**
 * Retrieves an photo of the given parameter id.
 * 
 * @author Disho
 *
 */
@WebServlet("/photo")
public class Photo extends HttpServlet {
	
	private static final long serialVersionUID = 7745102429382615793L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int id = Integer.parseInt(req.getParameter("id"));
		PhotoInfo photo = PhotoDBProvider.getPhotoDB().getPhotoOfId(id);
		String name = photo.getName();
		req.getRequestDispatcher("WEB-INF/slike/" + name).forward(req, resp);
	}
}
