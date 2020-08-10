package hr.fer.zemris.oopj.hw17.galerija.DB;

/**
 * Stores information about some photograph.
 * 
 * @author Disho
 *
 */
public class PhotoInfo {
	/**
	 * a file name
	 */
	private String name;
	/**
	 * photo description
	 */
	private String description;
	/**
	 * tags that describe this photo
	 */
	private String[] tags;
	/**
	 * unique photo id
	 */
	private int id;
	/**
	 * image file extension
	 */
	private String imageType;
	
	/**
	 * Initializes the photo information with the given parameters.
	 * 
	 * @param name a file name
	 * @param description photo description
	 * @param tags tags that describe this photo
	 * @param id unique photo id
	 */
	public PhotoInfo(String name, String description, String[] tags, int id) {
		super();
		this.name = name;
		this.description = description;
		this.tags = tags;
		this.id = id;
		imageType = name.substring(name.lastIndexOf('.')+1);
	}
	
	/**
	 * Get a file name.
	 * 
	 * @return a file name
	 */
	public String getName() {
		return name;
	}
	/**
	 * Get photo description
	 * 
	 * @return photo description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * Get tags that describe this photo.
	 * 
	 * @return tags
	 */
	public String[] getTags() {
		return tags;
	}
	
	/**
	 * Get unique photo id
	 * 
	 * @return id
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Get image file extension
	 * 
	 * @return image type
	 */
	public String getImageType() {
		return imageType;
	}
}
