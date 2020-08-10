package hr.fer.zemris.java.hw16.jvdraw.geometricalobjects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

import hr.fer.zemris.java.hw16.jvdraw.geometricalobjects.editors.GeometricalObjectEditor;
import hr.fer.zemris.java.hw16.jvdraw.geometricalobjects.editors.LineEditor;

/**
 * Geometrical object - line.
 * It has start and end point, and color.
 * 
 * @author Disho
 *
 */
public class Line extends GeometricalObject {
	/**
	 * start point
	 */
	private Point start;
	/**
	 * end point
	 */
	private Point end;
	/**
	 * line color
	 */
	private Color color;
	
	/**
	 * Initializes the line with the given parameters.
	 * 
	 * @param start start point
	 * @param end end point
	 * @param color line color
	 */
	public Line(Point start, Point end, Color color) {
		this.start = start;
		this.end = end;
		this.color = color;
	}

	/**
	 * Get start point
	 * @return start point
	 */
	public Point getStart() {
		return start;
	}



	/**
	 * Set start point
	 * @param start start point
	 */
	public void setStart(Point start) {
		this.start = start;
		notifyListeners();
	}



	/**
	 * Get end point
	 * @return end point
	 */
	public Point getEnd() {
		return end;
	}



	/**
	 * Set end point
	 * @param end end point
	 */
	public void setEnd(Point end) {
		this.end = end;
		notifyListeners();
	}



	/**
	 * Get line color
	 * @return line color
	 */
	public Color getColor() {
		return color;
	}



	/**
	 * Set line color
	 * @param color line color
	 */
	public void setColor(Color color) {
		this.color = color;
		notifyListeners();
	}

	@Override
	public void accept(GeometricalObjectVisitor v) {
		v.visit(this);
	}

	@Override
	public void paint(Graphics2D g) {
		g.setColor(color);
		g.drawLine(start.x, start.y, end.x, end.y);
	}
	
	@Override
	public String toString() {
		return String.format("Line (%d,%d)-(%d,%d)", start.x, start.y, end.x, end.y);
	}

	@Override
	public GeometricalObjectEditor createGeometricalObjectEditor() {
		return new LineEditor(this);
	}

}
