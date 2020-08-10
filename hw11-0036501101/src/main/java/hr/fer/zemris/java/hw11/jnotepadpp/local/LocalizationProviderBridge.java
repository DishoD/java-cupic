package hr.fer.zemris.java.hw11.jnotepadpp.local;

/**
 * A localization provider that acts an bridge between
 * a real localization provider and its listeners. This bridge is
 * used for when you want to prevent memory leaks. When you loose 
 * references to the listeners, a real localization prover would still keep
 * those references which would prevent garbage collector from cleaning those objects.
 * This bridge allows connecting and disconnecting from the real localization provider
 * for when you want to loose or regain the connection to the real localization provider
 * as when you disconnect garbage collector can clean listeners.
 *  
 * @author Disho
 *
 */
public class LocalizationProviderBridge extends AbstractLocalizationProvider {
	/**
	 * a real localization provider
	 */
	private final ILocalizationProvider parent;
	/**
	 * connected flag - tells if we are connected to the real localization provider
	 */
	private boolean connected;
	
	/**
	 * Tracks the locale setting changes from the real localization provider
	 */
	private final ILocalizationListener parentListener = new ILocalizationListener() {
		
		@Override
		public void localizationChanged() {
			LocalizationProviderBridge.this.fire();
		}
	};
	

	/**
	 * Initializes the localization provider bridge with the given
	 * real localization provider and connects its self to that provider.
	 * 
	 * @param parent the real localization provider
	 */
	public LocalizationProviderBridge(ILocalizationProvider parent) {
		this.parent = parent;
		
		parent.addLocalizationListener(parentListener);
		connected = true;
	}
	
	/**
	 * Connect the bridge to the real localization provider.
	 * If connected, garbage collector can't remove the listeners.
	 * If already connected, nothing happens.
	 */
	public void connect() {
		if(connected) return;
		
		parent.addLocalizationListener(parentListener);
		connected = true;
	}
	
	/**
	 * Disconnect the bridge to the real localization provider.
	 * If disconnected, garbage collector can remove the listeners.
	 * If already disconnected, nothing happens.
	 */
	public void disconnect() {
		if(!connected) return;
		
		parent.removeLocalizationListener(parentListener);
		connected = false;
	}

	@Override
	public String getString(String key) {
		if(!connected) throw new IllegalStateException("The bridge is not connected.");
		
		return parent.getString(key);
	}
	
}
