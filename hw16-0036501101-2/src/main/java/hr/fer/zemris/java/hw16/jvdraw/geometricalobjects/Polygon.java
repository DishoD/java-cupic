package hr.fer.zemris.java.hw16.jvdraw.geometricalobjects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw16.jvdraw.Vector3;
import hr.fer.zemris.java.hw16.jvdraw.geometricalobjects.editors.GeometricalObjectEditor;
import hr.fer.zemris.java.hw16.jvdraw.geometricalobjects.editors.PolygonEditor;

public class Polygon extends GeometricalObject {
	private final double THRESHOLD = 1e-3;
	private List<Point> points = new ArrayList<>();
	
	private Color fillColor;
	private Color outlineColor;
	
	private int minX;
	private int maxX;
	private int minY;
	private int maxY;
	

	public Polygon(Color fillColor, Color outlineColor) {
		super();
		this.fillColor = fillColor;
		this.outlineColor = outlineColor;
	}

	@Override
	public void accept(GeometricalObjectVisitor v) {
		v.visit(this);
	}

	@Override
	public void paint(Graphics2D g) {
		g.setColor(fillColor);
		g.fillPolygon(getXPoints(), getYPoints(), getNumberOfPoints());
		
		g.setColor(outlineColor);
		g.drawPolygon(getXPoints(), getYPoints(), getNumberOfPoints());
	}

	@Override
	public GeometricalObjectEditor createGeometricalObjectEditor() {
		return new PolygonEditor(this);
	}
	
	public void addPoint(Point p) {
		points.add(p);
		
		if(points.size() == 1) {
			minX = p.x;
			maxX = p.x;
			minY = p.y;
			maxY = p.y;
		} else {
			if(p.x < minX) minX = p.x;
			if(p.y < minY) minY = p.y;
			if(p.x > maxX) maxX = p.x;
			if(p.y > maxY) maxY = p.y;
		}
	}
	
	public int[] getXPoints() {
		int[] x = new int[points.size()];
		for(int i = 0; i < points.size(); ++i) {
			x[i] = points.get(i).x;
		}
		return x;
	}
	
	public int[] getYPoints() {
		int[] y = new int[points.size()];
		for(int i = 0; i < points.size(); ++i) {
			y[i] = points.get(i).y;
		}
		return y;
	}
	
	public List<Point> getPoints() {
		return points;
	}
	
	public int getNumberOfPoints() {
		return points.size();
	}
	
	public void setPoint(int i, Point p) {
		points.get(i).setLocation(p);
		this.notifyListeners();
	}
	
	public void setLastPoint(Point p) {
		points.get(getNumberOfPoints()-1).x = p.x;
		points.get(getNumberOfPoints()-1).y = p.y;
		this.notifyListeners();
	}
	
	public Point getLastPoint() {
		return points.get(getNumberOfPoints()-1);
	}
	public Point getLastPoint2() {
		return points.get(getNumberOfPoints()-2);
	}
	
	public void removeLastPoint() {
		points.remove(getNumberOfPoints()-1);
		this.notifyListeners();
	}
	
	public boolean isConvex() {
		List<Vector3> vecs = new ArrayList<>();
		int l = points.size();
		
		if(l < 4) return true;
		
		for(int i = 0; i < l; ++i) {
			vecs.add(new Vector3(points.get(i).x, points.get(i).y, 0));
		}
		
		Boolean z = null;
		for(int i = 0; i < l; ++i) {
			Vector3 r1 = vecs.get((i+1)%l).sub(vecs.get(i));
			Vector3 r2 = vecs.get((i+2)%l).sub(vecs.get(i));
			
			double temp = r1.cross(r2).getZ();
			
			if(z == null && temp > THRESHOLD) {
				z = temp > 0 ? true : false;
				continue;
			};
			
			if(z != null && temp > THRESHOLD) {
				boolean zz = temp > 0 ? true : false;
				if(zz != z) return false;
			}
		}
		
		return true;
	}
	
	public Color getFillColor() {
		return fillColor;
	}
	
	
	public Color getOutlineColor() {
		return outlineColor;
	}

	/**
	 * @return the minX
	 */
	public int getMinX() {
		return minX;
	}

	/**
	 * @param minX the minX to set
	 */
	public void setMinX(int minX) {
		this.minX = minX;
	}

	/**
	 * @return the maxX
	 */
	public int getMaxX() {
		return maxX;
	}

	/**
	 * @param maxX the maxX to set
	 */
	public void setMaxX(int maxX) {
		this.maxX = maxX;
	}

	/**
	 * @return the minY
	 */
	public int getMinY() {
		return minY;
	}

	/**
	 * @param minY the minY to set
	 */
	public void setMinY(int minY) {
		this.minY = minY;
	}

	/**
	 * @return the maxY
	 */
	public int getMaxY() {
		return maxY;
	}

	/**
	 * @param maxY the maxY to set
	 */
	public void setMaxY(int maxY) {
		this.maxY = maxY;
	}
	
	public Point getPoint(int i) {
		return points.get(i);
	}
	
	@Override
	public String toString() {
		return String.format("Polygon %d", points.size());
	}

	/**
	 * @param fillColor the fillColor to set
	 */
	public void setFillColor(Color fillColor) {
		this.fillColor = fillColor;
		this.notifyListeners();
	}

	/**
	 * @param outlineColor the outlineColor to set
	 */
	public void setOutlineColor(Color outlineColor) {
		this.outlineColor = outlineColor;
		this.notifyListeners();
	}
	
	
}
