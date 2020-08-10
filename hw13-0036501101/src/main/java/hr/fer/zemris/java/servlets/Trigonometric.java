package hr.fer.zemris.java.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Generates an HTML document that shows a table of sin and cos trigonometric
 * values. Table has 3 columns. First column shows integer values of angles in
 * degrees. Those values are in range [a, b]. 0 <= a, b <= 360. Other two
 * columns show sin and cos values of those angles.
 * 
 * @author Disho
 *
 */
@SuppressWarnings("serial")
@WebServlet("/trigonometric")
public class Trigonometric extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String a = req.getParameter("a");
		String b = req.getParameter("b");

		int intA;
		int intB;

		try {
			intA = Integer.parseInt(a);
		} catch (Exception e) {
			intA = 0;
		}
		try {
			intB = Integer.parseInt(b);
		} catch (Exception e) {
			intB = 360;
		}

		if (intA > intB) {
			int temp = intA;
			intA = intB;
			intB = temp;
		}

		if (intB > intA + 720) {
			intB = intA + 720;
		}

		List<TrigEntry> trigList = new ArrayList<>();
		for (int i = intA; i <= intB; ++i) {
			trigList.add(new TrigEntry(i));
		}

		req.setAttribute("trigList", trigList);
		req.getRequestDispatcher("/WEB-INF/pages/trigonometric.jsp").forward(req, resp);
	}

	/**
	 * Represents one row of the table. Containing angle in degrees, sin and cos
	 * values of that angle.
	 * 
	 * @author Disho
	 *
	 */
	public static class TrigEntry {
		/**
		 * sin value of the angle
		 */
		private double sin;
		/**
		 * cos value of the angle
		 */
		private double cos;
		/**
		 * an angle in degrees
		 */
		private int deg;

		/**
		 * Initializes the entry for the integer value of the given angle in degrees.
		 * 
		 * @param deg
		 *            angle in degrees
		 */
		public TrigEntry(int deg) {
			this.sin = Math.sin(Math.toRadians(deg));
			this.cos = Math.cos(Math.toRadians(deg));
			this.deg = deg;
		}

		/**
		 * @return the sin value
		 */
		public double getSin() {
			return sin;
		}

		/**
		 * @return the cos value
		 */
		public double getCos() {
			return cos;
		}

		/**
		 * @return the angle
		 */
		public int getDeg() {
			return deg;
		}

	}
}
