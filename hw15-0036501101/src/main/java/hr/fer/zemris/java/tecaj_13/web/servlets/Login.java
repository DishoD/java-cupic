package hr.fer.zemris.java.tecaj_13.web.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.tecaj_13.dao.DAOProvider;
import hr.fer.zemris.java.tecaj_13.model.BlogUser;

/**
 * Receives an login form data and tries to log in an user. If data is not valid,
 * redirects to the login page with the error message.
 * 
 * @author Disho
 *
 */
@SuppressWarnings("serial")
@WebServlet("/servleti/login")
public class Login extends HttpServlet {
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String nick = req.getParameter("nick");
		if(nick.isEmpty()) {
			sendLoginError("You must provide a nick to log in.", nick, req, resp);
			return;
		}
		
		if(!DAOProvider.getDAO().getNicks().contains(nick)) {
			sendLoginError("User with the given nick '"+ nick + "' is not registered.", nick, req, resp);
			return;
		}
		
		String password = req.getParameter("password");
		if(password.isEmpty()) {
			sendLoginError("Password must not be empty.", nick, req, resp);
			return;
		}
		String passwordHash = Util.hahsPassword(password);
		
		BlogUser user = DAOProvider.getDAO().getBlogUserOfNick(nick);
		
		if(!passwordHash.equals(user.getPasswordHash())) {
			sendLoginError("Incorrect password.", nick, req, resp);
			return;
		}
		
		req.getSession().setAttribute("current.user.id", user.getId());
		req.getSession().setAttribute("current.user.fn", user.getFirstName());
		req.getSession().setAttribute("current.user.ln", user.getLastName());
		req.getSession().setAttribute("current.user.nick", user.getNick());
		
		resp.sendRedirect("main");
	}
	
	private void sendLoginError(String msg, String nick, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setAttribute("errorMsg", msg);
		req.setAttribute("nick", nick);
		req.getRequestDispatcher("/servleti/main").forward(req, resp);
	}
}
