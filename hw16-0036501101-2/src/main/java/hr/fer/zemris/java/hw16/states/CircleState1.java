package hr.fer.zemris.java.hw16.states;

import java.awt.event.MouseEvent;

import hr.fer.zemris.java.hw16.jvdraw.JVDraw;
import hr.fer.zemris.java.hw16.jvdraw.geometricalobjects.Circle;

/**
 * This state should be set when the circle tool is selected.
 * Waits for the user to click on canvas to start drawing the circle.
 * 
 * @author Disho
 *
 */
public class CircleState1 extends AbstractState {

	/**
	 * Initializes the state with the given context.
	 * 
	 * @param context state context
	 */
	public CircleState1(JVDraw context) {
		super(context);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		Circle c = new Circle(e.getPoint(), 0, context.getForegroundColor());
		context.setToolState(new CircleState2(context, c));
	}
}
