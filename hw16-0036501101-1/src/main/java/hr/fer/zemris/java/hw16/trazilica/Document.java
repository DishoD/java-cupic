package hr.fer.zemris.java.hw16.trazilica;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a document in a simple search engine.
 * Calculates vectors based on some dictionary for the document likeness calculation.
 * 
 * @author Disho
 *
 */
public class Document {
	/**
	 * name of the document, can be null
	 */
	private String name;
	/**
	 * frequency of words in the document
	 */
	
	private Map<String, Integer> wordFrequency = new HashMap<>();
	/**
	 * words in this document and dictionary
	 */
	private Set<String> words = new LinkedHashSet<>();
	/**
	 * binary vector of the document
	 */
	@SuppressWarnings("unused")
	private Vector binaryVec;
	/**
	 * frequency vector of the document
	 */
	@SuppressWarnings("unused")
	private Vector frequencyVec;
	/**
	 * TF-IDF vector of the document
	 */
	private Vector tfidfVec;
	
	/**
	 * Constructs a document with the given parameters.
	 * 
	 * @param name name of the document, can be null
	 * @param text text of the document
	 * @param dictionary dictionary for constructing vectors
	 */
	public Document(String name, String text, Dictionary dictionary) {
		Objects.requireNonNull(text, "text cannot be null");
		Objects.requireNonNull(dictionary, "dictionary cannot be null");
		
		this.name = name;
		
		SimpleWordParser parser = new SimpleWordParser(text);
		
		while(true) {
			String word = parser.getNextWord();
			if(word == null) break;
			word = word.toLowerCase();
			if(!dictionary.contains(word)) continue;
			
			wordFrequency.merge(word, 1, (o,n)-> o+n);
			words.add(word);
		}
		
		List<Double> binary = new ArrayList<>();
		List<Double> freq = new ArrayList<>();
		List<Double> tfidf = new ArrayList<>();
		
		for(String word : dictionary.getWords()) {
			Integer f = wordFrequency.get(word);
			f = f == null ? 0 : f;
			
			binary.add(f == 0 ? 0. : 1.);
			freq.add((double)f);
			tfidf.add(f*Math.log((double)dictionary.getNumberOfDocuments()/dictionary.getWordDocumentFrequency(word)));
		}
		
		binaryVec = new Vector(binary);
		frequencyVec = new Vector(freq);
		tfidfVec = new Vector(tfidf);
	}
	
	/**
	 * Gets a name of the document.
	 * 
	 * @return document name, or null if not given one
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the name of the document.
	 * 
	 * @param name name of the document, or null if you want unnamed document
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Returns a set of words of this document and dictionary.
	 * 
	 * @return words
	 */
	public Set<String> getWords() {
		return words;
	}
	
	/**
	 * Calculates a likeness of this document against some other document based
	 * on the TF-IDF vector comparison.
	 * 
	 * @param other document to compare to
	 * @return likeness factor in [0, 1]
	 */
	double likeness(Document other) {
		return this.tfidfVec.dotProduct(other.tfidfVec)/(this.tfidfVec.norm()*other.tfidfVec.norm());
	}
}
