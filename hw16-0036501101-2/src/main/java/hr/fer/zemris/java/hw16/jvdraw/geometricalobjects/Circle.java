package hr.fer.zemris.java.hw16.jvdraw.geometricalobjects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

import hr.fer.zemris.java.hw16.jvdraw.geometricalobjects.editors.CircleEditor;
import hr.fer.zemris.java.hw16.jvdraw.geometricalobjects.editors.GeometricalObjectEditor;

/**
 * Geometrical object - Circle.
 * It has an center point, radius and outline color.
 * 
 * @author Disho
 *
 */
public class Circle extends GeometricalObject {
	/**
	 * center point of circle
	 */
	protected Point center;
	/**
	 * circle radius
	 */
	protected int radius;
	/**
	 * outline color
	 */
	protected Color outlineColor;

	/**
	 * Initializes the circle with the given parameters.
	 * 
	 * @param center center point of circle
	 * @param radius circle radius
	 * @param color outline color
	 */ 
	public Circle(Point center, int radius, Color color) {
		this.center = center;
		this.radius = radius;
		this.outlineColor = color;
	}

	@Override
	public void accept(GeometricalObjectVisitor v) {
		v.visit(this);
	}

	@Override
	public void paint(Graphics2D g) {
		g.setColor(outlineColor);
		g.drawOval(center.x - radius, center.y - radius, 2*radius, 2*radius);
	}

	/**
	 * @return the center point
	 */
	public Point getCenter() {
		return center;
	}

	/**
	 * Set center point of the circle
	 * @param center center point
	 */
	public void setCenter(Point center) {
		this.center = center;
		notifyListeners();
	}

	/**
	 * Circle radius
	 * @return radius
	 */
	public int getRadius() {
		return radius;
	}

	/**
	 * Set circle radius
	 * @param radius radius
	 */
	public void setRadius(int radius) {
		this.radius = radius;
		notifyListeners();
	}

	/**
	 * Get outline color
	 * @return outline color
	 */
	public Color getOutlineColor() {
		return outlineColor;
	}

	/**
	 * Set outline color
	 * @param color outline color
	 */
	public void setOutlineColor(Color color) {
		this.outlineColor = color;
		notifyListeners();
	}

	@Override
	public GeometricalObjectEditor createGeometricalObjectEditor() {
		return new CircleEditor(this);
	}
	
	@Override
	public String toString() {
		return String.format("Circle (%d,%d), %d", center.x, center.y, radius);
	}
}
