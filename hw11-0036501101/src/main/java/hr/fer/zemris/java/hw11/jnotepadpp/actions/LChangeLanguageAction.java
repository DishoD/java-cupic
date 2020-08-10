package hr.fer.zemris.java.hw11.jnotepadpp.actions;

import java.awt.event.ActionEvent;

import hr.fer.zemris.java.hw11.jnotepadpp.local.ILocalizationProvider;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizableAction;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizationProvider;

/**
 * Action that changes the language of the application.
 * 
 * @author Disho
 *
 */
@SuppressWarnings("serial")
public class LChangeLanguageAction extends LocalizableAction {
	/**
	 * to what language will the application be changed to
	 */
	private String language;

	/**
	 * Initializes the action with the given parameters.
	 * 
	 * @param key localization key and language identifier
	 * @param localizationProvider localization provider
	 * @param keyStroke accelerator key
	 */
	public LChangeLanguageAction(String key, ILocalizationProvider localizationProvider, String keyStroke) {
		super(key, localizationProvider, keyStroke);
		
		this.language = key;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		LocalizationProvider.getInstance().setLanguage(language);
	}

}
