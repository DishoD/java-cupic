package hr.fer.zemris.java.hw07.shell.commands;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains utility methods for shell commands.
 * 
 * @author Disho
 *
 */
public class Util {
	/**
	 * Parses arguments of the command and returns an array of arguments.
	 * 
	 * @param arguments command arguments
	 * @return array of strings
	 */
	public static List<String> parseArguments(String arguments) {
		char[] text = arguments.toCharArray();
		int currentIndex = 0;
		List<String> result = new ArrayList<>();
		boolean isSpaced = false;
		
		StringBuilder sb = new StringBuilder();
		
		while(currentIndex < text.length) {
			char currentChar = text[currentIndex];
			
			if(sb.length() == 0 && Character.isWhitespace(currentChar)) {
				currentIndex++;
				continue;
			}
			
			if(sb.length() == 0 && currentChar == '\"') {
				isSpaced = true;
				currentIndex++;
				continue;
			}
			
			if(isSpaced) {
				if(currentChar == '\\') {
					if(currentIndex + 1 >= text.length)
						throw new IllegalArgumentException("Illegal argument. Quotation mark not closed.");
					
					char next = text[currentIndex+1];
					if(next == '\"' || next == '\\') {
						sb.append(next);
						currentIndex += 2;
						continue;
					} else {
						sb.append(currentChar);
						currentIndex++;
						continue;
					}
				}
				
				if(currentChar == '\"') {
					if(currentIndex + 1 >= text.length || Character.isWhitespace(text[currentIndex+1])) {
						result.add(sb.toString());
						sb.delete(0, sb.length());
						currentIndex++;
						isSpaced = false;
						continue;
					}
					
					throw new IllegalArgumentException("Illegal argument: " + sb.toString());
				}
			} 
			
			if(!isSpaced) {
				if(Character.isWhitespace(currentChar)) {
					result.add(sb.toString());
					sb.delete(0, sb.length());
					currentIndex++;
					continue;
				}
			}
			
			sb.append(currentChar);
			currentIndex++;
		}
		
		if(isSpaced && sb.length() != 0) {
			throw new IllegalArgumentException("Illegal argument. Quotation mark not closed.");
		}
		
		if(!isSpaced && sb.length() != 0) {
			result.add(sb.toString());
		}
		
		return result;
	}
	
}
