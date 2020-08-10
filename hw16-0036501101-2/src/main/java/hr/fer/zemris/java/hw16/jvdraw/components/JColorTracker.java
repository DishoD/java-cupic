package hr.fer.zemris.java.hw16.jvdraw.components;

import java.awt.Color;

import javax.swing.JLabel;

import hr.fer.zemris.java.hw16.jvdraw.interfaces.ColorChangeListener;
import hr.fer.zemris.java.hw16.jvdraw.interfaces.IColorProvider;

/**
 * A simple label that shows and track the currently selected background
 * and foreground color of the JVDraw application.
 * 
 * @author Disho
 *
 */
@SuppressWarnings("serial")
public class JColorTracker extends JLabel {
	/**
	 * foreground red
	 */
	private int fr;
	/**
	 * foreground green
	 */
	private int fg;
	/**
	 * foreground blue
	 */
	private int fb;
	/**
	 * background red
	 */
	private int br;
	/**
	 * background green
	 */
	private int bg;
	/**
	 * background blue
	 */
	private int bb;
	
	/**
	 * Initializes the label with the given parameters.
	 * 
	 * @param foreground foreground color
	 * @param background background color
	 */
	public JColorTracker(IColorProvider foreground, IColorProvider background) {
		fr = foreground.getCurrentColor().getRed();
		fg = foreground.getCurrentColor().getGreen();
		fb = foreground.getCurrentColor().getBlue();
		br = background.getCurrentColor().getRed();
		bg = background.getCurrentColor().getGreen();
		bb = background.getCurrentColor().getBlue();
		refreshText();
		
		foreground.addColorChangeListener(new ColorChangeListener() {
			@Override
			public void newColorSelected(IColorProvider source, Color oldColor, Color newColor) {
				fr = newColor.getRed();
				fg = newColor.getGreen();
				fb = newColor.getBlue();
				refreshText();
			}
		});
		
		background.addColorChangeListener(new ColorChangeListener() {
			@Override
			public void newColorSelected(IColorProvider source, Color oldColor, Color newColor) {
				br = newColor.getRed();
				bg = newColor.getGreen();
				bb = newColor.getBlue();
				refreshText();
			}
		});
	}
	
	/**
	 * Updates the label's text for the current color information.
	 */
	private void refreshText() {
		setText(String.format("Foreground color: (%d, %d, %d), background color: (%d, %d, %d).", fr, fg, fb, br, bg, bb));
	}
}
