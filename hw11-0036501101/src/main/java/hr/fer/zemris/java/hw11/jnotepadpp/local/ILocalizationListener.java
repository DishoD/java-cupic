package hr.fer.zemris.java.hw11.jnotepadpp.local;

/**
 * Defines an interface for the listeners in observer model that
 * track when the localization settings has been changed.
 * 
 * @author Disho
 *
 */
public interface ILocalizationListener {
	/**
	 * This method is to be called when the localization settings has been changed.
	 */
	void localizationChanged();
}
