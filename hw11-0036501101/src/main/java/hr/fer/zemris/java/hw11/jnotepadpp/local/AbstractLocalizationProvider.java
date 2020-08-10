package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract implementation of the ILocalizationProvider that takes care
 * of registering, unregistering and notifying registered listeners.
 * 
 * @author Disho
 *
 */
public abstract class AbstractLocalizationProvider implements ILocalizationProvider {
	/**
	 * list of registered listeners
	 */
	private List<ILocalizationListener> listeners = new ArrayList<>();
	
	@Override
	public void addLocalizationListener(ILocalizationListener l) {
		listeners.add(l);
	}

	@Override
	public void removeLocalizationListener(ILocalizationListener l) {
		listeners.remove(l);
	}
	
	/**
	 * Notifies all registered listeners of the change in localization settings.
	 */
	public void fire() {
		for(ILocalizationListener listener : listeners) {
			listener.localizationChanged();
		}
	}

}
