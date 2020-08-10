package hr.fer.zemris.math;

import java.util.Objects;

/**
 * Represents a complex polynomial: z(n)*z^n+z(n-1)*z^(n-1)+...+z2*z^2+z1*z+z0
 * of the order n, where z is the complex variable and zn...z0 its factors.
 * 
 * @author Disho
 *
 */
public class ComplexPolynomial {
	private Complex[] factors;

	/**
	 * Initializes the complex polynomial with the given factors. For polynomial
	 * z(n)*z^n+z(n-1)*z^(n-1)+...+z2*z^2+z1*z+z0, first factor should be z0, and
	 * last zn.
	 * 
	 * @param factors polynomial factors z0...zn
	 */
	public ComplexPolynomial(Complex... factors) {
		if (factors.length <= 0)
			throw new IllegalArgumentException("You must provide at least one factor.");

		for (Complex factor : factors) {
			Objects.requireNonNull(factor, "No factor can be null.");
		}

		this.factors = factors;
	}

	/**
	 * Returns order of this polynom. eg. For (7+2i)z^3+2z^2+5z+1 returns 3
	 * 
	 * @return polynom order
	 */
	public short order() {
		return (short) (factors.length - 1);
	}


	/**
	 * Multiplies this polynomial with the given polynomial and returns the result.
	 * 
	 * @param p complex polynomial to multiply with
	 * @return product of the polynomials
	 */
	public ComplexPolynomial multiply(ComplexPolynomial p) {
		Complex[] newFactors = new Complex[order() + p.order() + 1];
		for(int i = 0; i < factors.length; ++i) {
			for(int j = 0; j < p.factors.length; ++j) {
				newFactors[i+j] = newFactors[i+j] == null ? factors[i].multiply(p.factors[j]) : newFactors[i+j].add(factors[i].multiply(p.factors[j]));
			}
		}
		
		return new ComplexPolynomial(newFactors);
	}

	
	/**
	 * Computes first derivative of this polynomial
	 * 
	 * @return first derivative of this polynomial
	 */
	public ComplexPolynomial derive() {
		Complex[] newFactors = new Complex[order()];
		
		for(int i = 0; i < newFactors.length; ++i) {
			newFactors[i] = factors[i+1].multiply(new Complex(i+1, 0));
		}
		
		return new ComplexPolynomial(newFactors);
	}

	/**
	 * Computes polynomial value at given point z
	 * 
	 * @param z substitutes z
	 * @return computed value of the polynomial at the point z
	 */
	public Complex apply(Complex z) {
		Complex r = factors[0];
		
		for(int i = 1; i < factors.length; ++i) {
			r = r.add(factors[i].multiply(z.power(i)));
		}
		
		return r;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(factors[0].toString() + " + ");
		
		for(int i = 1; i < factors.length; ++i) {
			sb.append("(");
			sb.append(factors[i].toString());
			sb.append(")*z^");
			sb.append(i);
			sb.append(" + ");
		}
		
		return sb.substring(0, sb.length() - 3).toString();
	}
}
