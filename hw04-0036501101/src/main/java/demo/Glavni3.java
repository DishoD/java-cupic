package demo;

import hr.fer.zemris.lsystems.gui.LSystemViewer;
import hr.fer.zemris.lsystems.impl.LSystemBuilderImpl;

/**
 * Demonstrates the LSystemBuilderImpl functionality for any
 * L-system fractal that can be loaded from the file.
 * 
 * @author Hrvoje Ditrih
 *
 */
public class Glavni3 {

	/**
	 * Runs the demonstration program.
	 * 
	 * @param args ignorable
	 */
	public static void main(String[] args) {
		LSystemViewer.showLSystem(LSystemBuilderImpl::new);
	}

}
