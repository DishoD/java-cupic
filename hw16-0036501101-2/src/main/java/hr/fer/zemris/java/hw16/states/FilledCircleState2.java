package hr.fer.zemris.java.hw16.states;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

import hr.fer.zemris.java.hw16.jvdraw.JVDraw;
import hr.fer.zemris.java.hw16.jvdraw.geometricalobjects.FilledCircle;

/**
 * This state should be set when user has clicked on the canvas to
 * start drawing the filled circle. This state awaits the second user click to
 * finalize the filled circle and add it to the drawing model.
 * 
 * @author Disho
 *
 */
public class FilledCircleState2 extends AbstractState {
	/**
	 * filled circle that is being drawn
	 */
	private FilledCircle circle;
	
	/**
	 * Initializes the state with the given parameters.
	 * 
	 * @param context state context
	 * @param circle filled circle that is being drawn
	 */
	protected FilledCircleState2(JVDraw context, FilledCircle circle) {
		super(context);
		this.circle =  circle;
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		circle.setRadius((int) e.getPoint().distance(circle.getCenter()));
		context.getDrawingCanvas().repaint();
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		context.getDrawingModel().add(circle);
		context.setToolState(new FilledCircleState1(context));
	}
	
	@Override
	public void paint(Graphics2D g2d) {
		circle.paint(g2d);
	}
}
