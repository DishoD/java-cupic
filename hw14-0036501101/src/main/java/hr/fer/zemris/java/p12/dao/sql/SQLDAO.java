package hr.fer.zemris.java.p12.dao.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import hr.fer.zemris.java.p12.dao.DAO;
import hr.fer.zemris.java.p12.dao.DAOException;
import hr.fer.zemris.java.p12.model.Poll;
import hr.fer.zemris.java.p12.model.PollOption;

/**
 * Ovo je implementacija podsustava DAO uporabom tehnologije SQL. Ova konkretna
 * implementacija očekuje da joj veza stoji na raspolaganju preko
 * {@link SQLConnectionProvider} razreda, što znači da bi netko prije no što
 * izvođenje dođe do ove točke to trebao tamo postaviti. U web-aplikacijama
 * tipično rješenje je konfigurirati jedan filter koji će presresti pozive
 * servleta i prije toga ovdje ubaciti jednu vezu iz connection-poola, a po
 * zavrsetku obrade je maknuti.
 * 
 * @author marcupic
 */
public class SQLDAO implements DAO {

	@Override
	public List<Poll> getPolls() throws DAOException {
		Connection con = SQLConnectionProvider.getConnection();

		List<Poll> polls = new ArrayList<>();

		try (PreparedStatement ps = con.prepareStatement("select * from polls order by id");
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				polls.add(new Poll(rs.getLong(1), rs.getString(2), rs.getString(3)));
			}

		} catch (SQLException e) {
			throw new DAOException(e);
		}

		return polls;
	}

	@Override
	public List<PollOption> getPollOptions(long pollId) throws DAOException {
		Connection con = SQLConnectionProvider.getConnection();

		List<PollOption> polloptions = new ArrayList<>();
		
		Objects.requireNonNull(con, "con is null! how?");

		try (PreparedStatement ps = con.prepareStatement("select * from polloptions where pollid=" + pollId + " order by id");
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				polloptions.add(
						new PollOption(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getLong(4), rs.getLong(5)));
			}

		} catch (SQLException e) {
			throw new DAOException(e);
		}

		return polloptions;
	}

	@Override
	public Poll getPollofID(long pollId) throws DAOException {
		Connection con = SQLConnectionProvider.getConnection();

		Poll poll;

		try (PreparedStatement ps = con.prepareStatement("select * from polls where id = " + pollId);
				ResultSet rs = ps.executeQuery()) {

			if (!rs.next()) {
				poll = null;
			} else {
				poll = new Poll(rs.getLong(1), rs.getString(2), rs.getString(3));
			}

		} catch (SQLException e) {
			throw new DAOException(e);
		}

		return poll;
	}

	@Override
	public void incerementPollOptionVotes(long id) throws DAOException {
		Connection con = SQLConnectionProvider.getConnection();

		try (PreparedStatement ps = con
				.prepareStatement("UPDATE polloptions SET votesCount = votesCount + 1 where id = ?")) {
			ps.setLong(1, id);
			ps.execute();

		} catch (SQLException e) {
			throw new DAOException(e);
		}

	}

	@Override
	public PollOption getPollOptionofID(long id) throws DAOException {
		Connection con = SQLConnectionProvider.getConnection();

		PollOption pollOption;

		try (PreparedStatement ps = con.prepareStatement("select * from polloptions where id = " + id);
				ResultSet rs = ps.executeQuery()) {

			if (!rs.next()) {
				pollOption = null;
			} else {
				pollOption = new PollOption(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getLong(4), rs.getLong(5));
			}

		} catch (SQLException e) {
			throw new DAOException(e);
		}

		return pollOption;
	}

}