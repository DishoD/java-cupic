package hr.fer.zemris.java.hw16.states;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;

import hr.fer.zemris.java.hw16.jvdraw.JVDraw;
import hr.fer.zemris.java.hw16.jvdraw.geometricalobjects.Polygon;

public class PolygonState2 extends AbstractState {
	private Polygon p;
	
	public PolygonState2(JVDraw context, Polygon p) {
		super(context);
		this.p = p;
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		p.setLastPoint(e.getPoint());
		context.getDrawingCanvas().repaint();
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getButton() == e.BUTTON3) {
			context.setToolState(new PolygonState1(context));
			context.getDrawingCanvas().repaint();
		}
		
		if(e.getButton() == e.BUTTON1) {
			if(e.getPoint().distance(p.getLastPoint2()) < 3) {
				if(p.getNumberOfPoints() > 3) {
					p.removeLastPoint();
					context.getDrawingModel().add(p);
					context.setToolState(new PolygonState1(context));
					return;
				}
				return;
			}
			
			if(!p.isConvex()) {
				JOptionPane.showMessageDialog(context, "Can't draw non convex polygons");
				return;
			}
			
			p.addPoint(e.getPoint());
		}
	}
	
	@Override
	public void paint(Graphics2D g2d) {
		p.paint(g2d);
	}
}
