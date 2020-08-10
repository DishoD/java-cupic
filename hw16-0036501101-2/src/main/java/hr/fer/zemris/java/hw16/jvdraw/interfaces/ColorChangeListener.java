package hr.fer.zemris.java.hw16.jvdraw.interfaces;

import java.awt.Color;

/**
 * Defines an listener for when the color of the ICOlorProvider changes.
 *  
 * @author Disho
 *
 */
public interface ColorChangeListener {
	/**
	 * This method is called when the color has changed.
	 * 
	 * @param source source that changed color
	 * @param oldColor old color
	 * @param newColor currently selected color
	 */
	public void newColorSelected(IColorProvider source, Color oldColor, Color newColor);
}