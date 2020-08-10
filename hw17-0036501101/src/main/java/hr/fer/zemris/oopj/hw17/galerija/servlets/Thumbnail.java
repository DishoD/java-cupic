package hr.fer.zemris.oopj.hw17.galerija.servlets;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.oopj.hw17.galerija.DB.PhotoDBProvider;
import hr.fer.zemris.oopj.hw17.galerija.DB.PhotoInfo;

/**
 * Retrieves a small 150px by 150px thumbnail for the photo of the given id. If
 * the thumbnail doesn't exist it will generate it and save it for later fast
 * retrieval.
 * 
 * @author Disho
 *
 */
@WebServlet("/thumbnail")
public class Thumbnail extends HttpServlet {
	private static final long serialVersionUID = -5771600687591913039L;
	/**
	 * width and height of the thumbnail
	 */
	private static final int SIZE = 150;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int id = Integer.parseInt(req.getParameter("id"));
		PhotoInfo photo = PhotoDBProvider.getPhotoDB().getPhotoOfId(id);
		String name = photo.getName();

		String pathT = req.getServletContext().getRealPath("WEB-INF/thumbnails");
		if (!Files.exists(Paths.get(pathT))) {
			try {
				Files.createDirectory(Paths.get(pathT));
			} catch (IOException ignorable) {
			}
		}

		String path = req.getServletContext().getRealPath("WEB-INF/thumbnails/" + name);
		if (!Files.exists(Paths.get(path))) {

			File fileL = Paths.get(req.getServletContext().getRealPath("WEB-INF/slike/" + name)).toFile();
			BufferedImage img = ImageIO.read(fileL);
			Image tmp = img.getScaledInstance(SIZE, SIZE, Image.SCALE_FAST);
			BufferedImage resized = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_RGB);
			Graphics2D g2d = resized.createGraphics();
			g2d.drawImage(tmp, 0, 0, null);
			g2d.dispose();

			File file = Paths.get(path).toFile();
			resp.setContentType("image/" + photo.getImageType().toLowerCase());
			ImageIO.write(resized, photo.getImageType(), resp.getOutputStream());
			resp.flushBuffer();

			Runnable job = () -> {
				try {
					ImageIO.write(resized, photo.getImageType(), file);
				} catch (IOException e) {
					e.printStackTrace();
				}
			};

			Thread thread = new Thread(job);
			thread.setDaemon(true);
			thread.start();

			return;
		}

		req.getRequestDispatcher("WEB-INF/thumbnails/" + name).forward(req, resp);

	}
}
