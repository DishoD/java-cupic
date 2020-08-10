package hr.fer.zemris.java.p12.dao;


import java.util.List;

import hr.fer.zemris.java.p12.model.Poll;
import hr.fer.zemris.java.p12.model.PollOption;

/**
 * Sučelje prema podsustavu za perzistenciju podataka.
 * 
 * @author marcupic
 *
 */
public interface DAO {

	/**
	 * Returns all defined polls.
	 * 
	 * @return polls
	 */
	List<Poll> getPolls() throws DAOException;
	
	/**
	 * Gets all poll options for the given poll id.
	 * 
	 * @param pollId poll id of the poll options
	 * @return poll options
	 */
	List<PollOption> getPollOptions(long pollId) throws DAOException;
	
	/**
	 * Returns a poll of the given poll id.
	 * 
	 * @param pollId poll id
	 * @return requested poll if it exists, null otherwise
	 * @throws DAOException
	 */
	Poll getPollofID(long pollId) throws DAOException;
	
	/**
	 * Increment by one a votes of the poll option of the given id.
	 * 
	 * @param id poll option id
	 */
	void incerementPollOptionVotes(long id) throws DAOException;
	
	/**
	 * Returns a poll option  of the given poll option id.
	 * @param id poll option id
	 * @return poll option
	 * @throws DAOException
	 */
	PollOption getPollOptionofID(long id) throws DAOException;
	
}