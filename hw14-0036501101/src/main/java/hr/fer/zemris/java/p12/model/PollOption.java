package hr.fer.zemris.java.p12.model;

/**
 * Represents a poll option entry in the database.
 * 
 * @author Disho
 *
 */
public class PollOption {
	/**
	 * poll option id
	 */
	public final Long id;
	/**
	 * poll option title
	 */
	public final String title;
	/**
	 * poll option link
	 */
	public final String link;
	/**
	 * poll id
	 */
	public final Long pollId;
	/**
	 * poll option votes count
	 */
	public final long votes;
	
	/**
	 * Initializes the poll option with the given parameters.
	 * 
	 * @param id poll option id
	 * @param title poll option title
	 * @param link poll option link
	 * @param pollId poll id
	 * @param votes poll option votes count
	 */
	public PollOption(Long id, String title, String link, Long pollId, long votes) {
		this.id = id;
		this.title = title;
		this.link = link;
		this.pollId = pollId;
		this.votes = votes;
	}

	/**
	 * @return the poll option id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @return the poll option title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @return the poll option link
	 */
	public String getLink() {
		return link;
	}

	/**
	 * @return the poll id
	 */
	public Long getPollId() {
		return pollId;
	}

	/**
	 * @return the poll option votes count
	 */
	public long getVotes() {
		return votes;
	}
	
	
}
