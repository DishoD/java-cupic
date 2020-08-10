package hr.fer.zemris.java.hw16.jvdraw.interfaces;

import java.awt.Color;

/**
 * Defines an color provider.
 * 
 * @author Disho
 *
 */
public interface IColorProvider {
	/**
	 * Get current color.
	 * 
	 * @return current color
	 */
	public Color getCurrentColor();

	/**
	 * Register listener.
	 * 
	 * @param l listener to register
	 */
	public void addColorChangeListener(ColorChangeListener l);

	/**
	 * Unregister an listener.
	 * 
	 * @param l listener to unregister
	 */
	public void removeColorChangeListener(ColorChangeListener l);
}
