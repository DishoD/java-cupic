package hr.fer.zemris.java.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.util.Rotation;
import org.jfree.data.general.DefaultPieDataset;

import hr.fer.zemris.java.servlets.VotingResults.ResultEntry;

/**
 * This servlet generates an png image of a bar chart. It represents a results
 * of users voting for their favorite band. The servlet reads the results of the
 * voting from the file.
 * 
 * @author Disho
 *
 */
@SuppressWarnings("serial")
@WebServlet("/glasanje-grafika")
public class BandsChart extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String resultFileName = req.getServletContext().getRealPath("/WEB-INF/glasanje-rezultati.txt");
		String definitionFileName = req.getServletContext().getRealPath("/WEB-INF/glasanje-definicija.txt");
		
		List<ResultEntry> bandReesults = VotingResults.getResults(resultFileName, definitionFileName);
		DefaultPieDataset result = new DefaultPieDataset();
		bandReesults.forEach(r -> result.setValue(r.getBand().getName(), r.getVotes()));

		JFreeChart chart = ChartFactory.createPieChart3D("Result of voting", result, true, true, false);

		PiePlot3D plot = (PiePlot3D) chart.getPlot();
		plot.setStartAngle(290);
		plot.setDirection(Rotation.CLOCKWISE);
		// plot.setForegroundAlpha(0.5f);
		
		resp.setContentType("image/png");
		ChartUtils.writeChartAsPNG(resp.getOutputStream(), chart, 600, 400);
	}
}
