package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Singleton implementation of the ILocalizationProvider.
 * To get an instance of this class call getInstance() method.
 * Provides additional methods for getting and setting current language of this
 * localization provider. Default local is English.
 * 
 * @author Disho
 *
 */
public class LocalizationProvider extends AbstractLocalizationProvider {
	/**
	 * current language
	 */
	private String language;
	/**
	 * resource bundle with the translations
	 */
	private ResourceBundle bundle;
	/**
	 * single instance of this class
	 */
	private static LocalizationProvider instance = new LocalizationProvider();
	
	private List<String> lista = new ArrayList<>();
	
	/**
	 * Initializes the localization provider with the resource bundle
	 * and sets default language as English.
	 */
	private LocalizationProvider() {
		language = "en";
		Locale locale = Locale.forLanguageTag(language);
		bundle = ResourceBundle.getBundle("translation", locale);
		
		lista.add("Leona");
		lista.add("Bejbs");
		lista.add("Leona!!!");
		lista.add("Leona?");
		lista.add("lil monkey");
		lista.add("Žajmun");
		lista.add("cikla");
		lista.add("Trnoružica");
	}
	
	/**
	 * As this class is an singleton, to get an instance of
	 * this class call this method.
	 * 
	 * @return instance of the localization provider
	 */
	public static LocalizationProvider getInstance() {
		return instance;
	}
	
	@Override
	public String getString(String key) {
		
		if(language.equals("leona")) {
			return lista.get(ThreadLocalRandom.current().nextInt(lista.size()));
		}
		
		return bundle.getString(key);
	}
	
	/**
	 * Gets the current language of this localization provider.
	 * 
	 * @return current language
	 */
	public String getCurrentLanguage() {
		return language;
	}
	
	/**
	 * Sets (changes) the current language of this localization provider.
	 */
	public void setLanguage(String language) {
		if(language.equals(this.language)) return;
		
		if(language.equals("leona")) {
			this.language = language;
			fire();
			return;
		}
		
		this.language = language;
		Locale locale = Locale.forLanguageTag(language);
		bundle = ResourceBundle.getBundle("translation", locale);
		
		fire();
	}
}
