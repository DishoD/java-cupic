package hr.fer.zemris.java.p12.servlets;

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

import hr.fer.zemris.java.p12.dao.DAO;
import hr.fer.zemris.java.p12.dao.sql.SQLDAO;
import hr.fer.zemris.java.p12.model.PollOption;

/**
 * This servlet generates an png image of a bar chart. It represents a results
 * of users poll voting.
 * 
 * @author Disho
 *
 */
@SuppressWarnings("serial")
@WebServlet("/servleti/glasanje-grafika")
public class GenerateResultsChart extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		long pollId = Long.parseLong(req.getParameter("pollID"));

		DAO dao = new SQLDAO();

		List<PollOption> results = dao.getPollOptions(pollId);
		
		DefaultPieDataset result = new DefaultPieDataset();
		results.forEach(r -> result.setValue(r.title, r.votes));

		JFreeChart chart = ChartFactory.createPieChart3D("Result of voting", result, true, true, false);

		PiePlot3D plot = (PiePlot3D) chart.getPlot();
		plot.setStartAngle(290);
		plot.setDirection(Rotation.CLOCKWISE);
		// plot.setForegroundAlpha(0.5f);
		
		resp.setContentType("image/png");
		ChartUtils.writeChartAsPNG(resp.getOutputStream(), chart, 600, 400);
	}
}
