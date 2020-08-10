package hr.fer.zemris.lsystems.impl.commands;

import java.awt.Color;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;

/**
 * Defines an L-system fractal commands which use turtle based graphics. This
 * command sets the color of the turtle.
 * 
 * @author Hrvoje Ditrih
 *
 */
public class ColorCommand implements Command {
	/**
	 * Turtle color.
	 */
	private Color color;

	/**
	 * Initializes the command with the given color.
	 * 
	 * @param color
	 *            turtle color
	 */
	public ColorCommand(Color color) {
		if(color == null)
			throw new IllegalArgumentException("Color can't be null");
		
		this.color = color;
	}

	@Override
	public void execute(Context ctx, Painter painter) {
		ctx.getCurrentState().setColor(color);
	}

}
