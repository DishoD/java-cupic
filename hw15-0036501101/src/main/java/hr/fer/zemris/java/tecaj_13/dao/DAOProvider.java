package hr.fer.zemris.java.tecaj_13.dao;

import hr.fer.zemris.java.tecaj_13.dao.jpa.JPADAOImpl;

/**
 * DAO implementation provider. If you want to use DAO you should always get it from here.
 * 
 * @author Disho
 *
 */
public class DAOProvider {

	/**
	 * used DAO implementation
	 */
	private static DAO dao = new JPADAOImpl();
	
	/**
	 * Gets a DAO implementation.
	 * 
	 * @return DAO implementation
	 */
	public static DAO getDAO() {
		return dao;
	}
	
}