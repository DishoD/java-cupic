package hr.fer.zemris.java.servlets;

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

import hr.fer.zemris.java.servlets.VotingResults.ResultEntry;

/**
 * This servlet generates an xls document. It represents a results of users
 * voting for their favorite band. The servlet reads the results of the voting
 * from the file.
 * 
 * @author Disho
 *
 */
@SuppressWarnings("serial")
@WebServlet("/glasanje-xls")
public class BandsXLS extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String resultFileName = req.getServletContext().getRealPath("/WEB-INF/glasanje-rezultati.txt");
		String definitionFileName = req.getServletContext().getRealPath("/WEB-INF/glasanje-definicija.txt");

		List<ResultEntry> bandReesults = VotingResults.getResults(resultFileName, definitionFileName);

		HSSFWorkbook document = new HSSFWorkbook();

		HSSFSheet sheet = document.createSheet("Rezultati glasovanja");

		for (int i = 0; i < bandReesults.size(); ++i) {
			HSSFRow row = sheet.createRow(i);
			row.createCell(0).setCellValue(bandReesults.get(i).getBand().getName());
			row.createCell(1).setCellValue(bandReesults.get(i).getVotes());
		}

		document.close();

		resp.setContentType("application/vnd.ms-excel");
		resp.setHeader("Content-Disposition", "attachment; filename=\"rezultati glasovanja.xls\"");

		document.write(resp.getOutputStream());
	}
}
