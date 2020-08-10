package hr.fer.zemris.java.hw16.trazilica;

/**
 * A simple word parser that parses some text and splits it into continuous
 * alphabetic character sequences.
 * 
 * @author Disho
 *
 */
public class SimpleWordParser {
	/**
	 * text
	 */
	private char[] text;
	/**
	 * current character index in text
	 */
	private int currentIndex;

	/**
	 * Initializes the parser with the given text.
	 * 
	 * @param text
	 *            text to parse
	 */
	public SimpleWordParser(String text) {
		this.text = text.toCharArray();
	}

	/**
	 * Returns next continuous alphabetic character sequence in the text.
	 * 
	 * @return continuous alphabetic character sequence in the text or null if no
	 *         more exists
	 */
	public String getNextWord() {
		StringBuilder sb = new StringBuilder();

		while (currentIndex < text.length) {
			char c = text[currentIndex];

			if (sb.length() == 0 && !Character.isAlphabetic(c)) {
				currentIndex++;
				continue;
			}

			if (Character.isAlphabetic(c)) {
				sb.append(c);
				currentIndex++;
				continue;
			}

			if (sb.length() != 0 && !Character.isAlphabetic(c)) {
				currentIndex++;
				return sb.toString();
			}
		}

		return sb.length() == 0 ? null : sb.toString();
	}
}
