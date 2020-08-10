package hr.fer.zemris.math;

import java.util.Objects;

/**
 * Represents a complex number polynomial  (z-z1)*(z-z2)*...*(z-zn)
 * where z1 to zn are its roots. Z is a complex number variable.
 * 
 * @author Disho
 *
 */
public class ComplexRootedPolynomial {
	private Complex[] roots;
	
	/**
	 * Initializes the polynomial with the given roots.
	 * 
	 * @param roots roots of the polynomial
	 */
	public ComplexRootedPolynomial(Complex ...roots) {
		if(roots.length <= 0) throw new IllegalArgumentException("You must provide at leas one root.");
		
		for(Complex root : roots) {
			Objects.requireNonNull(root, "No root can be null.");
		}
		
		this.roots = roots;
	}

	
	/**
	 * Computes polynomial value at given point z.
	 * 
	 * @param z substitutes z
	 * @return value at the point z
	 */
	public Complex apply(Complex z) {
		Complex r = z.sub(roots[0]);
		
		for(int i = 1; i < roots.length; ++i) {
			r = r.multiply(z.sub(roots[i]));
		}
		
		return r;
	}

	/**
	 * Converts this representation to ComplexPolynomial type.
	 * 
	 * @return ComplexPolynomial representation
	 */
	public ComplexPolynomial toComplexPolynom() {
		ComplexPolynomial r = new ComplexPolynomial(roots[0].negative(), Complex.ONE);
		
		for(int i = 1; i < roots.length; ++i) {
			ComplexPolynomial p = new ComplexPolynomial(roots[i].negative(), Complex.ONE);
			r = r.multiply(p);
		}
		
		return r;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		for(Complex root : roots) {
			sb.append("(z-(");
			sb.append(root.toString());
			sb.append("))*");
		}
		
		return sb.substring(0, sb.length()-1).toString();
	}

	
	/**
	 * Finds index of closest root for given complex number z that is within
	 * threshold. If there is no such root, returns -1.
	 * 
	 * @param z complex number z
	 * @param treshold threshold, can't be negative
	 * @return index
	 */
	public int indexOfClosestRootFor(Complex z, double threshold) {
		Objects.requireNonNull(z, "z can't be null");
		if(threshold <= 0) throw new IllegalArgumentException("threshold must be > 0");
		
		int index = -1;
		double minDelta = 0;
		
		for(int i = 0; i < roots.length; ++i) {
			double delta = z.sub(roots[i]).module();
			if(delta <= threshold) {
				if(index == -1) {
					index = i;
					minDelta = delta;
					continue;
				}
				
				if(delta < minDelta) {
					minDelta = delta;
				}
			}
		}
		
		return index+1;
	}
	
	/**
	 * Returns a root at the given index.
	 * 
	 * @param index index of the root
	 * @return root at the given index
	 */
	public Complex getRoot(int index) {
		if(index < 0 || index >= roots.length) throw new IndexOutOfBoundsException();
		
		return roots[index];
	}
}
