package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;

/**
 * Defines an L-system fractal commands which use turtle based graphics. This
 * command scales the unit length of the current turtle state.
 * 
 * @author Hrvoje Ditrih
 *
 */
public class ScaleCommand implements Command {
	/**
	 * Factor of scaling
	 */
	private double factor;

	/**
	 * Initializes the scale command with the given factor.
	 * 
	 * @param factor
	 *            Factor of scaling, must be positive
	 */
	public ScaleCommand(double factor) {
		if (factor < 0)
			throw new IllegalArgumentException("Factor must be >= 0. It was: " + factor);
		this.factor = factor;
	}

	@Override
	public void execute(Context ctx, Painter painter) {
		double currentLength = ctx.getCurrentState().getShiftLength();

		ctx.getCurrentState().setShiftLength(factor * currentLength);
	}

}
