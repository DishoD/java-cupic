package hr.fer.zemris.java.gui.charts;

import java.util.List;
import java.util.Objects;

/**
 * Represents a bar chart. Holds information on strings associated with x and y
 * values, height of each bar on x position, minimum and maximum y value and gap
 * between y values.
 * 
 * @author Disho
 *
 */
public class BarChart {
	/**
	 * (x,y) -> x - represents x coordinate, y - represents a height of the bar
	 */
	private List<XYValue> XYValues;
	/**
	 * string associated with the x axis
	 */
	private String xString;
	/**
	 * string associated with the y axis
	 */
	private String yString;
	/**
	 * minimum value of the y axis
	 */
	private int yMin;
	/**
	 * maximum value of the y axis
	 */
	private int yMax;
	/**
	 * numerical gap between each y value on the axis
	 */
	private int yGap;

	/**
	 * Initializes the bar chart with the given parameters.
	 * 
	 * @param xYValues (x,y) -> x - represents x coordinate, y - represents a height of the bar
	 * @param xString string associated with the x axis
	 * @param yString string associated with the y axis
	 * @param yMin minimum value of the y axis
	 * @param yMax maximum value of the y axis
	 * @param yGap numerical gap between each y value on the axis
	 */
	public BarChart(List<XYValue> xYValues, String xString, String yString, int yMin, int yMax, int yGap) {
		if (yMin > yMax)
			throw new IllegalArgumentException("It must be yMin <= yMax. It was: yMin > yMax");
		if (yGap > yMax - yMin)
			throw new IllegalArgumentException("yGap must be grater than yMax - yMin");
		
		Objects.requireNonNull(xYValues, "xYValues must not be null");
		Objects.requireNonNull(xString, "xString must not be null");
		Objects.requireNonNull(yString, "yString must not be null");

		XYValues = xYValues;
		this.xString = xString;
		this.yString = yString;
		this.yMin = yMin;
		this.yMax = yMax;
		this.yGap = yGap;
	}

	/**
	 * Returns a list of x coordinates and heights of the bars.
	 * 
	 * @return the xYValues
	 */
	public List<XYValue> getXYValues() {
		return XYValues;
	}

	/**
	 * Returns string associated with the x axis
	 * 
	 * @return the xString
	 */
	public String getxString() {
		return xString;
	}

	/**
	 * Returns string associated with the y axis
	 * 
	 * @return the yString
	 */
	public String getyString() {
		return yString;
	}

	/**
	 * Returns minimum value of the y axis
	 * 
	 * @return the yMin
	 */
	public int getyMin() {
		return yMin;
	}

	/**
	 * Returns maximum value of the y axis
	 * 
	 * @return the yMax
	 */
	public int getyMax() {
		return yMax;
	}

	/**
	 * Returns numerical gap between each y value on the axis
	 * 
	 * @return the yGap
	 */
	public int getyGap() {
		return yGap;
	}
	
	

}
