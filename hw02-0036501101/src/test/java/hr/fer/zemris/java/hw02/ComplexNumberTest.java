package hr.fer.zemris.java.hw02;

import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class ComplexNumberTest {

	@Test
	public void getRealAndGetImaginary() {
		ComplexNumber cp1 = new ComplexNumber(0.59, -45.26);
		ComplexNumber cp2 = new ComplexNumber(0, 0);
		ComplexNumber cp3 = new ComplexNumber(-100, 222222);
		ComplexNumber cp4 = new ComplexNumber(1, 0);

		assertTrue(areEqual(0.59, cp1.getReal()));
		assertTrue(areEqual(-45.26, cp1.getImaginary()));

		assertTrue(areEqual(0, cp2.getReal()));
		assertTrue(areEqual(0, cp2.getImaginary()));

		assertTrue(areEqual(-100, cp3.getReal()));
		assertTrue(areEqual(222222, cp3.getImaginary()));

		assertTrue(areEqual(1, cp4.getReal()));
		assertTrue(areEqual(0, cp4.getImaginary()));
	}

	@Test
	public void fromReal() {
		ComplexNumber cp1 = ComplexNumber.fromReal(0);
		ComplexNumber cp2 = ComplexNumber.fromReal(1);
		ComplexNumber cp3 = ComplexNumber.fromReal(0.889);
		ComplexNumber cp4 = ComplexNumber.fromReal(-125.66);

		assertTrue(areEqual(0, cp1.getReal()));
		assertTrue(areEqual(1, cp2.getReal()));
		assertTrue(areEqual(0.889, cp3.getReal()));
		assertTrue(areEqual(-125.66, cp4.getReal()));
	}

	@Test
	public void fromImaginary() {
		ComplexNumber cp1 = ComplexNumber.fromImaginary(0);
		ComplexNumber cp2 = ComplexNumber.fromImaginary(1);
		ComplexNumber cp3 = ComplexNumber.fromImaginary(0.889);
		ComplexNumber cp4 = ComplexNumber.fromImaginary(-125.66);

		assertTrue(areEqual(0, cp1.getImaginary()));
		assertTrue(areEqual(1, cp2.getImaginary()));
		assertTrue(areEqual(0.889, cp3.getImaginary()));
		assertTrue(areEqual(-125.66, cp4.getImaginary()));
	}

	@Test
	public void magnitudeAndAngle() {
		ComplexNumber cp1 = ComplexNumber.fromMagnitudeAndAngle(0, 66);
		ComplexNumber cp2 = ComplexNumber.fromMagnitudeAndAngle(1, Math.PI);
		ComplexNumber cp3 = ComplexNumber.fromMagnitudeAndAngle(5, -Math.PI / 4);
		ComplexNumber cp4 = ComplexNumber.fromMagnitudeAndAngle(100, 66);

		assertTrue(areEqual(0, cp1.getReal()));
		assertTrue(areEqual(0, cp1.getImaginary()));
		assertTrue(areEqual(0, cp1.getMagnitude()));

		assertTrue(areEqual(-1, cp2.getReal()));
		assertTrue(areEqual(0, cp2.getImaginary()));
		assertTrue(areEqual(1, cp2.getMagnitude()));
		assertTrue(areEqual(Math.PI, cp2.getAngle()));

		assertTrue(areEqual(3.535533906, cp3.getReal()));
		assertTrue(areEqual(-3.5355333906, cp3.getImaginary()));
		assertTrue(areEqual(5, cp3.getMagnitude()));
		assertTrue(areEqual(7 * Math.PI / 4, cp3.getAngle()));

		assertTrue(areEqual(-99.9647456, cp4.getReal()));
		assertTrue(areEqual(-2.655115402, cp4.getImaginary()));
		assertTrue(areEqual(100, cp4.getMagnitude()));
		assertTrue(areEqual(3.168146917, cp4.getAngle()));
	}

	@Test
	public void parse() {
		ComplexNumber cp1 = ComplexNumber.parse("0");
		ComplexNumber cp2 = ComplexNumber.parse("1");
		ComplexNumber cp3 = ComplexNumber.parse("i  ");
		ComplexNumber cp4 = ComplexNumber.parse(" -i");
		ComplexNumber cp5 = ComplexNumber.parse("  -100.26  ");
		ComplexNumber cp6 = ComplexNumber.parse("-25.3i");
		ComplexNumber cp7 = ComplexNumber.parse("12 - 9i");
		ComplexNumber cp8 = ComplexNumber.parse("5i+7.26");
		ComplexNumber cp9 = ComplexNumber.parse("5 - 3i + 2 + 4i");
		ComplexNumber cp10 = ComplexNumber.parse("-9 - 9");
		ComplexNumber cp11 = ComplexNumber.parse("-i    -i");

		assertTrue(areEqual(0, cp1.getReal()));
		assertTrue(areEqual(0, cp1.getImaginary()));

		assertTrue(areEqual(1, cp2.getReal()));
		assertTrue(areEqual(0, cp2.getImaginary()));

		assertTrue(areEqual(0, cp3.getReal()));
		assertTrue(areEqual(1, cp3.getImaginary()));

		assertTrue(areEqual(0, cp4.getReal()));
		assertTrue(areEqual(-1, cp4.getImaginary()));

		assertTrue(areEqual(-100.26, cp5.getReal()));
		assertTrue(areEqual(0, cp5.getImaginary()));

		assertTrue(areEqual(0, cp6.getReal()));
		assertTrue(areEqual(-25.3, cp6.getImaginary()));

		assertTrue(areEqual(12, cp7.getReal()));
		assertTrue(areEqual(-9, cp7.getImaginary()));

		assertTrue(areEqual(7.26, cp8.getReal()));
		assertTrue(areEqual(5, cp8.getImaginary()));

		assertTrue(areEqual(7, cp9.getReal()));
		assertTrue(areEqual(1, cp9.getImaginary()));

		assertTrue(areEqual(-18, cp10.getReal()));
		assertTrue(areEqual(0, cp10.getImaginary()));

		assertTrue(areEqual(0, cp11.getReal()));
		assertTrue(areEqual(-2, cp11.getImaginary()));
	}

	@Test
	public void add() {
		ComplexNumber cp1 = new ComplexNumber(5, 9);
		ComplexNumber cp2 = new ComplexNumber(-3, 16);
		ComplexNumber cp3 = cp1.add(cp2);

		assertTrue(areEqual(2, cp3.getReal()));
		assertTrue(areEqual(25, cp3.getImaginary()));
	}

	@Test
	public void sub() {
		ComplexNumber cp1 = new ComplexNumber(5, 9);
		ComplexNumber cp2 = new ComplexNumber(-3, 16);
		ComplexNumber cp3 = cp1.sub(cp2);

		assertTrue(areEqual(8, cp3.getReal()));
		assertTrue(areEqual(-7, cp3.getImaginary()));
	}

	@Test
	public void mul() {
		ComplexNumber cp1 = new ComplexNumber(5, 9);
		ComplexNumber cp2 = new ComplexNumber(-3, 16);
		ComplexNumber cp3 = cp1.mul(cp2);

		assertTrue(areEqual(-159, cp3.getReal()));
		assertTrue(areEqual(53, cp3.getImaginary()));
		assertTrue(areEqual(167.600716, cp3.getMagnitude()));
		assertTrue(areEqual(2.819842099, cp3.getAngle()));
	}

	@Test
	public void div() {
		ComplexNumber cp1 = new ComplexNumber(5, 9);
		ComplexNumber cp2 = new ComplexNumber(-3, 16);
		ComplexNumber cp3 = cp1.div(cp2);

		assertTrue(areEqual(0.4867924528, cp3.getReal()));
		assertTrue(areEqual(-0.4037735849, cp3.getImaginary()));
		assertTrue(areEqual(0.632455532, cp3.getMagnitude()));
		assertTrue(areEqual(-0.6924464544 + 2 * Math.PI, cp3.getAngle()));
	}

	@Test
	public void power() {
		ComplexNumber cp1 = new ComplexNumber(5, 9);
		ComplexNumber cp3 = cp1.power(3);

		assertTrue(areEqual(-1090, cp3.getReal()));
		assertTrue(areEqual(-54, cp3.getImaginary()));
		assertTrue(areEqual(1091.336795, cp3.getMagnitude()));
		assertTrue(areEqual(-3.09209184 + 2 * Math.PI, cp3.getAngle()));
	}

	@Test
	public void root() {
		ComplexNumber cp = ComplexNumber.fromMagnitudeAndAngle(6561, Math.PI / 4);
		ComplexNumber[] roots = cp.root(4);

		assertTrue(areEqual(9, roots[0].getMagnitude()));
		assertTrue(areEqual(Math.PI / 16, roots[0].getAngle()));

		assertTrue(areEqual(9, roots[1].getMagnitude()));
		assertTrue(areEqual(Math.PI / 16 + 0.5 * 1 * Math.PI, roots[1].getAngle()));

		assertTrue(areEqual(9, roots[2].getMagnitude()));
		assertTrue(areEqual(Math.PI / 16 + 0.5 * 2 * Math.PI, roots[2].getAngle()));

		assertTrue(areEqual(9, roots[3].getMagnitude()));
		assertTrue(areEqual(Math.PI / 16 + 0.5 * 3 * Math.PI, roots[3].getAngle()));
	}

	/**
	 * Tests if two doubles are equal. The may differ up to 1E-6.
	 * 
	 * @param first
	 *            double
	 * @param second
	 *            double
	 * @return true if equal, false otherwise
	 */
	private boolean areEqual(double first, double second) {
		return Math.abs(first - second) < 1E-6;
	}
}
