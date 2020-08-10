package hr.fer.zemris.java.hw11.jnotepadpp.local;

import javax.swing.JMenu;

/**
 * Localizable JMenu.
 * 
 * @author Disho
 *
 */
public class LJMenu extends JMenu{

	private static final long serialVersionUID = 1L;
	
	/**
	 * Initializes the menu with the given parameters.
	 * 
	 * @param key localization key
	 * @param localizationProvider localization provider
	 */
	public LJMenu(String key, ILocalizationProvider localizationProvider) {
		setText(localizationProvider.getString(key));
		
		localizationProvider.addLocalizationListener(() -> setText(localizationProvider.getString(key)));
	}

}
