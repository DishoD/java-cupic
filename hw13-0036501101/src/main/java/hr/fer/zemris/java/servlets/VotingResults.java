package hr.fer.zemris.java.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.servlets.Voting.BandEntry;

/**
 * Prepares the results of the voting for the users favorite band voting result
 * page.
 * 
 * @author Disho
 *
 */
@SuppressWarnings("serial")
@WebServlet("/glasanje-rezultati")
public class VotingResults extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String resultFileName = req.getServletContext().getRealPath("/WEB-INF/glasanje-rezultati.txt");
		String definitionFileName = req.getServletContext().getRealPath("/WEB-INF/glasanje-definicija.txt");

		List<ResultEntry> results = getResults(resultFileName, definitionFileName);
		results.sort(Comparator.comparingInt(ResultEntry::getVotes).reversed());
		req.setAttribute("results", results);

		int maxVotes = results.stream().mapToInt(ResultEntry::getVotes).max().getAsInt();
		List<ResultEntry> bestBands = results.stream().filter(r -> r.getVotes() == maxVotes).collect(Collectors.toList());
		req.setAttribute("bestBands", bestBands);

		req.getRequestDispatcher("/WEB-INF/pages/glasanjeRez.jsp").forward(req, resp);
	}

	/**
	 * Reads the results of the voting from the given files.
	 * 
	 * @param resultFileName
	 *            a path to a file containing the results of the voting
	 * @param definitionFileName
	 *            a path to a file containing band definitions
	 * @return a list of result entries
	 * @throws IOException
	 */
	public static List<ResultEntry> getResults(String resultFileName, String definitionFileName) throws IOException {
		Map<Integer, Integer> votes = Vote.getVotes(resultFileName);
		List<BandEntry> bands = Voting.getBands(definitionFileName);
		List<ResultEntry> results = new ArrayList<>();

		bands.forEach(band -> {
			results.add(new ResultEntry(band, votes.get(band.getId())));
		});

		return results;
	}

	/**
	 * Represents a bands result of voting.
	 * 
	 * @author Disho
	 *
	 */
	public static class ResultEntry {
		/**
		 * a band
		 */
		private BandEntry band;
		/**
		 * number of votes
		 */
		private int votes;

		/**
		 * Initializes the result entry with the given parameters.
		 * 
		 * @param band
		 *            a band
		 * @param votes
		 *            number of votes
		 */
		public ResultEntry(BandEntry band, int votes) {
			this.band = band;
			this.votes = votes;
		}

		/**
		 * @return the band
		 */
		public BandEntry getBand() {
			return band;
		}

		/**
		 * @return the number of votes
		 */
		public int getVotes() {
			return votes;
		}

	}
}
