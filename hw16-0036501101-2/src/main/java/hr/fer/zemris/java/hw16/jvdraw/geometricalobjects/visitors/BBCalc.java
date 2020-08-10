package hr.fer.zemris.java.hw16.jvdraw.geometricalobjects.visitors;

import java.awt.Rectangle;

import hr.fer.zemris.java.hw16.jvdraw.geometricalobjects.Circle;
import hr.fer.zemris.java.hw16.jvdraw.geometricalobjects.FilledCircle;
import hr.fer.zemris.java.hw16.jvdraw.geometricalobjects.GeometricalObjectVisitor;
import hr.fer.zemris.java.hw16.jvdraw.geometricalobjects.Line;
import hr.fer.zemris.java.hw16.jvdraw.geometricalobjects.Polygon;

/**
 * Geometrical object visitor that calculates the bounding box
 * of the geometrical objects.
 * 
 * @author Disho
 *
 */
public class BBCalc implements GeometricalObjectVisitor {
	/**
	 * most left x value
	 */
	private Integer left; 
	/**
	 * most right x value
	 */
	private Integer right; 
	/**
	 * most top y value
	 */
	private Integer top;
	/**
	 * most bottom y value
	 */
	private Integer bottom;

	@Override
	public void visit(Line line) {
		int l, r, t, b;
		if(line.getStart().x < line.getEnd().x) {
			l = line.getStart().x;
			r = line.getEnd().x;
		} else {
			r = line.getStart().x;
			l = line.getEnd().x;
		}
		
		if(line.getStart().y < line.getEnd().y) {
			t = line.getStart().y;
			b = line.getEnd().y;
		} else {
			b = line.getStart().y;
			t = line.getEnd().y;
		}
		
		compareAndSet(l, r, t, b);
	}


	@Override
	public void visit(Circle circle) {
		int l = circle.getCenter().x - circle.getRadius();
		int r = circle.getCenter().x + circle.getRadius();
		int t = circle.getCenter().y - circle.getRadius();
		int b = circle.getCenter().y + circle.getRadius();
		
		compareAndSet(l, r, t, b);
	}

	@Override
	public void visit(FilledCircle filledCircle) {
		visit((Circle)filledCircle);
	}
	
	private void compareAndSet(int left, int right, int top, int bottom) {
		if(this.left == null) {
			this.left = left;
			this.right = right;
			this.top = top;
			this.bottom = bottom;
			return;
		}
		
		if(left < this.left) {
			this.left = left;
		}
		if(right > this.right) {
			this.right = right;
		}
		if(top < this.top) {
			this.top = top;
		}
		if(bottom > this.bottom) {
			this.bottom = bottom;
		}
	}
	
	/**
	 * Returns the bounding box of the visited geometrical objects.
	 * 
	 * @return bounding box
	 */
	public Rectangle getBoundingBox() {
		return new Rectangle(left, top, right - left, bottom - top);
	}


	@Override
	public void visit(Polygon polygon) {
		compareAndSet(polygon.getMinX(), polygon.getMaxX(), polygon.getMinY(), polygon.getMaxY());
	}

}
