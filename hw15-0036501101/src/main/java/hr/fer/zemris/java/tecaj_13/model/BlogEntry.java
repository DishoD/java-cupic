package hr.fer.zemris.java.tecaj_13.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Represents a blog entry in the JPA database.
 * 
 * @author Disho
 *
 */
@Entity
@Table(name="blog_entries")
@Cacheable(true)
public class BlogEntry {

	/**
	 * unique entry id (primary key)
	 */
	private Long id;
	/**
	 * list of comments on this entry
	 */
	private List<BlogComment> comments = new ArrayList<>();
	/**
	 * date and time of the creation of this entry
	 */
	private Date createdAt;
	/**
	 * last modification date and time of this entry
	 */
	private Date lastModifiedAt;
	/**
	 * entry title
	 */
	private String title;
	/**
	 * entry text
	 */
	private String text;
	/**
	 * blog user that owns this entry
	 */
	private BlogUser creator;
	
	/**
	 * Gets unique entry id (primary key).
	 * @return id
	 */
	@Id @GeneratedValue
	public Long getId() {
		return id;
	}
	
	/**
	 * Sets unique entry id (primary key)
	 * @param id id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Gets list of comments on this entry.
	 * 
	 * @return comments
	 */
	@OneToMany(mappedBy="blogEntry",fetch=FetchType.LAZY, cascade=CascadeType.PERSIST, orphanRemoval=true)
	@OrderBy("postedOn")
	public List<BlogComment> getComments() {
		return comments;
	}
	
	/**
	 * sets list of comments on this entry.
	 * @param comments comments, can't be null
	 */
	public void setComments(List<BlogComment> comments) {
		this.comments = comments;
	}

	/**
	 * Gets date and time of the creation of this entry.
	 * 
	 * @return creation date and time
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable=false)
	public Date getCreatedAt() {
		return createdAt;
	}

	/**
	 * Sets date and time of the creation of this entry.
	 * 
	 * @param createdAt creation date and time, can't be null
	 */
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	/**
	 * Get last modification date and time of this entry.
	 * 
	 * @return last modification date and time
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable=true)
	public Date getLastModifiedAt() {
		return lastModifiedAt;
	}

	/**
	 * Sets last modification date and time of this entry.
	 * @param lastModifiedAt last modification date and time, can't be null
	 */
	public void setLastModifiedAt(Date lastModifiedAt) {
		this.lastModifiedAt = lastModifiedAt;
	}

	/**
	 * Gets entry title.
	 * @return entry title
	 */
	@Column(length=200,nullable=false)
	public String getTitle() {
		return title;
	}

	/**
	 * Sets entry title.
	 * @param title entry title, can't be null
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Gets entry text
	 * @return entry text
	 */
	@Column(length=4096,nullable=false)
	public String getText() {
		return text;
	}

	/**
	 * sets entry text
	 * @param text entry text, can't be null
	 */
	public void setText(String text) {
		this.text = text;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	
	/**
	 * Gets blog user that owns this entry.
	 * @return creator
	 */
	@ManyToOne
	public BlogUser getCreator() {
		return creator;
	}
	
	/**
	 * Sets blog user that owns this entry
	 * @param creator creator, can't be null
	 */
	public void setCreator(BlogUser creator) {
		this.creator = creator;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BlogEntry other = (BlogEntry) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}