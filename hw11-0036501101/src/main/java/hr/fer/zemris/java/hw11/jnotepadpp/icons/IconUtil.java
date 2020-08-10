package hr.fer.zemris.java.hw11.jnotepadpp.icons;

import java.io.IOException;
import java.io.InputStream;

import javax.swing.ImageIcon;

/**
 * Utility class for loading image icons.
 * 
 * @author Disho
 *
 */
public class IconUtil {
	
	/**
	 * image icon of a small green diskette
	 */
	public static final ImageIcon GREEN_DISKETTE = loadIcon("green-diskette.png");
	/**
	 * image icon of a small red diskette
	 */
	public static final ImageIcon RED_DISKETTE = loadIcon("red-diskette.png");
	
	private IconUtil() {}
	
	/**
	 * Loads icon from the file in /icons directory.
	 * 
	 * @param name name of the icon
	 * @return image icon
	 */
	public static ImageIcon loadIcon(String name) {
		InputStream is = IconUtil.class.getClassLoader().getResourceAsStream("icons/" + name);
		if(is==null) throw new IllegalArgumentException("File 'icons/" + name +"' doesn't exist or can't be loaded.");
		
		byte[] bytes = null;
		
		try {
			bytes = is.readAllBytes();
			is.close();
		} catch (IOException e) {
			throw new IllegalArgumentException("File 'icons/" + name +"' doesn't exist or can't be loaded.");
		}
		
		return new ImageIcon(bytes); 
	}
}
