package hr.fer.zemris.java.p12.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.p12.dao.DAO;
import hr.fer.zemris.java.p12.dao.sql.SQLDAO;
import hr.fer.zemris.java.p12.model.Poll;
import hr.fer.zemris.java.p12.model.PollOption;

/**
 * Prepares a list of poll options of the poll for the user voting.
 * 
 * @author Disho
 *
 */
@SuppressWarnings("serial")
@WebServlet("/servleti/glasanje")
public class PreparePollOptions extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		long pollId = Long.parseLong(req.getParameter("pollID"));
		
		DAO dao = new SQLDAO();
		
		List<PollOption> pollOptions = dao.getPollOptions(pollId);
		Poll poll = dao.getPollofID(pollId);
		
		req.setAttribute("poll", poll);
		req.setAttribute("pollOptions", pollOptions);
		
		req.getRequestDispatcher("/WEB-INF/pages/pollOptions.jsp").forward(req, resp);
	}

}
