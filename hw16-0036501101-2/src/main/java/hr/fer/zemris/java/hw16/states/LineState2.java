package hr.fer.zemris.java.hw16.states;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

import hr.fer.zemris.java.hw16.jvdraw.JVDraw;
import hr.fer.zemris.java.hw16.jvdraw.geometricalobjects.Line;

/**
 * This state should be set when user has clicked on the canvas to
 * start drawing the line. This state awaits the second user click to
 * finalize the line and add it to the drawing model.
 * 
 * @author Disho
 *
 */
public class LineState2 extends AbstractState{
	/**
	 * line that is being drawn
	 */
	private Line line;
	
	/**
	 * Initializes the state with the given parameters.
	 * 
	 * @param context state context
	 * @param line line that is being drawn
	 */
	public LineState2(JVDraw context, Line line) {
		super(context);
		this.line = line;
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		line.setEnd(e.getPoint());
		context.getDrawingCanvas().repaint();
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		context.getDrawingModel().add(line);
		context.setToolState(new LineState1(context));
	}
	
	@Override
	public void paint(Graphics2D g2d) {
		line.paint(g2d);
	}
}
