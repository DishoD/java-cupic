package hr.fer.zemris.java.hw16.trazilica;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * A simple command line application that compares likeness of the given query
 * against some documents. Path to the documents directory is given through the command
 * line arguments.
 * 
 * @author Disho
 *
 */
public class Konzola {
	/**
	 * stop words
	 */
	private static Set<String> stopWords;
	/**
	 * dictionary
	 */
	private static Dictionary dictionary;
	/**
	 * list of documents
	 */
	private static List<Document> documents = new ArrayList<>();
	/**
	 * query result list
	 */
	private static List<QueryResultEntry> results;
	
	/**
	 * Main method. Controls the flow of the document
	 * 
	 * @param args command-line arguments
	 */
	public static void main(String[] args) {
		if(args.length != 1) {
			System.out.println("Application expects one argument. A path to a directory of documents.");
			System.exit(0);
		}
		
		Path path = Paths.get(args[0]);
		if(!Files.isDirectory(path)) {
			System.out.println("Given path is not a directory.");
			System.exit(0);
		}
		
		try {
			stopWords = loadStopWords("src/main/resources/hrvatski_stoprijeci.txt");
			dictionary = new Dictionary(path, stopWords);
			
			System.out.println("Veličina rječnika je "+ dictionary.getNumberOfWords() + " riječi.");
			
			Files.list(path).filter(Files::isReadable).forEach(p -> {
				try {
					String text = new String(Files.readAllBytes(p), StandardCharsets.UTF_8);
					documents.add(new Document(p.toAbsolutePath().toString(), text, dictionary));
				} catch (IOException e) {
					System.out.println("Error ocurred in application initialization.");
					System.exit(0);
				}
			});
		} catch (IOException e) {
			System.out.println("Error ocurred in application initialization.");
			System.exit(0);
		}
		
		Scanner sc = new Scanner(System.in);
		while(true) {
			System.out.print("Enter command > ");
			String command = sc.nextLine().trim();
			
			if(command.equalsIgnoreCase("exit")) {
				break;
			}
			
			if(command.startsWith("query")) {
				String argument = command.substring(5).trim();
				if(argument.isEmpty()) {
					System.out.println("Naredba mora primiti neki tekst kao argument.");
					continue;
				}
				
				results = query(argument);
				System.out.println("Najboljih 10 rezultata:");
				for(int i = 0; i < 10 && i < results.size(); ++i) {
					System.out.println("[" + i +"] " + results.get(i));
				}
				
				System.out.println();
				continue;
			}
			
			if(command.startsWith("type")) {
				type(command.substring(4));
				continue;
			}
			
			if(command.startsWith("results")) {
				if(results == null) {
					System.out.println("Morate prvo pozvati 'query' naredbu da bi ste pozvali ovu naredbu.\n");
					continue;
				}
				
				for(int i = 0; i < results.size(); ++i) {
					System.out.println("[" + i +"] " + results.get(i));
				}
				System.out.println();
				continue;
			}
			
			System.out.println("Nepoznata naredba.\n");
		}
		
		sc.close();
	}

	/**
	 * Executes the type command with the given argument. Prints the document content to the screen.
	 * The document is selected as index of the result of the query.
	 * 
	 * @param argument string representation of the index
	 */
	private static void type(String argument) {
		int n;
		try {
			n = Integer.parseInt(argument.trim());
		} catch(NumberFormatException e) {
			System.out.println("Ilegalni argument. Ova naredba očekije cijeli broj.\n");
			return;
		}
		
		if(results == null) {
			System.out.println("Morate prvo pozvati 'query' naredbu da bi ste pozvali ovu naredbu.\n");
			return;
		}
		
		if(n < 0 || n >= results.size()) {
			System.out.println("Indeks izvan dosega. Pozovite naredbu 'results' da bi ste vidjeli popis dozvoljenih indeksa.");
			return;
		}
		
		Path path = Paths.get(results.get(n).getName());
		System.out.println("Dokument: " + path);
		try {
			System.out.println(new String(Files.readAllBytes(path), StandardCharsets.UTF_8));
			System.out.println();
		} catch (IOException e) {
			System.out.println("Greška se dogodila prilikom čitanja datoteke.");
		}
	}

	/**
	 * Executes the query command with the given text argument.
	 * 
	 * @param text text to query
	 * @return list of results sorted from the best
	 */
	private static List<QueryResultEntry> query(String text) {
		Document doc = new Document(null, text, dictionary);
		System.out.println("Query is: " + doc.getWords());
		
		List<QueryResultEntry> res = new ArrayList<>();
		
		for(Document d : documents) {
			double likeness = doc.likeness(d);
			if(likeness > 1e-6) {
				res.add(new QueryResultEntry(likeness, d.getName()));
			}
		}
		
		Collections.sort(res);
		
		return res;
	}

	/**
	 * Loads the stop words from the given document.
	 * 
	 * @param document path to a document with the stop words
	 * @return set of stop words
	 * @throws IOException
	 */
	private static Set<String> loadStopWords(String document) throws IOException {
		Path path = Paths.get(document);
		Set<String> stopWords = new HashSet<>();
		
		Files.readAllLines(path, StandardCharsets.UTF_8).forEach(l -> stopWords.add(l));
		return stopWords;
	}
	
	/**
	 * Represents a query result entry.
	 * 
	 * @author Disho
	 *
	 */
	private static class QueryResultEntry implements Comparable<QueryResultEntry>{
		/**
		 * likeness factor of the query against some document
		 */
		private double likeness;
		/**
		 * documents name
		 */
		private String name;
		
		/**
		 * Initializes the entry with the given parameters.
		 * 
		 * @param likeness likeness factor of the query against some document
		 * @param name documents name
		 */
		public QueryResultEntry(double likeness, String name) {
			this.likeness = likeness;
			this.name = name;
		}

		/**
		 * Get entry likeness.
		 * 
		 * @return likeness
		 */
		@SuppressWarnings("unused")
		public double getLikeness() {
			return likeness;
		}
		
		/**
		 * Get entry's document name.
		 * 
		 * @return document name
		 */
		public String getName() {
			return name;
		}
		
		@Override
		public String toString() {
			return String.format("(%.4f) %s", likeness, name);
		}

		@Override
		public int compareTo(QueryResultEntry other) {
			return Double.compare(other.likeness, this.likeness);
		}
	}
}
