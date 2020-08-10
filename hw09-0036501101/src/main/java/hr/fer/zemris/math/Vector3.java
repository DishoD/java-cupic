package hr.fer.zemris.math;

/**
 * Represents a simple immutable vector in Cartesian 3D space.
 * 
 * @author Disho
 *
 */
public class Vector3 {
	/**
	 * x coordinate
	 */
	private double x;
	/**
	 * y coordinate
	 */
	private double y;
	/**
	 * z coordinate
	 */
	private double z;
	
	private static final double THRESHOLD = 1e-6;

	/**
	 * Initializes the vector with the given parameters.
	 * 
	 * @param x x coordinate
	 * @param y y coordinate
	 * @param z z coordinate
	 */
	public Vector3(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Calculates a norm of a vector.
	 * 
	 * @return norm of a vector
	 */
	public double norm() {
		return Math.sqrt(x*x + y*y + z*z);
	} 

	/**
	 * Constructs a normalized vector of this vector.
	 * 
	 * @return normalized vector
	 */
	public Vector3 normalized() {
		return scale(1/norm());
	} 

	/**
	 * Constructs a vector that is a addition of this vector and the given vector.
	 * 
	 * @param other vector to add
	 * @return new vector that is addition of this vector and the given vector
	 */
	public Vector3 add(Vector3 other) {
		return new Vector3(this.x + other.x, this.y + other.y, this.z + other.z);
	} 
	
	/**
	 * Constructs a vector that is a subtraction of this vector and the given vector.
	 * 
	 * @param other vector to subtract
	 * @return new vector that is subtraction of this vector and the given vector
	 */
	public Vector3 sub(Vector3 other) {
		return new Vector3(this.x - other.x, this.y - other.y, this.z - other.z);
	} 

	/**
	 * Calculates a vector dot product.
	 * 
	 * @param other dot product multiplier
	 * @return vector dot product
	 */
	public double dot(Vector3 other) {
		return this.x*other.x + this.y*other.y +this.z*other.z;
	}

	/**
	 * Constructs a vector that is a cross product of this vector and the given vector.
	 * 
	 * @param other cross product multiplier
	 * @return new vector that is a cross product of this vector and the given vector
	 */
	public Vector3 cross(Vector3 other) {
		return new Vector3(this.y*other.z - this.z*other.y, 
							this.z*other.x - this.x*other.z, 
							this.x*other.y - this.y*other.x);
	}

	/**
	 * Constructs a vector that is this vector scaled by the factor s.
	 * 
	 * @param s scaler
	 * @return vector that is this vector scaled by the factor s
	 */
	public Vector3 scale(double s) {
		return new Vector3(x*s, y*s, z*s);
	}

	/**
	 * Calculates a cos of the angle between this vector and the given vector.
	 * 
	 * @param other vector against which to get an angle
	 * @return cos of the angle between this vector and the given vector
	 */
	public double cosAngle(Vector3 other) {
		return dot(other)/(norm() * other.norm());
	} 

	/**
	 * Get x coordinate.
	 * 
	 * @return x coordinate
	 */
	public double getX() {
		return x;
	}
	
	/**
	 * Get y coordinate.
	 * 
	 * @return y coordinate
	 */
	public double getY() {
		return y;
	}
	
	/**
	 * Get z coordinate.
	 * 
	 * @return z coordinate
	 */
	public double getZ() {
		return z;
	}

	/**
	 * Returns an array of three elements
	 * representing this vectors coordinates [x, y, z].
	 * 
	 * @return array of vectors coordinates
	 */
	public double[] toArray() {
		return new double[] {x, y, z};
	} 
	
	@Override
	public String toString() {
		return String.format("(%f,%f,%f)", x, y, z);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Vector3)) return false;
		
		Vector3 other = (Vector3)obj;
		if(Math.abs(this.x - other.x) > THRESHOLD) return false;
		if(Math.abs(this.y - other.y) > THRESHOLD) return false;
		if(Math.abs(this.z - other.z) > THRESHOLD) return false;
		
		return true;
	}
}
