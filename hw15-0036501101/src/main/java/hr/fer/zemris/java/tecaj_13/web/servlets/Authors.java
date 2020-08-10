package hr.fer.zemris.java.tecaj_13.web.servlets;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.tecaj_13.dao.DAO;
import hr.fer.zemris.java.tecaj_13.dao.DAOProvider;
import hr.fer.zemris.java.tecaj_13.model.BlogEntry;
import hr.fer.zemris.java.tecaj_13.model.BlogUser;

/**
 * Handles the viewing, editing and creating new blog entries and comments on entries.
 * Validates user login to show more options for the registered blog users.
 * 
 * @author Disho
 *
 */
@SuppressWarnings("serial")
@WebServlet("/servleti/author/*")
public class Authors extends HttpServlet {
	/**
	 * flag that represents a validity of the current user login and current page
	 */
	private Boolean validLogin;
	/**
	 * dao implementation used in this servlet
	 */
	private DAO dao = DAOProvider.getDAO();
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String[] info = req.getPathInfo().substring(1).split("/");
		
		if(info.length == 0 || info.length > 2 || !dao.getNicks().contains(info[0])) {
			Util.sendInfo("Error - illegal page", req, resp);
			return;
		}
		
		BlogUser user = dao.getBlogUserOfNick(info[0]);
		validateUser(user, req, resp);
		req.setAttribute("nick", info[0]);
		
		if(info.length == 1) {
			List<BlogEntry> blogEntries = user.getBlogEntries();
			req.setAttribute("blogEntries", blogEntries.size() == 0 ? null : blogEntries);
			req.getRequestDispatcher("/WEB-INF/pages/listBlogEntries.jsp").forward(req, resp);
			return;
		}
		
		//else info.length == 2
		
		if(info[1].equals("new")) {
			if(!validLogin) {
				Util.sendInfo("Error - illegal page", req, resp);
				return;
			}
			
			req.getRequestDispatcher("/WEB-INF/pages/newBlogEntry.jsp").forward(req, resp);
			return;
		}
		
		if(info[1].equals("edit")) {
			if(!validLogin) {
				Util.sendInfo("Error - illegal page", req, resp);
				return;
			}
			
			long eid = Long.parseLong(req.getParameter("eid"));
			BlogEntry be = dao.getBlogEntry(eid);
			if(be == null) {
				Util.sendInfo("Error - illegal page", req, resp);
				return;
			}
			
			if(!user.getBlogEntries().contains(be)) {
				Util.sendInfo("Error - illegal page", req, resp);
				return;
			}
			
			req.setAttribute("blogEntry", be);
			req.getRequestDispatcher("/WEB-INF/pages/editBlogEntry.jsp").forward(req, resp);
			return;
		}
		
		long eid;
		try {
			eid = Long.parseLong(info[1]);
		} catch(NumberFormatException e) {
			Util.sendInfo("Error - illegal page", req, resp);
			return;
		}
		BlogEntry be = dao.getBlogEntry(eid);
		
		if(be == null) {
			Util.sendInfo("Error - illegal page", req, resp);
			return;
		}
		
		req.setAttribute("blogEntry", be);
		req.getRequestDispatcher("/WEB-INF/pages/blogEntry.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String[] info = req.getPathInfo().substring(1).split("/");
		
		BlogUser user = dao.getBlogUserOfNick(info[0]);
		validateUser(user, req, resp);
		
		if(info[1].equals("new")) {
			if(!validLogin) {
				Util.sendInfo("Error - illegal page", req, resp);
				return;
			}
			
			String title = req.getParameter("title");
			String text = req.getParameter("text");
			
			if(title.isEmpty() || text.isEmpty()) {
				Util.sendInfo("Blog entry must have non empty title and text.", req, resp);
				return;
			}
			
			BlogEntry be = dao.addBlogEntryOfUser(user, title, text);
			resp.sendRedirect("/blog" + req.getServletPath() + "/" + user.getNick() + "/" + be.getId());
			return;
		}
		
		if(info[1].equals("edit")) {
			if(!validLogin) {
				Util.sendInfo("Error - illegal page", req, resp);
				return;
			}
			
			long eid = Long.parseLong(req.getParameter("eid"));
			String title = req.getParameter("title");
			String text = req.getParameter("text");
			
			if(title.isEmpty() || text.isEmpty()) {
				Util.sendInfo("Blog entry must have non empty title and text.", req, resp);
				return;
			}
			
			BlogEntry be = dao.getBlogEntry(eid);
			
			be.setTitle(title);
			be.setText(text);
			be.setLastModifiedAt(new Date());
			resp.sendRedirect("/blog" + req.getServletPath() + "/" + user.getNick() + "/" + eid);
			return;
		}
		
		long eid;
		try {
			eid = Long.parseLong(info[1]);
		} catch(NumberFormatException e) {
			Util.sendInfo("Error - illegal page", req, resp);
			return;
		}
		BlogEntry be = dao.getBlogEntry(eid);
		
		if(be == null) {
			Util.sendInfo("Error - illegal page", req, resp);
			return;
		}
		
		String email = req.getParameter("email");
		String message = req.getParameter("message");
		
		if(email.isEmpty() || message.isEmpty()) {
			Util.sendInfo("Comment must have non empty e-mail and message.", req, resp);
			return;
		}
		
		if(!Util.validateEmail(email)) {
			Util.sendInfo("Illegal e-mail", req, resp);
			return;
		}
		
		dao.addCommentToBlogEntry(email, message, be);
		
		resp.sendRedirect("/blog" + req.getServletPath() + req.getPathInfo());
	}
	
	/**
	 * Checks if the given user has credentials for more options on /author/* pages.
	 * Sets the validLogin flag to true if valid, false otherwise.
	 * 
	 * @param user user to check validity of
	 * @param req http request
	 * @param resp http response
	 */
	private void validateUser(BlogUser user, HttpServletRequest req, HttpServletResponse resp) {
		Long loggedInUserId = (Long) req.getSession().getAttribute("current.user.id");
		
		if(user.getId() == loggedInUserId) {
			validLogin = true;
		} else {
			validLogin = false;
		}
		
		req.setAttribute("validLogin", validLogin);
	}
}
