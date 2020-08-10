package hr.fer.zemris.java.tecaj_13.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Represents a blog comment in the JPA database.
 * 
 * @author Disho
 *
 */
@Entity
@Table(name="blog_comments")
public class BlogComment {

	/**
	 * comments unique id (primary key)
	 */
	private Long id;
	/**
	 * parent entry of this comment
	 */
	private BlogEntry blogEntry;
	/**
	 * users email
	 */
	private String usersEMail;
	/**
	 * comment message
	 */
	private String message;
	/**
	 * date and time of the creation of this comment
	 */
	private Date postedOn;
	
	/**
	 * Gets comments unique id (primary key).
	 * 
	 * @return id
	 */
	@Id @GeneratedValue
	public Long getId() {
		return id;
	}
	
	/**
	 * Sets comments unique id (primary key)
	 * @param id comments unique id (primary key), can't be null
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Gets parent entry of this comment
	 * @return blog entry
	 */
	@ManyToOne
	@JoinColumn(nullable=false)
	public BlogEntry getBlogEntry() {
		return blogEntry;
	}
	
	/**
	 * Sets parent entry of this comment
	 * @param blogEntry blog entry, can't be null
	 */
	public void setBlogEntry(BlogEntry blogEntry) {
		this.blogEntry = blogEntry;
	}

	/**
	 * Gets users email.
	 * 
	 * @return users email
	 */
	@Column(length=100,nullable=false)
	public String getUsersEMail() {
		return usersEMail;
	}

	/**
	 * Sets users email.
	 * 
	 * @param usersEMail users email, can't be null
	 */
	public void setUsersEMail(String usersEMail) {
		this.usersEMail = usersEMail;
	}

	/**
	 * GEts comment message.
	 * @return message
	 */
	@Column(length=4096,nullable=false)
	public String getMessage() {
		return message;
	}

	/**
	 * Sets comment message.
	 * 
	 * @param message comment message, can't be null
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Gets date and time of the creation of this comment.
	 * 
	 * @return date and time of the creation of this comment
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable=false)
	public Date getPostedOn() {
		return postedOn;
	}

	/**
	 * Sets date and time of the creation of this comment.
	 * 
	 * @param postedOn date and time of the creation of this comment, can't be null
	 */
	public void setPostedOn(Date postedOn) {
		this.postedOn = postedOn;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BlogComment other = (BlogComment) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}