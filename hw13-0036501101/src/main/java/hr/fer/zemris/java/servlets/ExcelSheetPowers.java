package hr.fer.zemris.java.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * Generates an xls document. Each page contains two columns. First column
 * contains numbers from a to b and second column contains n-th power of that
 * number. Consequently, this servlet takes 3 parameters: a, b and n.
 * 
 * @author Disho
 *
 */
@SuppressWarnings("serial")
@WebServlet("/powers")
public class ExcelSheetPowers extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int a, b, n;
		try {
			a = Integer.parseInt(req.getParameter("a"));
			if (a < -100 || a > 100)
				throw new Exception();

			b = Integer.parseInt(req.getParameter("b"));
			if (b < -100 || b > 100)
				throw new Exception();

			n = Integer.parseInt(req.getParameter("n"));
			if (n < 1 || n > 5)
				throw new Exception();
		} catch (Exception e) {
			req.getRequestDispatcher("/WEB-INF/pages/powersIllegalParametars.jsp").forward(req, resp);
			return;
		}

		if (a > b) {
			int temp = a;
			a = b;
			b = temp;
		}

		HSSFWorkbook document = new HSSFWorkbook();

		for (int i = 1; i <= n; ++i) {
			HSSFSheet sheet = document.createSheet(Integer.toString(i));

			for (int j = a; j <= b; ++j) {
				HSSFRow row = sheet.createRow(j - a);
				row.createCell(0).setCellValue(j);
				row.createCell(1).setCellValue((int) Math.pow(j, i));
			}
		}

		document.close();

		resp.setContentType("application/vnd.ms-excel");
		resp.setHeader("Content-Disposition", "attachment; filename=\"tablica.xls\"");

		document.write(resp.getOutputStream());
	}
}
