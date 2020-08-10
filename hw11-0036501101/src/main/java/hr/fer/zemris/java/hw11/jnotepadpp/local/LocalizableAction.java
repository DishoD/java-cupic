package hr.fer.zemris.java.hw11.jnotepadpp.local;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

/**
 * An AbstractAction with the localization (internationalization) abilities.
 * 
 * @author Disho
 *
 */
public abstract class LocalizableAction extends AbstractAction{
	private static final long serialVersionUID = 1L;
	
	/**
	 * Initializes an abstract localizable action with the given parameters.
	 * Actions will have an short description for keys with '.tt' suffix.
	 * 
	 * @param key localization key
	 * @param localizationProvider localization provider
	 * @param keyStroke accelerator key (can be null)
	 */
	public LocalizableAction(String key, ILocalizationProvider localizationProvider, String keyStroke) {
		this.putValue(NAME, localizationProvider.getString(key));
		this.putValue(SHORT_DESCRIPTION, localizationProvider.getString(key+".tt"));
		this.putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(keyStroke));
		
		localizationProvider.addLocalizationListener(() -> {
			this.putValue(NAME, localizationProvider.getString(key));
			this.putValue(SHORT_DESCRIPTION, localizationProvider.getString(key+".tt"));
		});
	}
	
	
}
