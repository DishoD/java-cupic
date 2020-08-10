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
 * Receives the registration form data, validates it and registers an new user.
 * If given invalid data redirects to an error information page with the appropriate message.
 * 
 * @author Disho
 *
 */
@SuppressWarnings("serial")
@WebServlet("/servleti/registerUser")
public class RegisterUser extends HttpServlet {
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String firstName = req.getParameter("fn");
		if(firstName.isEmpty()) {
			Util.sendInfo("First name must not be empty.", req, resp);
			return;
		}
		if(firstName.length() < 2) {
			Util.sendInfo("First name too short. It must contain at least 2 characters.", req, resp);
			return;
		}
		if(firstName.length() > 20) {
			Util.sendInfo("First name too long. It must be shorter than 20 characters.", req, resp);
			return;
		}
		
		String lastName = req.getParameter("ln");
		if(lastName.isEmpty()) {
			Util.sendInfo("Last name must not be empty.", req, resp);
			return;
		}
		if(lastName.length() < 2) {
			Util.sendInfo("Last name too short. It must contain at least 2 characters.", req, resp);
			return;
		}
		if(lastName.length() > 20) {
			Util.sendInfo("Last name too long. It must be shorter than 20 characters.", req, resp);
			return;
		}
		
		String email = req.getParameter("email");
		if(email.isEmpty()) {
			Util.sendInfo("E-mail name must not be empty.", req, resp);
			return;
		}
		
		if(!Util.validateEmail(email)) {
			Util.sendInfo("Invalid e-mail address.", req, resp);
			return;
		}
		
		String nick = req.getParameter("nick");
		if(nick.isEmpty()) {
			Util.sendInfo("Nick name must not be empty.", req, resp);
			return;
		}
		if(nick.length() < 2) {
			Util.sendInfo("Nick too short. It must contain at least 2 characters.", req, resp);
			return;
		}
		if(nick.length() > 20) {
			Util.sendInfo("Nick too long. It must be shorter than 20 characters.", req, resp);
			return;
		}
		
		if(DAOProvider.getDAO().getNicks().contains(nick)) {
			Util.sendInfo("User with the given nick '" + nick + "' already exists.", req, resp);
			return;
		}
		
		String password = req.getParameter("password");
		if(password.isEmpty()) {
			Util.sendInfo("Password name must not be empty.", req, resp);
			return;
		}
		if(password.length() < 6) {
			Util.sendInfo("Password name be at least 6 characters long.", req, resp);
			return;
		}
		
		String passwordHash = Util.hahsPassword(password);
		
		BlogUser newUser = new BlogUser();
		newUser.setFirstName(firstName);
		newUser.setLastName(lastName);
		newUser.setEmail(email);
		newUser.setNick(nick);
		newUser.setPasswordHash(passwordHash);
		
		DAOProvider.getDAO().registerUser(newUser);
		Util.sendInfo("A new user under nick '"+ nick + "' successfully registered.", req, resp);
	}
}
