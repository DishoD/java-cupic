package hr.fer.zemris.java.p12.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.p12.dao.DAO;
import hr.fer.zemris.java.p12.dao.sql.SQLDAO;

/**
 * Increments a vote count of chosen poll option.
 * 
 * @author Disho
 *
 */
@SuppressWarnings("serial")
@WebServlet("/servleti/glasanje-glasaj")
public class Vote extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		long id = Long.parseLong(req.getParameter("id"));
		
		DAO dao = new SQLDAO();
		dao.incerementPollOptionVotes(id);
		
		long pollId = dao.getPollOptionofID(id).getPollId();
		
		req.getRequestDispatcher("/servleti/glasanje-rezultati?pollID=" + pollId).forward(req, resp);
	}
}
