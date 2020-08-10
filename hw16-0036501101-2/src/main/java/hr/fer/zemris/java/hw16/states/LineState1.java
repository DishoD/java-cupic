package hr.fer.zemris.java.hw16.states;

import java.awt.event.MouseEvent;

import hr.fer.zemris.java.hw16.jvdraw.JVDraw;
import hr.fer.zemris.java.hw16.jvdraw.geometricalobjects.Line;

/**
 * This state should be set when the line tool is selected.
 * Waits for the user to click on canvas to start drawing the line.
 * 
 * @author Disho
 *
 */
public class LineState1 extends AbstractState{
	
	/**
	 * Initializes the state with the given context.
	 * 
	 * @param context state context
	 */
	public LineState1(JVDraw context) {
		super(context);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		Line line = new Line(e.getPoint(), e.getPoint(), context.getForegroundColor());
		context.setToolState(new LineState2(context, line));
	}

}
