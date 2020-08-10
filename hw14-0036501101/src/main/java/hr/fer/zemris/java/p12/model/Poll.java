package hr.fer.zemris.java.p12.model;

/**
 * Represents an poll entry in the database.
 * 
 * @author Disho
 *
 */
public class Poll {
	/**
	 * poll id
	 */
	public final long id;
	/**
	 * poll title
	 */
	public final String title;
	/**
	 * poll message
	 */
	public final String message;
	
	/**
	 * Initializes the poll with the given parameters.
	 * 
	 * @param id poll id
	 * @param title poll title
	 * @param message poll message
	 */
	public Poll(long id, String title, String message) {
		this.id = id;
		this.title = title;
		this.message = message;
	}

	/**
	 * @return the poll id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @return the poll title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @return the poll message
	 */
	public String getMessage() {
		return message;
	}
}
