package hr.fer.zemris.java.hw16.states;

import java.awt.event.MouseEvent;

import hr.fer.zemris.java.hw16.jvdraw.JVDraw;
import hr.fer.zemris.java.hw16.jvdraw.geometricalobjects.FilledCircle;

/**
 * This state should be set when the filled circle tool is selected.
 * Waits for the user to click on canvas to start drawing the filled circle.
 * 
 * @author Disho
 *
 */
public class FilledCircleState1 extends AbstractState{
	
	/**
	 * Initializes the state with the given context.
	 * 
	 * @param context state context
	 */
	public FilledCircleState1(JVDraw context) {
		super(context);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		FilledCircle c = new FilledCircle(e.getPoint(), 0, context.getForegroundColor(), context.getBackgroundColor());
		context.setToolState(new FilledCircleState2(context, c));
	}

}
