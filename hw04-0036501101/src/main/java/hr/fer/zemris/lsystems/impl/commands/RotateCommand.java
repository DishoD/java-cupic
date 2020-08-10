package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;

/**
 * Defines an L-system fractal commands which use turtle based graphics. This
 * command rotates the direction angle of the turtle.
 * 
 * @author Hrvoje Ditrih
 *
 */
public class RotateCommand implements Command {
	/**
	 * Angle by which the turtle will be rotated. Positive values rotate
	 * counter-clockwise.
	 */
	private double angle;

	/**
	 * Initializes the rotate command by the given angle
	 * 
	 * @param angle
	 *            Angle(in degrees) by which the turtle will be rotated. Positive
	 *            values rotate counter-clockwise.
	 */
	public RotateCommand(double angle) {
		this.angle = angle;
	}

	@Override
	public void execute(Context ctx, Painter painter) {
		ctx.getCurrentState().getDirection().rotate(angle);
	}

}
