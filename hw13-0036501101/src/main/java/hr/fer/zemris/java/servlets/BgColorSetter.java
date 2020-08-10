package hr.fer.zemris.java.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Sets the background color for the all the web pages of this web application.
 * But only for the current user session.
 * 
 * @author Disho
 *
 */
@SuppressWarnings("serial")
@WebServlet("/setcolor")
public class BgColorSetter extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getSession().setAttribute("pickedBgCol", req.getParameter("color"));
		req.getRequestDispatcher("/colors.jsp").forward(req, resp);
	}
}
