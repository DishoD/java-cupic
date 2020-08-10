package hr.fer.zemris.java.hw11.jnotepadpp.local;

/**
 * Defines an interface for localization (internationalization) provider. It is
 * an observer model that notifies its registered listeners when the locale has
 * been changed. Components can fetch their localized texts from the dictionary
 * with a specific key.
 * 
 * @author Disho
 *
 */
public interface ILocalizationProvider {
	/**
	 * Register an localization listener to this provider that will be notified when
	 * the local has been changed.
	 * 
	 * @param l
	 *            listener to register
	 */
	void addLocalizationListener(ILocalizationListener l);

	/**
	 * Unregister an localization listener from this provider. No longer will be
	 * notified when the local has been changed.
	 * 
	 * @param l
	 *            listener to unregister
	 */
	void removeLocalizationListener(ILocalizationListener l);

	/**
	 * For the current locale of this provider you will be provided with the
	 * localized text for the given key.
	 * 
	 * @param key
	 *            text key
	 * @return localized text
	 */
	String getString(String key);
}
