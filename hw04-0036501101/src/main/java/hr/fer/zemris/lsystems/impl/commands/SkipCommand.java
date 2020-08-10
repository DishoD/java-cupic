package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;
import hr.fer.zemris.lsystems.impl.TurtleState;
import hr.fer.zemris.math.Vector2D;

/**
 * Defines an L-system fractal commands which use turtle based graphics. This
 * command moves the turtle on the line from the turtles position with its
 * effective unit length multiplied by the step value in the turtles direction.
 * 
 * @author Hrvoje Ditrih
 *
 */
public class SkipCommand implements Command {
	/**
	 * This value will multiply the turtles effective unit length.
	 */
	private double step;

	/**
	 * Initializes the skip command with the given step.
	 * 
	 * @param step
	 *            this value will multiply the turtles effective unit length
	 */
	public SkipCommand(double step) {
		this.step = step;
	}

	@Override
	public void execute(Context ctx, Painter painter) {
		TurtleState currentState = ctx.getCurrentState();

		double length = step * currentState.getShiftLength();
		Vector2D position = currentState.getPosition();
		Vector2D direction = currentState.getDirection();

		position.translate(direction.scaled(length));
	}

}
