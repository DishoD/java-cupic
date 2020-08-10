package hr.fer.zemris.lsystems.impl;

import java.awt.Color;

import hr.fer.zemris.math.Vector2D;

/**
 * Represents one state of the turtle for the turtle graphics. It holds turtle
 * information: position, direction, color and length that turtle can move per
 * one unit length.
 * 
 * @author Hrvoje Ditrih
 *
 */
public class TurtleState {
	/**
	 * Current position of the turtle.
	 */
	public Vector2D position;

	/**
	 * The direction that turtle is facing in degrees. (-180, 180]. 0 degrees is
	 * facing to the right, 90 degrees is facing upward, etc.
	 */
	public Vector2D direction;

	/**
	 * If the turtle draws a line, it will draw it with this color.
	 */
	public Color color;

	/**
	 * Represents a unit length that turtle will shift in one draw or skip call.
	 */
	public double shiftLength;

	/**
	 * Initializes an turtle wit the given parameters.
	 * 
	 * @param position
	 *            position vector of the turtle; x = [0, 1.0], y = [0, 1.0], where
	 *            (0,0) is bottom left point on the screen
	 * @param direction
	 *            a unit vector representing the angle the turtle is facing. 0
	 *            degrees is facing to the right, 90 degrees is facing upwards, etc.
	 * @param color
	 *            If the turtle draws a line, it will draw it with this color.
	 * @param shiftLength
	 *            Represents a unit length that turtle will shift in one draw or
	 *            skip call.
	 */
	public TurtleState(Vector2D position, Vector2D direction, Color color, double shiftLength) {
		this.position = position;
		this.direction = direction;
		this.color = color;
		this.shiftLength = shiftLength;
	}

	/**
	 * Current position of the turtle.
	 * 
	 * @return position vector
	 */
	public Vector2D getPosition() {
		return position;
	}

	/**
	 * Sets current position of the turtle.
	 * 
	 * @param position
	 *            Position vector to set
	 */
	public void setPosition(Vector2D position) {
		this.position = position;
	}

	/**
	 * Current direction that the turtle is facing.
	 * 
	 * @return a unit vector representing the angle the turtle is facing. 0 degrees
	 *         is facing to the right, 90 degrees is facing upwards, etc.
	 */
	public Vector2D getDirection() {
		return direction;
	}

	/**
	 * Set the direction that the turtle is facing.
	 * 
	 * @param direction
	 *            a unit vector representing the angle the turtle is facing. 0
	 *            degrees is facing to the right, 90 degrees is facing upwards, etc.
	 */
	public void setDirection(Vector2D direction) {
		this.direction = direction;
	}

	/**
	 * Current color of the turtle.
	 * 
	 * @return current color
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Sets current color of the turtle.
	 * 
	 * @param color
	 *            color of the turtle to be set
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * Unit length of the turtle.
	 * 
	 * @return unit length that turtle will shift in one draw or skip call.
	 */
	public double getShiftLength() {
		return shiftLength;
	}

	/**
	 * Set unit length of the turtle.
	 * 
	 * @param shiftLength
	 *            unit length that turtle will shift in one draw or skip call.
	 */
	public void setShiftLength(double shiftLength) {
		this.shiftLength = shiftLength;
	}

	/**
	 * Constructs a new TurtleState object that is a copy of the current turtle
	 * state.
	 * 
	 * @return a copy of the turtle state
	 */
	public TurtleState copy() {
		return new TurtleState(position.copy(), direction.copy(),
				new Color(color.getRed(), color.getGreen(), color.getBlue()), shiftLength);
	}

}
