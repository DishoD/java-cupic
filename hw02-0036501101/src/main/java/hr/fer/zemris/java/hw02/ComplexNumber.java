package hr.fer.zemris.java.hw02;

/**
 * This class is a representation of a complex number. This class is immutable,
 * which means that any modifying methode will return a new instance of
 * ComplexNumber. And once created complex number can't be changed.
 * 
 * @author Hrvoje Ditrih
 * @version
 */
public class ComplexNumber {
	private double real;
	private double imaginary;
	private double magnitude;
	private double angle;

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
	public ComplexNumber(double real, double imaginary) {
		this.real = real;
		this.imaginary = imaginary;
		this.magnitude = calculateMagnitude(real, imaginary);
		this.angle = calculateAngle(real, imaginary);
	}

	/**
	 * Constructs a complex number from a real number.
	 * 
	 * @param real
	 *            number from which a complex number will be constructed
	 * @return a complex number
	 */
	public static ComplexNumber fromReal(double real) {
		return new ComplexNumber(real, 0);
	}

	/**
	 * Constructs a complex number from a imaginary number.
	 * 
	 * @param imaginary
	 *            number from which a complex number will be constructed
	 * @return a complex number
	 */
	public static ComplexNumber fromImaginary(double imaginary) {
		return new ComplexNumber(0, imaginary);
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
	public static ComplexNumber fromMagnitudeAndAngle(double magnitude, double angle) {
		double real = calculateReal(magnitude, angle);
		double imaginary = calculateImaginary(magnitude, angle);
		
		return new ComplexNumber(real, imaginary);
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
	public static ComplexNumber parse(String s) {
		if (s == null)
			throw new NullPointerException("String must not be null.");
		if (s.isEmpty())
			throw new IllegalArgumentException("String must not be empty.");
		
		s = s.replaceAll("\\s", "");
		String[] numbers = s.split("(?=(\\+|-))");
		double real = 0;
		double imaginary = 0;

		for (String number : numbers) {
			if (number.endsWith("i")) {
				number = number.substring(0, number.length() - 1);
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

		return new ComplexNumber(real, imaginary);
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
	 * Calculates an angle theta in (r, theta) for a complex number.
	 * 
	 * @param real
	 *            real part of a complex number
	 * @param imaginary
	 *            imaginary part of a complex number
	 * @return an angle theta in (r, theta) from range [-pi, pi]
	 */
	private static double calculateAngle(double real, double imaginary) {
		double angle = Math.atan2(imaginary, real);
		
		return angle;
	}

	/**
	 * Calculates magnitude r in (r, theta) for a complex number.
	 * 
	 * @param real
	 *            real part of a complex number
	 * @param imaginary
	 *            imaginary part of a complex number
	 * @return magnitude r in (r, theta)
	 */
	private static double calculateMagnitude(double real, double imaginary) {
		return Math.sqrt(real * real + imaginary * imaginary);
	}

	/**
	 * Get a real part from complex number.
	 * 
	 * @return real part
	 */
	public double getReal() {
		return real;
	}

	/**
	 * Get an imaginary part from complex number.
	 * 
	 * @return imaginary part
	 */
	public double getImaginary() {
		return imaginary;
	}

	/**
	 * Get magnitude r in (r, theta) from a complex number.
	 * 
	 * @return magnitude r in (r, theta)
	 */
	public double getMagnitude() {
		return magnitude;
	}

	/**
	 * Get angle theta in (r, theta) from a complex number.
	 * 
	 * @return angle theta in (r, theta) in rangle [0, 2pi)
	 */
	public double getAngle() {
		if(Math.abs(angle) >= 2*Math.PI) {
			double factor = angle/(2*Math.PI);
			angle *=  factor - Math.floor(factor) ;
		}
		
		return angle >= 0 ? angle : angle + 2 * Math.PI;
	}

	/**
	 * Adds a givne complex number to the current one and returns an result.
	 * 
	 * @param other
	 *            complex number to be added
	 * @return a new resulted complex number
	 */
	public ComplexNumber add(ComplexNumber other) {
		return new ComplexNumber(
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
	public ComplexNumber sub(ComplexNumber other) {
		return new ComplexNumber(
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
	public ComplexNumber mul(ComplexNumber other) {
		return fromMagnitudeAndAngle(
				this.magnitude * other.magnitude, 
				this.angle     + other.angle
		);
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
	public ComplexNumber div(ComplexNumber other) {
		if(Math.abs(other.magnitude) < THRESHOLD)
			throw new IllegalArgumentException("can't divide by zero! You passed zero.");
		
		return fromMagnitudeAndAngle(
				this.magnitude / other.magnitude, 
				this.angle     - other.angle
		);
	}

	/**
	 * Calculates an n-th power of a complex number.
	 * 
	 * @param n
	 *            exponent, must be greater or equal to 0
	 * @return a new resulted complex number
	 */
	public ComplexNumber power(int n) {
		if (n < 0)
			throw new IllegalArgumentException("n must be >= 0. It was: " + n);

		return fromMagnitudeAndAngle(Math.pow(magnitude, n), angle * n);
	}

	/**
	 * Calculates n roots of a complex number.
	 * 
	 * @param n
	 *            root, must be greater than zero
	 * @return n roots of a complex number
	 */
	public ComplexNumber[] root(int n) {
		if (n <= 0)
			throw new IllegalArgumentException("n must be greater then 0. It was: " + n);

		double newMagnitude = Math.pow(this.magnitude, 1.0 / n);

		ComplexNumber[] roots = new ComplexNumber[n];
		for (int i = 0; i < n; ++i) {
			double newAngle = (this.angle + 2.0 * i * Math.PI) / n;

			roots[i] = fromMagnitudeAndAngle(newMagnitude, newAngle);
		}

		return roots;
	}

	@Override
	public String toString() {
		if (Math.abs(magnitude) <= THRESHOLD)
			return "0";

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

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (!(obj instanceof ComplexNumber))
			return false;

		ComplexNumber other = (ComplexNumber) obj;
		return  Math.abs(this.real      - other.real)      < THRESHOLD && 
				Math.abs(this.imaginary - other.imaginary) < THRESHOLD;
	}
	
}
