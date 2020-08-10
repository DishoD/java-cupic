package hr.fer.zemris.java.hw02.demo;

import hr.fer.zemris.java.hw02.ComplexNumber;

/**
 * This program is a simple demonstration of a ComplexNumber class.
 * 
 * @author Hrvoje Ditrih
 * @version 1.0
 */
public class ComplexDemo {

	/**
	 * Demonstrates ComplexNumber class and its methods.
	 * 
	 * @param args command-line arguments
	 */
	public static void main(String[] args) {
		ComplexNumber c1 = new ComplexNumber(2, 3);
		ComplexNumber c2 = ComplexNumber.parse("2.5-3i");
		ComplexNumber c3 = c1.add(ComplexNumber.fromMagnitudeAndAngle(2, 1.57))
				              .div(c2).power(3).root(2)[1];

		System.out.println(c3);
	}

}
