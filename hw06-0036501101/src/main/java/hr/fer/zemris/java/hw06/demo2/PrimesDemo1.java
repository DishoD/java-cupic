package hr.fer.zemris.java.hw06.demo2;

/**
 * Demonstration program for PrimeCollection
 * 
 * @author Disho
 *
 */
public class PrimesDemo1 {
	/**
	 * Main method> runs the program
	 * 
	 * @param args
	 *            ignorable
	 */
	public static void main(String[] args) {
		PrimesCollection primesCollection = new PrimesCollection(5); // 5: how many of them
		for (Integer prime : primesCollection) {
			System.out.println("Got prime: " + prime);
		}
	}
}
