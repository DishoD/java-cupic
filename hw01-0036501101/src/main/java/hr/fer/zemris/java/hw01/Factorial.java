package hr.fer.zemris.java.hw01;

import java.util.Scanner;

/**
 * Simple command line program that calculates factorial of a given number in
 * range [1, 20]. Program ends when a user enters 'kraj'.
 * 
 * @author Hrvoje Ditrih
 * @version 1.0
 *
 */
public class Factorial {
	/**
	 * The main program method. It takes user input and gives appropriate output.
	 * 
	 * @param args
	 *            command line arguments
	 */
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		System.out.println("Pozdrav! Ovaj program računa faktorijelu od cijelih brojeva u rasponu\n"
				+ "od 1 do 20. Ako želite završiti program unesite 'kraj'\n\n");

		System.out.print("Unesite broj > ");

		while (sc.hasNext()) {
			if (sc.hasNextInt()) {
				int number = sc.nextInt();

				if (number >= 1 && number <= 20) {
					System.out.println(number + "! = " + calculateFactorial(number));
				} else {
					System.out.println(number + " nije u dozvoljenom rasponu.");
				}
			} else {
				String input = sc.next();

				if (input.equals("kraj")) {
					System.out.println("Doviđenja.");
					break;
				} else {
					System.out.println("'" + input + "' nije cijeli broj.");
				}
			}

			System.out.print("\nUnesite broj > ");
		}

		sc.close();
	}

	/**
	 * Calculates the factorial of a given number in range [0, 20].
	 * 
	 * @param number
	 *            Integer in range [0, 20]
	 * @return factorial of a number
	 */
	public static long calculateFactorial(int number) {
		if (number < 0 || number > 20)
			throw new IllegalArgumentException();

		if (number == 0 || number == 1) {
			return 1;
		}

		return number * calculateFactorial(number - 1);
	}

}
