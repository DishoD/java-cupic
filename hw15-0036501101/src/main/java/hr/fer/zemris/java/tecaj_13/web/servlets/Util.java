package hr.fer.zemris.java.tecaj_13.web.servlets;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Contains utility methods for blog servlets.
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
	 * Converts a byte array to an string representation of a hexadecimal number.
	 * 
	 * @param bytearray byte array
	 * @return string representation of a hexadecimal number
	 */
	private static String bytetohex(byte[] bytearray) {
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
	
	/**
	 * Sends redirect to the error/info page with the given message.
	 * 
	 * @param msg message to display
	 * @param req http request
	 * @param resp http response
	 */
	public static void sendInfo(String msg, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setAttribute("msg", msg);
		req.getRequestDispatcher("/WEB-INF/pages/info.jsp").forward(req, resp);
	}
	
	/**
	 * For the given password generates an SHA-1 hash.
	 * 
	 * @param password password to hash
	 * @return hash of the password
	 */
	public static String hahsPassword(String password) {
		MessageDigest sha = null;

		try {
			sha = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException igonrable) {
		}
		
		sha.update(password.getBytes());
		return Util.bytetohex(sha.digest());
	}
	
	/**
	 * Checks validity of the given e-mail address.
	 * 
	 * @param email e-mail address
	 * @return true if e-mail is valid, false otherwise
	 */
	public static boolean validateEmail(String email) {
		Pattern pattern = Pattern.compile("^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(email);
		
		return matcher.matches();
	}
	
}
