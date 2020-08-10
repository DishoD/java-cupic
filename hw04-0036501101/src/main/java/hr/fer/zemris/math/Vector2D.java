package hr.fer.zemris.math;

/**
 * Represents a simple vector in Cartesian 2D space.
 * X and Y components are real numbers.
 * 
 * @author Hrvoje Ditrih
 *
 */
public class Vector2D {
	/**
	 * x component of a vector
	 */
	private double x;

	/**
	 * y component of a vector
	 */
	private double y;

	/**
	 * Threshold for comparing doubles
	 */
	private static final double EPS = 1E-6;

	/**
	 * Initializes an vector with the given parameters.
	 * 
	 * @param x
	 *            x coordinate of a vector, as decimal number
	 * @param y
	 *            y coordinate of a vector, as decimal number
	 */
	public Vector2D(double x, double y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * X component of a vector
	 * 
	 * @return x coordinate
	 */
	public double getX() {
		return x;
	}

	/**
	 * Y component of a vector
	 * 
	 * @return y coordinate
	 */
	public double getY() {
		return y;
	}

	/**
	 * Translates the current vector by the given vector.
	 * 
	 * @param offset
	 *            vector to translate by
	 */
	public void translate(Vector2D offset) {
		this.x += offset.x;
		this.y += offset.y;
	}

	/**
	 * Constructs a new vector that is a current vector translated by the given
	 * vector.
	 * 
	 * @param offset
	 *            vector to translate by
	 * @return a new translated vector
	 */
	public Vector2D translated(Vector2D offset) {
		return new Vector2D(this.x + offset.x, this.y + offset.y);
	}

	/**
	 * Rotates the current vector by the given angle.
	 * 
	 * @param angle
	 *            to rotate by (in degrees)
	 */
	public void rotate(double angle) {
		angle = Math.toRadians(angle);

		double newX = x * Math.cos(angle) - y * Math.sin(angle);
		y = x * Math.sin(angle) + y * Math.cos(angle);
		x = newX;
	}

	/**
	 * Constructs a new vector that is a current vector rotated by the given angle.
	 * 
	 * @param angle
	 *            angle to rotate by (in degrees)
	 * @return a new rotated vector
	 */
	public Vector2D rotated(double angle) {
		angle = Math.toRadians(angle);

		return new Vector2D(x * Math.cos(angle) - y * Math.sin(angle), x * Math.sin(angle) + y * Math.cos(angle));
	}

	/**
	 * Scales an current vector by the given scaler.
	 * 
	 * @param scaler
	 *            value to scale the vector by
	 */
	public void scale(double scaler) {
		x *= scaler;
		y *= scaler;
	}

	/**
	 * Constructs a new vector that is a current vector scaled by the given scaler.
	 * 
	 * @param scaler
	 *            value to scale the vector by
	 * @return a new scaled vector
	 */
	public Vector2D scaled(double scaler) {
		return new Vector2D(x * scaler, y * scaler);
	}

	/**
	 * Returns a new vector that is a copy of the current vector. Allocates a new
	 * object.
	 * 
	 * @return a new vector that is a copy of the current vector
	 */
	public Vector2D copy() {
		return new Vector2D(this.x, this.y);
	}
	
	/**
	 * Constructs a new unit vector of the given angle.
	 * 
	 * @param angle angle of the unit vector, in degrees
	 * @return a new unit vector
	 */
	public static Vector2D unitVector(double angle) {
		angle = Math.toRadians(angle);
		double x = Math.cos(angle);
		double y = Math.sin(angle);
		
		return new Vector2D(x, y);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Vector2D))
			return false;

		Vector2D other = (Vector2D) obj;

		return Math.abs(this.x - other.x) < EPS && Math.abs(this.y - other.y) < EPS;
	}

	@Override
	public String toString() {
		return String.format("(%f,%f)", x, y);
	}

}
