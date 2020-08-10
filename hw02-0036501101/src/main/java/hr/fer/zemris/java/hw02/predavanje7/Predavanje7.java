package hr.fer.zemris.java.hw02.predavanje7;

import java.util.Scanner;

import hr.fer.zemris.java.hw02.ComplexNumber;

public class Predavanje7 {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		double Cre = sc.nextDouble();
		double Cim = sc.nextDouble();
		
		ComplexNumber c = new ComplexNumber(Cre, Cim);
		
		ComplexNumber z = ComplexNumber.fromReal(0);
		
		for(int i = 1; true; ++i) {
			z = z.power(2);
			z = z.add(c);
			if(z.getMagnitude() > 2) {
				System.out.println(i);
				break;
			}
		}
	}
}
