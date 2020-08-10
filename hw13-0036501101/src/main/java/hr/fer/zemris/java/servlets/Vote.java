package hr.fer.zemris.java.servlets;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Gets the parameter 'id' and updates the results of the voting for the users
 * favorite band. The parameter indicates which bands number of votes will be
 * incremented by one.
 * 
 * @author Disho
 *
 */
@SuppressWarnings("serial")
@WebServlet("/glasanje-glasaj")
public class Vote extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String fileName = req.getServletContext().getRealPath("/WEB-INF/glasanje-rezultati.txt");

		Map<Integer, Integer> votes = getVotes(fileName);
		int id = Integer.parseInt(req.getParameter("id"));
		int voteToIncerment = votes.get(id);
		votes.put(id, voteToIncerment + 1);

		saveVotes(votes, fileName);
		resp.sendRedirect(req.getContextPath() + "/glasanje-rezultati");
	}

	/**
	 * Reads numbers of votes for each band from the given file.
	 * 
	 * @param fileName
	 *            a path to the file containing results of the voting
	 * @return a map of pairs (id of the band, number of the votes)
	 * @throws IOException
	 */
	synchronized public static Map<Integer, Integer> getVotes(String fileName) throws IOException {
		Path path = Paths.get(fileName);
		Map<Integer, Integer> votes = new HashMap<>();

		Files.readAllLines(path).forEach(line -> {
			String[] splitted = line.split("\\t");
			int id = Integer.parseInt(splitted[0]);
			int bandVote = Integer.parseInt(splitted[1]);

			votes.put(id, bandVote);
		});

		return votes;
	}

	/**
	 * Saves the results of the voting to the file.
	 * 
	 * @param votes
	 *            a map of pairs (id of band, number of votes) that represent
	 *            results of voting
	 * @param fileName
	 *            a path to file to which the results will be saved to
	 * @throws IOException
	 */
	synchronized public static void saveVotes(Map<Integer, Integer> votes, String fileName) throws IOException {
		Path path = Paths.get(fileName);
		BufferedWriter writer = Files.newBufferedWriter(path, StandardOpenOption.TRUNCATE_EXISTING);

		StringBuilder sb = new StringBuilder();
		votes.forEach((id, bandVote) -> {
			sb.append(id).append("\t").append(bandVote).append("\n");
		});

		writer.append(sb.toString());
		writer.flush();
		writer.close();
	}

}
