package hr.fer.zemris.lsystems.impl;

import hr.fer.zemris.lsystems.Painter;

/**
 * Defines an L-system fractal commands which use turtle based graphics.
 * 
 * @author Hrvoje Ditrih
 *
 */
public interface Command {
	/**
	 * Execute the command for the given L-system context.
	 * 
	 * @param ctx
	 *            L-system TurtleState stack context
	 * @param painter
	 *            if this is a drawing command, it will be drawn to this painter
	 */
	void execute(Context ctx, Painter painter);
}
