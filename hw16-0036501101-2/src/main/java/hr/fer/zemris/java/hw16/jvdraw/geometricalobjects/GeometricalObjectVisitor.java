package hr.fer.zemris.java.hw16.jvdraw.geometricalobjects;

/**
 * Geometrical object visitor.
 * 
 * @author Disho
 *
 */
public interface GeometricalObjectVisitor {
	/**
	 * Called when the line is visited.
	 * 
	 * @param line line object
	 */
	public void visit(Line line);
	
	/**
	 * Called when the circle is visited.
	 * 
	 * @param line circle object
	 */
	public void visit(Circle circle);
	
	/**
	 * Called when the filled circle is visited.
	 * 
	 * @param line filled circle object
	 */
	public void visit(FilledCircle filledCircle);
	
	public void visit(Polygon polygon);
}
