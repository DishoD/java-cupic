package hr.fer.zemris.math;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is a representation of a complex number. This class is immutable,
 * which means that any modifying methode will return a new instance of
 * ComplexNumber. And once created complex number can't be changed.
 * 
 * @author Hrvoje Ditrih
 * @version
 */
public class Complex {
	/**
	 * Real part of the complex number
	 */
	private double real;
	/**
	 * Imaginary part of the complex number
	 */
	private double imaginary;
	
	/**
	 * 0
	 */
	public static final Complex ZERO = new Complex(0,0);
	/**
	 * 1
	 */
	public static final Complex ONE = new Complex(1,0);
	/**
	 * -1
	 */
	public static final Complex ONE_NEG = new Complex(-1,0);
	/**
	 * i
	 */
	public static final Complex IM = new Complex(0,1);
	/**
	 * -i
	 */
	public static final Complex IM_NEG = new Complex(0,-1);

	/**
	 * Used for comparing doubles. If two doubles differ less then this amonut it means
	 * that the are equal.
	 */
	private static double THRESHOLD = 1E-6;

	/**
	 * Initializes an complex number with given real and imaginary part.
	 * 
	 * @param real
	 *            real part of a complex number
	 * @param imaginary
	 *            imaginary part of a complex number
	 */
	public Complex(double real, double imaginary) {
		this.real = real;
		this.imaginary = imaginary;
	}

	/**
	 * Constructs a complex number from magnitude and angle, or as it is known (r,
	 * theta).
	 * 
	 * @param magnitude
	 *            magnitude - r
	 * @param angle
	 *            angle - theta
	 * @return a complex number
	 */
	public static Complex fromMagnitudeAndAngle(double magnitude, double angle) {
		double real = calculateReal(magnitude, angle);
		double imaginary = calculateImaginary(magnitude, angle);
		
		return new Complex(real, imaginary);
	}

	/**
	 * Calculates a real part of a complex number from magnitude and angle (r,
	 * theta).
	 * 
	 * @param magnitude
	 *            magnitude - r, must be 0 ot positive
	 * @param angle
	 *            angle - theta
	 * @return a real part of a complex number
	 * @throws IllegalArgumentException
	 *             if magnitude is negative
	 */
	private static double calculateReal(double magnitude, double angle) {
		if (magnitude < 0)
			throw new IllegalArgumentException("Magnitude must be >= 0. It was: " + magnitude);

		return Math.cos(angle) * magnitude;
	}

	/**
	 * Calculates an imaginary part of a complex number from magnitude and angle (r,
	 * theta).
	 * 
	 * @param magnitude
	 *            magnitude - r, must be 0 ot positive
	 * @param angle
	 *            angle - theta
	 * @return an imaginary part of a complex number
	 * @throws IllegalArgumentException
	 *             if magnitude is negative
	 */
	private static double calculateImaginary(double magnitude, double angle) {
		if (magnitude < 0)
			throw new IllegalArgumentException("Magnitude must be >= 0. It was: " + magnitude);

		return Math.sin(angle) * magnitude;
	}
	
	/**
	 * Constructs a complex number by parsing a string representation of a complex
	 * number. It should contain only numerical values(0-9), dots(.), pluses(+) and 
	 * minuses(-), and imaginary simbols(i). 
	 * For example: "3.51", "-3.17", "-2.71i", "i", "1", "-2.71 - 3.15i"
	 * 
	 * @param s
	 *            a string to be parsed
	 * @return a complex number
	 * @throws NullPointerException
	 *             if string is null
	 * @throws IllegalArgumentException
	 *             if string is empty or it couldn't be parsed
	 */
	public static Complex parse(String s) {
		if (s == null)
			throw new NullPointerException("String must not be null.");
		if (s.isEmpty())
			throw new IllegalArgumentException("String must not be empty.");
		
		s = s.replaceAll("\\s", "");
		String[] numbers = s.split("(?=(\\+|-))");
		double real = 0;
		double imaginary = 0;

		for (String number : numbers) {
			if (number.startsWith("+i") || number.startsWith("-i") || number.startsWith("i")) {
				number = number.replace("i", "");
				if (number.isEmpty() || number.equals("+") || number.equals("-")) {
					number += "1";
				}

				try {
					imaginary += Double.parseDouble(number);
				} catch (NumberFormatException ex) {
					throw new IllegalArgumentException("Can't parse '" + number + "' into a number.");
				}

			} else {
				try {
					real += Double.parseDouble(number);
				} catch (NumberFormatException ex) {
					throw new IllegalArgumentException("Can't parse '" + number + "' into a number.");
				}
			}
		}

		return new Complex(real, imaginary);
	}

