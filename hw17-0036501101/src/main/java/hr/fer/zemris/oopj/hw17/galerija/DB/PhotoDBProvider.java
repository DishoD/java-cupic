package hr.fer.zemris.oopj.hw17.galerija.DB;

/**
 * A PhotoDB provider.
 * 
 * @author Disho
 *
 */
public class PhotoDBProvider {
	/**
	 * current PhotoDB instance
	 */
	private static PhotoDB photoDB;
	
	/**
	 * this class is meant only for using the static methods
	 */
	private PhotoDBProvider() {}

	/**
	 * Get PhotoDB
	 * 
	 * @return photoDB
	 */
	public static PhotoDB getPhotoDB() {
		return photoDB;
	}

	/**
	 * Set PhotoDB
	 * @param photoDB photoDB to set
	 */
	public static void setPhotoDB(PhotoDB photoDB) {
		PhotoDBProvider.photoDB = photoDB;
	}
	
	
}
