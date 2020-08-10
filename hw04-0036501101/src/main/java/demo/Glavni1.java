package demo;

import hr.fer.zemris.lsystems.LSystem;
import hr.fer.zemris.lsystems.LSystemBuilderProvider;
import hr.fer.zemris.lsystems.gui.LSystemViewer;
import hr.fer.zemris.lsystems.impl.LSystemBuilderImpl;

/**
 * Demonstrates the LSystemBuilderImpl functionality with
 * the Koch curve fractal.
 * 
 * @author Hrvoje Ditrih
 *
 */
public class Glavni1 {

	/**
	 * Runs the demonstration.
	 * 
	 * @param args ignorable
	 */
	public static void main(String[] args) {
		LSystemViewer.showLSystem(createKochCurve(LSystemBuilderImpl::new));
	}
	
	/**
	 * Returns L-system Koch curve fractal that can be drawn on the screen.
	 * 
	 * @param provider LSystemBuilderProvider
	 * @return L-system fractal
	 */
	private static LSystem createKochCurve(LSystemBuilderProvider provider) {
		return provider.createLSystemBuilder()
						.registerCommand('F', "draw 1")
						.registerCommand('+', "rotate 60")
						.registerCommand('-', "rotate -60")
						.setOrigin(0.05, 0.4)
						.setAngle(0)
						.setUnitLength(0.9)
						.setUnitLengthDegreeScaler(1.0/3.0)
						.registerProduction('F', "F+F--F+F")
						.setAxiom("F")
						.build();
	}


}
