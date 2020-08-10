package hr.fer.zemris.java.hw16.trazilica;

import java.util.List;
import java.util.Objects;

/**
 * Represents a simple n-dimensional vector.
 * 
 * @author Disho
 *
 */
public class Vector {
	/**
	 * components of vector
	 */
	private List<Double> components;
	
	/**
	 * Initializes the vector of the dimension of the size of the given list which
	 * components are that of the elements of the list in the same order.
	 * 
	 * @param components list of vector components
	 */
	public Vector(List<Double> components) {
		this.components = components;
	}
	
	/**
	 * Calculates the norm of this vector.
	 * 
	 * @return norm
	 */
	double norm() {
		double norm = 0;
		for(double c : components) {
			norm += c*c;
		}
		
		return Math.sqrt(norm);
	}
	
	/**
	 * Calculates the dot product of this vector against the given vector.
	 * 
	 * @param other other vector
	 * @return dot product
	 */
	double dotProduct(Vector other) {
		Objects.requireNonNull(other, "other vector cannot be null");
		
		if(this.getDimension() != other.getDimension()) 
			throw new IllegalArgumentException("Other vector must be of the same dimension.");
		
		double product = 0;
		
		for(int i = 0; i < this.getDimension(); ++i) {
			product += this.getComponent(i) * other.getComponent(i);
		}
		
		return product;
	}
	
	/**
	 * Returns the dimension of this vector
	 * 
	 * @return dimension
	 */
	int getDimension() {
		return components.size();
	}
	
	/**
	 * Returns the component of this vector at the given index.
	 * 
	 * @param i index
	 * @return i-th component of vector
	 */
	double getComponent(int i) {
		if(i < 0 || i >= getDimension()) 
			throw new IndexOutOfBoundsException();
		
		return components.get(i);
	}
	
	@Override
	public String toString() {
		return components.toString();
	}
}
