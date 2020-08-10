package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;

/**
 * Defines an L-system fractal commands which use turtle based graphics. This
 * command duplicates (creates new object) the last turtle state from the top of
 * the L-system context stack and pushes it on to the stack again.
 * 
 * @author Hrvoje Ditrih
 *
 */
public class PushCommand implements Command {

	@Override
	public void execute(Context ctx, Painter painter) {
		ctx.pushState(ctx.getCurrentState().copy());
	}

}
