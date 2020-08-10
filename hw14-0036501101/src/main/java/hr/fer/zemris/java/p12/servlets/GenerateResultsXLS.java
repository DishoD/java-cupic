package hr.fer.zemris.java.p12.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import hr.fer.zemris.java.p12.dao.DAO;
import hr.fer.zemris.java.p12.dao.sql.SQLDAO;
import hr.fer.zemris.java.p12.model.PollOption;

/**
 * This servlet generates an xls document. It represents a results of users
 * poll voting.
 * 
 * @author Disho
 *
 */
@SuppressWarnings("serial")
@WebServlet("/servleti/glasanje-xls")
public class GenerateResultsXLS extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		long pollId = Long.parseLong(req.getParameter("pollID"));

		DAO dao = new SQLDAO();

		List<PollOption> results = dao.getPollOptions(pollId);

		HSSFWorkbook document = new HSSFWorkbook();

		HSSFSheet sheet = document.createSheet("Rezultati glasovanja");

		for (int i = 0; i < results.size(); ++i) {
			HSSFRow row = sheet.createRow(i);
			row.createCell(0).setCellValue(results.get(i).title);
			row.createCell(1).setCellValue(results.get(i).votes);
		}

		document.close();

		resp.setContentType("application/vnd.ms-excel");
		resp.setHeader("Content-Disposition", "attachment; filename=\"rezultati glasovanja.xls\"");

		document.write(resp.getOutputStream());
	}
}
