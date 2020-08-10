package hr.fer.zemris.java.hw07.crypto;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Contains utility methods for converting hex strings to byte arrays and reverse.
 * 
 * @author Disho
 *
 */
public class Util {
	/**
	 * hexadecimal digits
	 */
	private static final String[] hexDigits = new String[] {"0" ,"1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};
	/**
	 * maps single hexadecimal digit to a decimal number
	 */
	private static final Map<Character, Integer> hexToDecimal = new HashMap<>();
	
	static {
		hexToDecimal.put('0', 0);
		hexToDecimal.put('1', 1);
		hexToDecimal.put('2', 2);
		hexToDecimal.put('3', 3);
		hexToDecimal.put('4', 4);
		hexToDecimal.put('5', 5);
		hexToDecimal.put('6', 6);
		hexToDecimal.put('7', 7);
		hexToDecimal.put('8', 8);
		hexToDecimal.put('9', 9);
		hexToDecimal.put('a', 10);
		hexToDecimal.put('b', 11);
		hexToDecimal.put('c', 12);
		hexToDecimal.put('d', 13);
		hexToDecimal.put('e', 14);
		hexToDecimal.put('f', 15);
	}
	
	/**
	 * Converts a string representation of hexadecimal number to byte array.
	 * 
	 * @param keyText string representation of hexadecimal number
	 * @return byte array
	 */
	public static byte[] hextobyte(String keyText) {
		Objects.nonNull(keyText);
		
		if(keyText.length() % 2 != 0 || keyText.isEmpty()) 
			throw new IllegalArgumentException("keyText must contain even number of characters. It cointained " + keyText.length());
		
		char[] text = keyText.toLowerCase().toCharArray();
		byte[] result = new byte[text.length/2];
		
		for(int i = 0; i < text.length; i+=2) {
			int n1, n2;
			try {
				n1 = hexToDecimal.get(text[i]);
				n2 = hexToDecimal.get(text[i+1]);
			} catch(Exception ex) {
				throw new IllegalArgumentException("Illegal character. It was: " + text[i] + text[i+1]);
			}
			
			int number = n1*16 + n2;
			result[i/2] = (byte) (number >= 128 ? number - 256 : number);
		}
		
		return result;
	}
	
	/**
	 * Converts a byte array to an string representation of a hexadecimal number.
	 * 
	 * @param bytearray byte array
	 * @return string representation of a hexadecimal number
	 */
	public static String bytetohex(byte[] bytearray) {
		Objects.nonNull(bytearray);
		
		StringBuilder sb = new StringBuilder();
		
		for(byte num : bytearray) {
			int number = num < 0 ? 256 + num : num;
			int n1 = number/16;
			int n2 = number - 16*n1;
			
			sb.append(hexDigits[n1]).append(hexDigits[n2]);
		}
		
		return sb.toString();
	}
	
}
