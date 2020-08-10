package hr.fer.zemris.java.hw16.jvdraw.geometricalobjects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

import hr.fer.zemris.java.hw16.jvdraw.geometricalobjects.editors.FilledCircleEditor;
import hr.fer.zemris.java.hw16.jvdraw.geometricalobjects.editors.GeometricalObjectEditor;

/**
 * Geometrical object - filled circle.
 * Defined with center point, radius, outline color and fill color.
 * 
 * @author Disho
 *
 */
public class FilledCircle extends Circle {
	/**
	 * fill color
	 */
	private Color fillColor;
	
	/**
	 * Initializes the filled circle with the given parameters
	 * @param center center point
	 * @param radius circle radius
	 * @param outlineColor outline color
	 * @param fillColor fill color
	 */
	public FilledCircle(Point center, int radius, Color outlineColor, Color fillColor) {
		super(center, radius, outlineColor);
		this.fillColor = fillColor;
	}
	
	@Override
	public void accept(GeometricalObjectVisitor v) {
		v.visit((FilledCircle)this);
	}

	@Override
	public void paint(Graphics2D g) {
		g.setColor(fillColor);
		g.fillOval(center.x - radius, center.y - radius, 2*radius, 2*radius);
		
		g.setColor(outlineColor);
		g.drawOval(center.x - radius, center.y - radius, 2*radius, 2*radius);
	}

	/**
	 * Get fill color
	 * @return fill color
	 */
	public Color getFillColor() {
		return fillColor;
	}

	/**
	 * Set fill color
	 * @param fillColor fill color
	 */
	public void setFillColor(Color fillColor) {
		this.fillColor = fillColor;
		notifyListeners();
	}
	
	@Override
	public String toString() {
		return String.format("Filled Circle (%d,%d), %d, #%02X%02X%02X", 
								center.x, center.y, radius, fillColor.getRed(), fillColor.getGreen(), fillColor.getBlue()
		);
	}

	@Override
	public GeometricalObjectEditor createGeometricalObjectEditor() {
		return new FilledCircleEditor(this);
	}

}
