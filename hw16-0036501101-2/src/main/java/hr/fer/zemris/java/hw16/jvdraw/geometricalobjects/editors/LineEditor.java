package hr.fer.zemris.java.hw16.jvdraw.geometricalobjects.editors;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;

import javax.swing.JLabel;
import javax.swing.JSpinner;

import hr.fer.zemris.java.hw16.jvdraw.components.JColorArea;
import hr.fer.zemris.java.hw16.jvdraw.geometricalobjects.Line;

/**
 * Geometrical object editor for the lines.
 * 
 * @author Disho
 *
 */
@SuppressWarnings("serial")
public class LineEditor extends GeometricalObjectEditor {
	private Line line;
	private JSpinner startX;
	private JSpinner startY;
	private JSpinner endX;
	private JSpinner endY;
	private JColorArea color;
	
	/**
	 * Initializes the editor with the given line.
	 * 
	 * @param line line to edit
	 */
	public LineEditor(Line line) {
		this.line = line;
		
		startX = Util.createJSpinner(line.getStart().x);
		startY = Util.createJSpinner(line.getStart().y);
		endX = Util.createJSpinner(line.getEnd().x);
		endY = Util.createJSpinner(line.getEnd().y);
		color = new JColorArea(line.getColor());
		
		
		setLayout(new GridLayout(0, 2));
		add(new JLabel("Start point - x:"));
		add(startX);
		
		add(new JLabel("Start point - y:"));
		add(startY);
		
		add(new JLabel("End point - x:"));
		add(endX);
		
		add(new JLabel("End point - y:"));
		add(endY);
		
		add(new JLabel("Color:"));
		add(color);
		setPreferredSize(new Dimension(100, 100));
	}

	@Override
	public void checkEditing() {
		Util.checkSpinner(startX, "Start point - x");
		Util.checkSpinner(startY, "Start point - y");
		Util.checkSpinner(endX, "End point - x");
		Util.checkSpinner(endY, "End point - y");
	}

	@Override
	public void acceptEditing() {
		line.setStart(new Point((int)startX.getValue(), (int) startY.getValue()));
		line.setEnd(new Point((int)endX.getValue(), (int) endY.getValue()));
		line.setColor(color.getCurrentColor());
	}
	

}
