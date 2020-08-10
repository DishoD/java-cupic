package hr.fer.zemris.java.tecaj_13.dao;

import java.util.List;

import hr.fer.zemris.java.tecaj_13.model.BlogComment;
import hr.fer.zemris.java.tecaj_13.model.BlogEntry;
import hr.fer.zemris.java.tecaj_13.model.BlogUser;

/**
 * Abstracts a communication with the database. Lists methods for blog database
 * communication.
 * 
 * @author Disho
 *
 */
public interface DAO {

	/**
	 * Gets blog entry of the given id.
	 * 
	 * @param id
	 *            entry id
	 * @return blog entry or null if it desn't exist
	 */
	BlogEntry getBlogEntry(Long id) throws DAOException;

	/**
	 * Returns a list of nicks of the blog users.
	 * 
	 * @return list of nicks
	 */
	List<String> getNicks() throws DAOException;

	/**
	 * Register a new user to the blog.
	 * 
	 * @param newUser
	 */
	void registerUser(BlogUser newUser);

	/**
	 * Gets a blog user of the given nick.
	 * 
	 * @param nick
	 *            blog user's nick
	 * @return blog user or null if one doesn't exist
	 */
	BlogUser getBlogUserOfNick(String nick);

	/**
	 * Adds a new blog entry of the given user with the current creation time and
	 * date.
	 * 
	 * @param user
	 *            to which user will this blog entry be added to
	 * @param title
	 *            blog title
	 * @param text
	 *            blog text
	 * @return generated blog entry
	 */
	BlogEntry addBlogEntryOfUser(BlogUser user, String title, String text);

	/**
	 * Adds a new comment with the current date and time to the given blog entry.
	 * 
	 * @param email
	 *            email of the user that commented
	 * @param message
	 *            comment message
	 * @param blogEntry
	 *            to which blog entry will this comment be added to
	 * @return generated blog comment
	 */
	BlogComment addCommentToBlogEntry(String email, String message, BlogEntry blogEntry);
}