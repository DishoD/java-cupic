package hr.fer.zemris.java.fractals;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import hr.fer.zemris.math.Complex;

@SuppressWarnings("javadoc")
public class ComplexTest {

	@Test
	public void getRealAndGetImaginary() {
		Complex cp1 = new Complex(0.59, -45.26);
		Complex cp2 = new Complex(0, 0);
		Complex cp3 = new Complex(-100, 222222);
		Complex cp4 = new Complex(1, 0);

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
	public void magnitudeAndAngle() {
		Complex cp1 = Complex.fromMagnitudeAndAngle(0, 66);
		Complex cp2 = Complex.fromMagnitudeAndAngle(1, Math.PI);
		Complex cp3 = Complex.fromMagnitudeAndAngle(5, -Math.PI / 4);
		Complex cp4 = Complex.fromMagnitudeAndAngle(100, 66);

		assertTrue(areEqual(0, cp1.getReal()));
		assertTrue(areEqual(0, cp1.getImaginary()));
		assertTrue(areEqual(0, cp1.module()));

		assertTrue(areEqual(-1, cp2.getReal()));
		assertTrue(areEqual(0, cp2.getImaginary()));
		assertTrue(areEqual(1, cp2.module()));

		assertTrue(areEqual(3.535533906, cp3.getReal()));
		assertTrue(areEqual(-3.5355333906, cp3.getImaginary()));
		assertTrue(areEqual(5, cp3.module()));

		assertTrue(areEqual(-99.9647456, cp4.getReal()));
		assertTrue(areEqual(-2.655115402, cp4.getImaginary()));
		assertTrue(areEqual(100, cp4.module()));
	}

	@Test
	public void parse() {
		Complex cp1 = Complex.parse("0");
		Complex cp2 = Complex.parse("1");
		Complex cp3 = Complex.parse("i  ");
		Complex cp4 = Complex.parse(" -i");
		Complex cp5 = Complex.parse("  -100.26  ");
		Complex cp6 = Complex.parse("-i25.3");
		Complex cp7 = Complex.parse("12 - i9");
		Complex cp8 = Complex.parse("i5+7.26");
		Complex cp9 = Complex.parse("5 - i3 + 2 + i4");
		Complex cp10 = Complex.parse("-9 - 9");
		Complex cp11 = Complex.parse("-i    -i");

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
		Complex cp1 = new Complex(5, 9);
		Complex cp2 = new Complex(-3, 16);
		Complex cp3 = cp1.add(cp2);

		assertTrue(areEqual(2, cp3.getReal()));
		assertTrue(areEqual(25, cp3.getImaginary()));
	}

	@Test
	public void sub() {
		Complex cp1 = new Complex(5, 9);
		Complex cp2 = new Complex(-3, 16);
		Complex cp3 = cp1.sub(cp2);

		assertTrue(areEqual(8, cp3.getReal()));
		assertTrue(areEqual(-7, cp3.getImaginary()));
	}

	@Test
	public void mul() {
		Complex cp1 = new Complex(5, 9);
		Complex cp2 = new Complex(-3, 16);
		Complex cp3 = cp1.multiply(cp2);

		assertTrue(areEqual(-159, cp3.getReal()));
		assertTrue(areEqual(53, cp3.getImaginary()));
		assertTrue(areEqual(167.600716, cp3.module()));
	}

	@Test
	public void div() {
		Complex cp1 = new Complex(5, 9);
		Complex cp2 = new Complex(-3, 16);
		Complex cp3 = cp1.divide(cp2);

		assertTrue(areEqual(0.4867924528, cp3.getReal()));
		assertTrue(areEqual(-0.4037735849, cp3.getImaginary()));
		assertTrue(areEqual(0.632455532, cp3.module()));
	}

	@Test
	public void power() {
		Complex cp1 = new Complex(5, 9);
		Complex cp3 = cp1.power(3);

		assertTrue(areEqual(-1090, cp3.getReal()));
		assertTrue(areEqual(-54, cp3.getImaginary()));
		assertTrue(areEqual(1091.336795, cp3.module()));
	}

	@Test
	public void root() {
		Complex cp = Complex.fromMagnitudeAndAngle(6561, Math.PI / 4);
		List<Complex> roots = cp.root(4);

		assertTrue(areEqual(9, roots.get(0).module()));

		assertTrue(areEqual(9, roots.get(1).module()));

		assertTrue(areEqual(9, roots.get(2).module()));

		assertTrue(areEqual(9, roots.get(3).module()));
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
