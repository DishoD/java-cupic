package hr.fer.zemris.java.hw16.jvdraw.geometricalobjects.editors;

import java.awt.GridLayout;
import java.awt.Point;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;

import hr.fer.zemris.java.hw16.jvdraw.components.JColorArea;
import hr.fer.zemris.java.hw16.jvdraw.geometricalobjects.Polygon;

public class PolygonEditor extends GeometricalObjectEditor{
	private Polygon p;
	private JSpinner[] xs;
	private JSpinner[] ys;
	private JColorArea outlineColor;
	private JColorArea fillColor;
	
	public PolygonEditor(Polygon p) {
		super();
		this.p = p;
		
		xs = new JSpinner[p.getNumberOfPoints()];
		ys = new JSpinner[p.getNumberOfPoints()];
		
		setLayout(new GridLayout(0, 2));
		
		for(int i = 0; i < p.getNumberOfPoints(); ++i) {
			xs[i] = Util.createJSpinner(p.getPoint(i).x);
			ys[i] = Util.createJSpinner(p.getPoint(i).y);
			
			add(new JLabel("Center point - x"+i+":"));
			add(xs[i]);
			
			add(new JLabel("Center point - y"+i+":"));
			add(ys[i]);
		}
		outlineColor = new JColorArea(p.getOutlineColor());
		fillColor = new JColorArea(p.getFillColor());
		
		add(new JLabel("Outline color:"));
		add(outlineColor);
		
		add(new JLabel("Fill color:"));
		add(fillColor);
	}

	@Override
	public void checkEditing() {
		Polygon testP = new Polygon(fillColor.getCurrentColor(), outlineColor.getCurrentColor());
		for(int i = 0; i < p.getNumberOfPoints(); ++i) {
			testP.addPoint(new Point((int)xs[i].getValue(), (int)ys[i].getValue()));
		}
		
		if(!testP.isConvex()) {
			throw new RuntimeException("Polygon is not convex");
		}
		
		int l = testP.getNumberOfPoints();
		for(int i = 0; i < l; ++i) {
			if(testP.getPoint(i).distance(testP.getPoint((i+1)%l)) < 3) throw new RuntimeException("Dvije tocke zaredom su preblizo.");
		}
		
	}

	@Override
	public void acceptEditing() {
		p.setFillColor(fillColor.getCurrentColor());
		p.setOutlineColor(outlineColor.getCurrentColor());
		
		for(int i = 0; i < p.getNumberOfPoints(); ++i) {
			p.setPoint(i, new Point((int)xs[i].getValue(), (int)ys[i].getValue()));
		}
	}

}
