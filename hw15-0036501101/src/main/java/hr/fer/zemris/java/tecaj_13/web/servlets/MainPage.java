package hr.fer.zemris.java.tecaj_13.web.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.tecaj_13.dao.DAOProvider;

/**
 * Prepares a list of authors for the main blog page.
 * 
 * @author Disho
 *
 */
@SuppressWarnings("serial")
@WebServlet("/servleti/main")
public class MainPage extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		job(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		job(req, resp);
	}
	
	private void job(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setAttribute("authors", DAOProvider.getDAO().getNicks());
		req.getRequestDispatcher("/WEB-INF/pages/main.jsp").forward(req, resp);
	}
}
