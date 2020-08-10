package hr.fer.zemris.java.hw16.states;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

/**
 * Represents a tool for drawing in the JVDraw application.
 * 
 * @author Disho
 *
 */
public interface Tool {
	/**
	 * Some action that tool does when mouse is pressed.
	 * 
	 * @param e mouse event
	 */
	public void mousePressed(MouseEvent e);
	
	/**
	 * Some action that tool does when mouse is released.
	 * 
	 * @param e mouse event
	 */
	public void mouseReleased(MouseEvent e);
	
	/**
	 * Some action that tool does when mouse is clicked.
	 * 
	 * @param e mouse event
	 */
	public void mouseClicked(MouseEvent e);
	
	/**
	 * Some action that tool does when mouse is moved.
	 * 
	 * @param e mouse event
	 */
	public void mouseMoved(MouseEvent e);

	/**
	 * Some action that tool does when mouse is dragged.
	 * 
	 * @param e mouse event
	 */
	public void mouseDragged(MouseEvent e);

	/**
	 * Canvas should call this method at the end to draw
	 * something that the tool wants to draw.
	 * 
	 * @param g2d
	 */
	public void paint(Graphics2D g2d);
}