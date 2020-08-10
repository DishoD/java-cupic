package hr.fer.zemris.java.hw06.demo2;

/**
 * Demonstration program for PrimeCollection
 * 
 * @author Disho
 *
 */
public class PrimesDemo2 {
	/**
	 * Main method> runs the program
	 * 
	 * @param args
	 *            ignorable
	 */
	public static void main(String[] args) {
		PrimesCollection primesCollection = new PrimesCollection(2);
		for (Integer prime : primesCollection) {
			for (Integer prime2 : primesCollection) {
				System.out.println("Got prime pair: " + prime + ", " + prime2);
			}
		}

	}
}