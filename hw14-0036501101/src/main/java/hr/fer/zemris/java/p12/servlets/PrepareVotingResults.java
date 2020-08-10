package hr.fer.zemris.java.p12.servlets;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.p12.dao.DAO;
import hr.fer.zemris.java.p12.dao.sql.SQLDAO;
import hr.fer.zemris.java.p12.model.PollOption;

/**
 * Prepares the results of the voting for the users poll voting result page.
 * 
 * @author Disho
 *
 */
@SuppressWarnings("serial")
@WebServlet("/servleti/glasanje-rezultati")
public class PrepareVotingResults extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		long pollId = Long.parseLong(req.getParameter("pollID"));

		DAO dao = new SQLDAO();

		List<PollOption> results = dao.getPollOptions(pollId);
		results.sort(Comparator.comparingLong(PollOption::getVotes).reversed());

		req.setAttribute("results", results);

		long maxVotes = results.stream().mapToLong(PollOption::getVotes).max().getAsLong();

		List<PollOption> bestOptions = results.stream().filter(o -> o.votes == maxVotes).collect(Collectors.toList());

		req.setAttribute("bestOptions", bestOptions);

		req.getRequestDispatcher("/WEB-INF/pages/results.jsp").forward(req, resp);
	}

}
