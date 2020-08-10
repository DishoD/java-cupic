package hr.fer.zemris.java.tecaj_13.dao.jpa;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import hr.fer.zemris.java.tecaj_13.dao.DAO;
import hr.fer.zemris.java.tecaj_13.dao.DAOException;
import hr.fer.zemris.java.tecaj_13.model.BlogComment;
import hr.fer.zemris.java.tecaj_13.model.BlogEntry;
import hr.fer.zemris.java.tecaj_13.model.BlogUser;

/**
 * DAO implementation that uses JPA.
 * 
 * @author Disho
 *
 */
public class JPADAOImpl implements DAO {

	@Override
	public BlogEntry getBlogEntry(Long id) throws DAOException {
		BlogEntry blogEntry = JPAEMProvider.getEntityManager().find(BlogEntry.class, id);
		return blogEntry;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getNicks() throws DAOException {
		return JPAEMProvider.getEntityManager().createNamedQuery("BlogUser.nicks").getResultList();
	}

	@Override
	public void registerUser(BlogUser newUser) {
		EntityManager em = JPAEMProvider.getEntityManager();
		em.persist(newUser);
	}

	@Override
	public BlogUser getBlogUserOfNick(String nick) {
		EntityManager em = JPAEMProvider.getEntityManager();
		
		Query query = em.createNamedQuery("BlogUser.userOfNick");
		query.setParameter("nick", nick);
		
		@SuppressWarnings("unchecked")
		List<BlogUser> users = query.getResultList();
		
		if(users.isEmpty()) return null;
		
		return users.get(0);
	}

	@Override
	public BlogEntry addBlogEntryOfUser(BlogUser user, String title, String text) {
		BlogEntry be = new BlogEntry();
		
		be.setTitle(title);
		be.setText(text);
		Date creation = new Date();
		be.setCreatedAt(creation);
		be.setLastModifiedAt(creation);
		be.setCreator(user);
		
		EntityManager em = JPAEMProvider.getEntityManager();
		em.persist(be);
		
		return be;
	}

	@Override
	public BlogComment addCommentToBlogEntry(String email, String message, BlogEntry blogEntry) {
		BlogComment comment = new BlogComment();
		
		comment.setBlogEntry(blogEntry);
		comment.setUsersEMail(email);
		comment.setMessage(message);
		comment.setPostedOn(new Date());
		
		EntityManager em = JPAEMProvider.getEntityManager();
		em.persist(comment);
		
		return comment;
	}

}