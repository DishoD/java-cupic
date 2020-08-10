package hr.fer.zemris.java.hw16.jvdraw.geometricalobjects.editors;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;

import javax.swing.JLabel;
import javax.swing.JSpinner;

import hr.fer.zemris.java.hw16.jvdraw.components.JColorArea;
import hr.fer.zemris.java.hw16.jvdraw.geometricalobjects.Circle;

/**
 * Geometrical object editor for the circles.
 * 
 * @author Disho
 *
 */
@SuppressWarnings("serial")
public class CircleEditor extends GeometricalObjectEditor {
	private Circle circle;
	private JSpinner centerX;
	private JSpinner centerY;
	private JSpinner radius;
	private JColorArea outlineColor;

	/**
	 * Initializes the editor with the given circle.
	 * 
	 * @param circle circle to edit
	 */
	public CircleEditor(Circle circle) {
		this.circle = circle;
		
		centerX = Util.createJSpinner(circle.getCenter().x);
		centerY = Util.createJSpinner(circle.getCenter().y);
		radius = Util.createJSpinner(circle.getRadius());
		outlineColor = new JColorArea(circle.getOutlineColor());
		
		setLayout(new GridLayout(0, 2));
		add(new JLabel("Center point - x:"));
		add(centerX);
		
		add(new JLabel("Center point - y:"));
		add(centerY);
		
		add(new JLabel("Radius:"));
		add(radius);
		
		add(new JLabel("Color:"));
		add(outlineColor);
		setPreferredSize(new Dimension(100, 100));
	}

	@Override
	public void checkEditing() {
		Util.checkSpinner(centerX, "Center point - x");
		Util.checkSpinner(centerY, "Center point - y");
		Util.checkSpinner(radius, "Radius");
	}

	@Override
	public void acceptEditing() {
		circle.setCenter(new Point((int)centerX.getValue(), (int)centerY.getValue()));
		circle.setRadius((int)radius.getValue());
		circle.setOutlineColor(outlineColor.getCurrentColor());
	}

}
