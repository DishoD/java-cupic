package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

/**
 * A LocalizationproviderBridge that acs as an bridge between real localization
 * provider and some JFrame. When the window is opened, the bridge is
 * automatically connected and when the window is closed, the bridge is
 * automatically disconnected from the real localization provider.
 * 
 * @author Disho
 *
 */
public class FormLocalizationProvider extends LocalizationProviderBridge {

	/**
	 * Initializes the object. Creates an bridge between
	 * the real localization prover and the given JFrame.
	 * 
	 * @param parent the real localization provider
	 * @param window the JFrame
	 */
	public FormLocalizationProvider(ILocalizationProvider parent, JFrame window) {
		super(parent);

		window.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosed(WindowEvent e) {
				FormLocalizationProvider.this.disconnect();
			}

			@Override
			public void windowOpened(WindowEvent e) {
				FormLocalizationProvider.this.connect();
			}
		});
	}

}
