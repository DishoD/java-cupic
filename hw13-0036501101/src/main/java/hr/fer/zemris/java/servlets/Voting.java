package hr.fer.zemris.java.servlets;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Prepares a list of bands for the the users favorite band voting.
 * 
 * @author Disho
 *
 */
@SuppressWarnings("serial")
@WebServlet("/glasanje")
public class Voting extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String fileName = req.getServletContext().getRealPath("/WEB-INF/glasanje-definicija.txt");

		req.setAttribute("bandList", getBands(fileName));
		req.getRequestDispatcher("/WEB-INF/pages/glasanjeIndex.jsp").forward(req, resp);
	}

	/**
	 * Reads a list of the bands from the given file.
	 * 
	 * @param fileName
	 *            a path to a file containing bands
	 * @return a list of bands
	 * @throws IOException
	 */
	synchronized public static List<BandEntry> getBands(String fileName) throws IOException {
		Path path = Paths.get(fileName);
		List<BandEntry> bandList = new ArrayList<>();

		Files.readAllLines(path).forEach(line -> {
			String[] splitted = line.split("\\t");
			int id = Integer.parseInt(splitted[0]);
			String name = splitted[1];
			String songUrl = splitted[2];
			bandList.add(new BandEntry(id, name, songUrl));
		});

		return bandList;
	}

	/**
	 * Represents one band for the voting system.
	 * 
	 * @author Disho
	 *
	 */
	public static class BandEntry {
		/**
		 * bands unique id
		 */
		private int id;
		/**
		 * bands name
		 */
		private String name;
		/**
		 * an url link to some song of the band
		 */
		private String songUrl;

		/**
		 * Initializes an band entry with the given parameters.
		 * 
		 * @param id
		 *            bands unique id
		 * @param name
		 *            bands name
		 * @param songUrl
		 *            an url link to some song of the band
		 */
		public BandEntry(int id, String name, String songUrl) {
			this.id = id;
			this.name = name;
			this.songUrl = songUrl;
		}

		/**
		 * @return bands unique id
		 */
		public int getId() {
			return id;
		}

		/**
		 * @return bands name
		 */
		public String getName() {
			return name;
		}

		/**
		 * @return an url link to some song of the band
		 */
		public String getSongUrl() {
			return songUrl;
		}

	}
}
