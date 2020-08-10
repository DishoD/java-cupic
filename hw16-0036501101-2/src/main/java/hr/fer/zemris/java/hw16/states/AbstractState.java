package hr.fer.zemris.java.hw16.states;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

import hr.fer.zemris.java.hw16.jvdraw.JVDraw;

/**
 * Represens an abstract tool state.
 * 
 * @author Disho
 *
 */
public class AbstractState implements Tool {
	/**
	 * state context
	 */
	protected JVDraw context;

	/**
	 * Initializes the state with the given context
	 * 
	 * @param context state context
	 */
	protected AbstractState(JVDraw context) {
		this.context = context;
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void paint(Graphics2D g2d) {
	}

}
