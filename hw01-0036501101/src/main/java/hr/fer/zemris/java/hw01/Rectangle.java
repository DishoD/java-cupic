package hr.fer.zemris.java.hw01;

import java.util.Scanner;

/**
 * This is a simple command line program that calculates surface and perimeter
 * of a rectangle. Width and height of a rectangle can be given trough command
 * line arguments or trough the standard input.
 * 
 * @author Hrvoje Ditrih
 * @version 1.0
 *
 */
public class Rectangle {
	private static Scanner sc;

	/**
	 * The main methode of a program. Controls the flow of a program.
	 * 
	 * @param args
	 *            command line arguments
	 */
	public static void main(String[] args) {
		double width = 0;
		double height = 0;

		if (args.length == 2) {
			try {
				width = Double.parseDouble(args[0]);
				height = Double.parseDouble(args[1]);

				if (width <= 0 || height <= 0) {
					System.out.println("Visina i širina ne smiju biti negativni brojevi ili nule.");
					System.exit(0);
				}
			} catch (NumberFormatException ex) {
				System.out.println("Uneseni argumenti se ne mogu interpretirati kao brojevi.");
				System.exit(0);
			}
		} else if (args.length != 0) {
			System.out.println(
					"Kao argumente morate proslijediti dva pozitivna broja: širinu i visinu. Točno tim redosljedom.");
			System.exit(0);
		} else {
			System.out.println("Pozdrav! Ovaj program računa površinu i opseg pravokutnika.\n"
					+ "Širinu i visinu pravokutnika također možete predati kao argumente u naredbenom retku.\n\n");

			sc = new Scanner(System.in);

			width = inputPositiveNumber("Unesite širinu > ");
			height = inputPositiveNumber("Unesite visinu > ");

			sc.close();
		}

		double perimiter = 2 * width + 2 * height;
		double surface = width * height;

		System.out.printf("Pravokutnik širine %.2f i visine %.2f ima površinu %.2f te opseg %.2f.", width, height,
				surface, perimiter);
	}

	/**
	 * Returns a positive number from standard input.
	 * 
	 * @param message
	 *            Message to be printed before user input
	 * @return a positive number
	 */
	public static double inputPositiveNumber(String message) {
		System.out.print(message);

		while (sc.hasNext()) {
			if (sc.hasNextDouble()) {
				double value = sc.nextDouble();

				if (value < 0) {
					System.out.println("Unjeli ste negativnu vrijednost.");
				} else if (value == 0) {
					System.out.println("Pravokutnik ne može imati visinu ili širinu 0");
				} else {
					return value;
				}
			} else {
				String input = sc.next();
				System.out.println("'" + input + "' se ne može protumačiti kao broj.");
			}

			System.out.print(message);
		}

		// The methode should never come to this point.
		// This is put here only because compailer wouldn't compile otherwise.
		return 0;
	}
}
