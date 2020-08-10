package hr.fer.zemris.java.gui.charts;

/**
 * Holds x and y integer values.
 * 
 * @author Disho
 *
 */
public class XYValue {
	/**
	 * x value
	 */
	public final int x;
	/**
	 * y value
	 */
	public final int y;
	
	/**
	 * Initializes the object with the given parameters.
	 * 
	 * @param x x value
	 * @param y y value
	 */
	public XYValue(int x, int y) {
		if(y < 0) throw new IllegalArgumentException("y value must be >= 0. Pair was: (" + x + "," + y +")");
		
		this.x = x;
		this.y = y;
	}
}
