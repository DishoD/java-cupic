package hr.fer.zemris.java.hw16.trazilica;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Represents a dictionary of words of some documents. All word from the documents will be added
 * to the dictionary unless they are in stop words dictionary. This class also tracks of how many documents
 * this dictionary was constructed of and how many documents did contain some word.
 * 
 * @author Disho
 *
 */
public class Dictionary {
	/**
	 * words in the dictionary
	 */
	private Set<String> words = new HashSet<>();
	/**
	 * maps how many documents contained some word
	 */
	private Map<String, Integer> wordDocumentFrequency = new HashMap<>();
	/**
	 * of how many documents was this dictionary constructed of
	 */
	private int numberOfDocuments;
	
	/**
	 * Constructs a dictionary of documents.
	 * 
	 * @param path path to a directory that contains documents
	 * @param stopWords dictionary of words that must not be contained in this dictionary
	 * @throws IOException
	 */
	public Dictionary(Path path, Set<String> stopWords) throws IOException {
		if(!Files.isDirectory(path)) throw new IllegalArgumentException("Path must be a directory");
		
		Files.list(path).filter(Files::isReadable).forEach(p -> {
			try {
				String text = new String(Files.readAllBytes(p), StandardCharsets.UTF_8);
				numberOfDocuments++;
				SimpleWordParser parser = new SimpleWordParser(text);
				Set<String> currentDocumentWords = new HashSet<>();
				
				while(true) {
					String word = parser.getNextWord();
					if(word == null) break;
					word = word.toLowerCase();
					if(stopWords.contains(word)) continue;
					
					words.add(word);
					if(!currentDocumentWords.contains(word)) {
						wordDocumentFrequency.merge(word, 1, (o, n) -> o+n);
						currentDocumentWords.add(word);
					}
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}
	
	/**
	 * Gets a set of words in this dictionary.
	 * 
	 * @return set of words in this dictionary
	 */
	public Set<String> getWords() {
		return Collections.unmodifiableSet(words);
	}
	
	/**
	 * Gets a number of words in the dictionary.
	 * 
	 * @return number of words
	 */
	public int getNumberOfWords() {
		return words.size();
	}
	
	/**
	 * Returns how many documents did contain the given word.
	 * 
	 * @param word word to check
	 * @return word document frequency
	 */
	public int getWordDocumentFrequency(String word) {
		Integer result = wordDocumentFrequency.get(word);
		return result == null ? 0 : result;
	}
	
	/**
	 * Gets a number of how many documents was this dictionary constructed of.
	 * 
	 * @return number of documents
	 */
	public int getNumberOfDocuments() {
		return numberOfDocuments;
	}
	
	/**
	 * Checks whether this dictionary contains the given word.
	 * 
	 * @param word word to check
	 * @return true if the given word exists in the dictionary, false otherwise
	 */
	public boolean contains(String word) {
		return words.contains(word);
	}
}
