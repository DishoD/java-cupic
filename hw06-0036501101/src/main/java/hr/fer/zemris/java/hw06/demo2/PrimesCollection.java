package hr.fer.zemris.java.hw06.demo2;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Represents a collection of prime numbers. You can grab i-th prime number by
 * iterating trough the class.
 * 
 * @author Disho
 *
 */
public class PrimesCollection implements Iterable<Integer> {
	/**
	 * stores how many prime numbers this collection holds
	 */
	private final int limit;

	/**
	 * Initializes the prime collection with first n prime numbers.
	 * 
	 * @param n
	 *            how many prime numbers will this collection gold
	 */
	public PrimesCollection(int n) {
		this.limit = n;
	}

	@Override
	public Iterator<Integer> iterator() {
		return new IteratorImpl();
	}

	/**
	 * Checks whether the given number is prime
	 * 
	 * @param number
	 *            number to check
	 * @return true if number is prime, false otherwise
	 */
	private static boolean isPrime(int number) {
		for (int i = 2; i <= (int) Math.sqrt(number); ++i) {
			if (number % i == 0)
				return false;
		}

		return true;
	}

	/**
	 * Iterator implementation for the <code>PrimesCollection</code>
	 * 
	 * @author Disho
	 *
	 */
	private class IteratorImpl implements Iterator<Integer> {
		/**
		 * current ordinal number of the prime number (starting with 0)
		 */
		private int n;
		/**
		 * last calculated prime number
		 */
		private int lastPrime = 1;

		@Override
		public boolean hasNext() {
			return n < limit;
		}

		@Override
		public Integer next() {
			if (n >= limit)
				throw new NoSuchElementException();

			while (true) {
				if (isPrime(++lastPrime)) {
					n++;
					return lastPrime;
				}
			}
		}

	}

}
