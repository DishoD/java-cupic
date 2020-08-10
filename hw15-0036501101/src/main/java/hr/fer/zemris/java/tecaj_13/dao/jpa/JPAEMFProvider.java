package hr.fer.zemris.java.tecaj_13.dao.jpa;

import javax.persistence.EntityManagerFactory;

/**
 * JPA entity manager factory provider (EMF).
 * 
 * @author Disho
 *
 */
public class JPAEMFProvider {

	/**
	 * used EMF
	 */
	public static EntityManagerFactory emf;
	
	/**
	 * get EMF
	 * @return EMF
	 */
	public static EntityManagerFactory getEmf() {
		return emf;
	}
	
	/**
	 * Set EMF.
	 * 
	 * @param emf EMF
	 */
	public static void setEmf(EntityManagerFactory emf) {
		JPAEMFProvider.emf = emf;
	}
}