	/**
	 * Calculates module r in (r, theta) for a complex number.
	 * 
	 * @return module r in (r, theta)
	 */
	public double module() {
		return Math.sqrt(real * real + imaginary * imaginary);
	}


	/**
	 * Adds a givne complex number to the current one and returns an result.
	 * 
	 * @param other
	 *            complex number to be added
	 * @return a new resulted complex number
	 */
	public Complex add(Complex other) {
		return new Complex(
				this.real      + other.real, 
				this.imaginary + other.imaginary
		);
	}

	/**
	 * Subtracts a givne complex number to the current one and returns an result.
	 * 
	 * @param other
	 *            complex number to be subtracted
	 * @return a new resulted complex number
	 */
	public Complex sub(Complex other) {
		return new Complex(
				this.real      - other.real, 
				this.imaginary - other.imaginary
		);
	}

	/**
	 * Multiplies a givne complex number with the current one and returns an result.
	 * 
	 * @param other
	 *            complex number to be multipied with
	 * @return a new resulted complex number
	 */
	public Complex multiply(Complex other) {
		return new Complex(this.real*other.real - this.imaginary*other.imaginary, 
							this.real*other.imaginary + this.imaginary*other.real);
	}

	/**
	 * Divides the current complex number with the given one and returns an result.
	 * Can't divide by zero!
	 * 
	 * @param other
	 *            complex number to be divided with
	 * @return a new resulted complex number
	 * @throws IllegalArgumentException if given complex number is zero
	 */
	public Complex divide(Complex other) {
		if(other.real == 0 && other.imaginary == 0) throw new IllegalArgumentException("Can't divide by zero");
		
		double denominator = other.real*other.real + other.imaginary*other.imaginary;
		return new Complex((this.real*other.real+this.imaginary*other.imaginary)/denominator, 
							(this.imaginary*other.real-this.real*other.imaginary)/denominator);
	}
	
	/**
	 * Constructs a negative complex number of this complex number.
	 * 
	 * @return negative complex number
	 */
	public Complex negative() {
		return new Complex(-real, -imaginary);
	}

	/**
	 * Calculates an n-th power of a complex number.
	 * 
	 * @param n
	 *            exponent, must be greater or equal to 0
	 * @return a new resulted complex number
	 */
	public Complex power(int n) {
		if (n < 0)
			throw new IllegalArgumentException("n must be >= 0. It was: " + n);
		
		
		Complex r = new Complex(1, 0);
		
		Complex c = this;
		
		for(int i = 0; i < n; ++i) {
			r = r.multiply(c);
		}
		
		return r;
	}

	/**
	 * Calculates n roots of a complex number.
	 * 
	 * @param n
	 *            root, must be greater than zero
	 * @return n roots of a complex number
	 */
	public List<Complex> root(int n) {
		if (n <= 0)
			throw new IllegalArgumentException("n must be greater then 0. It was: " + n);

		double newMagnitude = Math.pow(this.module(), 1.0 / n);
		double angle = Math.atan2(imaginary, real);

		List<Complex> roots = new ArrayList<>();
		for (int i = 0; i < n; ++i) {
			double newAngle = (angle + 2.0 * i * Math.PI) / n;

			roots.add(fromMagnitudeAndAngle(newMagnitude, newAngle));
		}

		return roots;
	}
	
	/**
	 * Returns real part of the complex number
	 * @return real part
	 */
	public double getReal() {
		return real;
	}
	
	/**
	 * Returns imaginary part of the complex number
	 * @return imaginary part
	 */
	public double getImaginary() {
		return imaginary;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (Math.abs(real) > THRESHOLD) {
			sb.append(real);
		}

		if (Math.abs(imaginary) > THRESHOLD) {
			if (imaginary < 0) {
				sb.append("-");
			} else if (imaginary >= 0 && sb.length() != 0) {
				sb.append("+");
			}
			
			sb.append(Math.abs(imaginary)).append("i");
		}

		return sb.toString();
	}
	
}
