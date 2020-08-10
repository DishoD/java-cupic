package hr.fer.zemris.java.tecaj_13.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

/**
 * Represents a blog user in the JPA database.
 * 
 * @author Disho
 *
 */
@NamedQueries({
	@NamedQuery(name="BlogUser.nicks",query="select user.nick from BlogUser as user"),
	@NamedQuery(name="BlogUser.userOfNick",query="select user from BlogUser as user where user.nick=:nick")
})
@Entity
@Table(name="blog_users")
public class BlogUser {
	/**
	 * unique blog user id (primary key)
	 */
	private Long id;
	/**
	 * blog users first name
	 */
	private String firstName;
	/**
	 * blog users last name
	 */
	private String lastName;
	/**
	 * blog users nickname
	 */
	private String nick;
	/**
	 * blog users e-mail
	 */
	private String email;
	/**
	 * password hash of the user
	 */
	private String passwordHash;
	/**
	 * list of users blog entries
	 */
	private List<BlogEntry> blogEntries = new ArrayList<>();
	
	/**
	 * Gets unique blog user id (primary key).
	 * @return the id
	 */
	@Id @GeneratedValue
	public Long getId() {
		return id;
	}
	/**
	 * Sets unique blog user id (primary key)
	 * @param id the id to set, can't be null
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * GEts blog users first name.
	 * 
	 * @return the firstName
	 */
	@Column(length=20, nullable=false)
	public String getFirstName() {
		return firstName;
	}
	/**
	 * Sets blog users first name
	 * @param firstName the first name to set, can't be null
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/**
	 * Gets blog users last name.
	 * @return the last name
	 */
	@Column(length=20, nullable=false)
	public String getLastName() {
		return lastName;
	}
	/**
	 * Sets blog users last name.
	 * @param lastName blog users last name, can't be null
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	/**
	 * Gets blog users nickname
	 * @return the nick
	 */
	@Column(length=20, nullable=false, unique=true)
	public String getNick() {
		return nick;
	}
	/**
	 * Sets blog users nickname
	 * @param nick blog users nickname, can't be null
	 */
	public void setNick(String nick) {
		this.nick = nick;
	}
	/**
	 * Gets blog users e-mail
	 * 
	 * @return the email
	 */
	@Column(length=50, nullable=false)
	public String getEmail() {
		return email;
	}
	/**
	 * Sets blog users e-mail.
	 * @param email blog users e-mail, can't be null
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * Gets password hash of the user.
	 * @return password hash of the user
	 */
	@Column(length=40, nullable=false)
	public String getPasswordHash() {
		return passwordHash;
	}
	/**
	 * Sets password hash of the user
	 * @param passwordHash password hash of the user, can't be null
	 */
	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}
	/**
	 * Gets list of users blog entries
	 * @return list of users blog entries
	 */
	@OneToMany(mappedBy="creator", fetch=FetchType.LAZY)
	@OrderBy("createdAt")
	public List<BlogEntry> getBlogEntries() {
		return blogEntries;
	}
	/**
	 * Sets list of users blog entries.
	 * @param blogEntries list of users blog entries, can't be null
	 */
	public void setBlogEntries(List<BlogEntry> blogEntries) {
		this.blogEntries = blogEntries;
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
		BlogUser other = (BlogUser) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
