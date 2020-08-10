package hr.fer.zemris.java.hw16.states;

import java.awt.event.MouseEvent;

import hr.fer.zemris.java.hw16.jvdraw.JVDraw;
import hr.fer.zemris.java.hw16.jvdraw.geometricalobjects.Polygon;

public class PolygonState1 extends AbstractState {

	public PolygonState1(JVDraw context) {
		super(context);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		Polygon p = new Polygon(context.getBackgroundColor(), context.getForegroundColor());
		p.addPoint(e.getPoint());
		p.addPoint(e.getPoint());
		context.setToolState(new PolygonState2(context, p));
	}
}
