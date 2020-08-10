package hr.fer.zemris.java.hw16.jvdraw.geometricalobjects.visitors;

import java.awt.Graphics2D;

import hr.fer.zemris.java.hw16.jvdraw.geometricalobjects.Circle;
import hr.fer.zemris.java.hw16.jvdraw.geometricalobjects.FilledCircle;
import hr.fer.zemris.java.hw16.jvdraw.geometricalobjects.GeometricalObjectVisitor;
import hr.fer.zemris.java.hw16.jvdraw.geometricalobjects.Line;
import hr.fer.zemris.java.hw16.jvdraw.geometricalobjects.Polygon;

/**
 * Geometrical object visitor that draws accepted objects to the
 * given Graphics objects.
 * 
 * @author Disho
 *
 */
public class ObjectPainter implements GeometricalObjectVisitor {
	/**
	 * graphics object to be painted on
	 */
	private Graphics2D g;

	/**
	 * Initializes the visitor with the given graphics object.
	 * 
	 * @param g graphics object to be painted on
	 */
	public ObjectPainter(Graphics2D g) {
		this.g = g;
	}

	@Override
	public void visit(Line line) {
		g.setColor(line.getColor());
		g.drawLine(line.getStart().x, line.getStart().y, line.getEnd().x, line.getEnd().y);
	}

	@Override
	public void visit(Circle circle) {
		g.setColor(circle.getOutlineColor());
		g.drawOval(
				circle.getCenter().x - circle.getRadius(), 
				circle.getCenter().y - circle.getRadius(), 
				2*circle.getRadius(), 
				2*circle.getRadius()
	);
	}

	@Override
	public void visit(FilledCircle filledCircle) {
		g.setColor(filledCircle.getFillColor());
		g.fillOval(
				filledCircle.getCenter().x - filledCircle.getRadius(), 
				filledCircle.getCenter().y - filledCircle.getRadius(),
				2*filledCircle.getRadius(), 
				2*filledCircle.getRadius()
		);
		
		g.setColor(filledCircle.getOutlineColor());
		g.drawOval(
				filledCircle.getCenter().x - filledCircle.getRadius(), 
				filledCircle.getCenter().y - filledCircle.getRadius(),
				2*filledCircle.getRadius(), 
				2*filledCircle.getRadius()
		);
	}

	@Override
	public void visit(Polygon polygon) {
		g.setColor(polygon.getFillColor());
		g.fillPolygon(polygon.getXPoints(), polygon.getYPoints(), polygon.getNumberOfPoints());
		
		g.setColor(polygon.getOutlineColor());
		g.drawPolygon(polygon.getXPoints(), polygon.getYPoints(), polygon.getNumberOfPoints());
	}

}
