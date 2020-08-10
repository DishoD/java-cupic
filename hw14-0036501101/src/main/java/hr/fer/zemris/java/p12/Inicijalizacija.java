package hr.fer.zemris.java.p12;

import java.beans.PropertyVetoException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;

import hr.fer.zemris.java.p12.model.PollOption;

/**
 * Sets up the database and database pool upon the server initialization
 * and cleans up after the server destruction.
 * 
 * @author Disho
 *
 */
@WebListener
public class Inicijalizacija implements ServletContextListener {
	/**
	 * SQL statement that creates the POLLS table
	 */
	private static final String CREATE_TABLE_POLLS = "CREATE TABLE Polls " + 
													" (id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY," + 
													" title VARCHAR(150) NOT NULL," + 
													" message CLOB(2048) NOT NULL" + 
													")";
	/**
	 * SQL statement that creates the POLLOPTIONS table
	 */
	private static final String CREATE_TABLE_POLLOPTIONS = "CREATE TABLE PollOptions " + 
															" (id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY," + 
															" optionTitle VARCHAR(100) NOT NULL," + 
															" optionLink VARCHAR(150) NOT NULL," + 
															" pollID BIGINT," + 
															" votesCount BIGINT," + 
															" FOREIGN KEY (pollID) REFERENCES Polls(id)" + 
															")";
	
	/**
	 * SQL statement that populates the POLLS table
	 */
	private static final String INSERT_INTO_POLLS = "INSERT INTO POLLS (title, message) values "
			+ "('Glasanje za omiljeni bend', 'Od sljedećih bendova, koji Vam je bend najdraži? Kliknite na link kako biste glasali!'),"
			+ "('Glasanje za omiljenog filmskog redatelja', 'Od sljedećih redatelja, koji Vam je najdraži? Kliknite na link kako biste glasali!')";
	
	/**
	 * poll options of the first poll
	 */
	private static final List<PollOption> poll1Options;
	/**
	 * poll options of the second poll
	 */
	private static final List<PollOption> poll2Options;
	
	static {
		poll1Options = new ArrayList<>();
		poll1Options.add(new PollOption(null, "The Beatles", "https://www.youtube.com/watch?v=z9ypq6_5bsg", null, 0));
		poll1Options.add(new PollOption(null, "The Platters", "https://www.youtube.com/watch?v=H2di83WAOhU", null, 0));
		poll1Options.add(new PollOption(null, "The Beach Boys", "https://www.youtube.com/watch?v=2s4slliAtQU", null, 0));
		poll1Options.add(new PollOption(null, "The Four Seasons", "https://www.youtube.com/watch?v=y8yvnqHmFds", null, 0));
		poll1Options.add(new PollOption(null, "The Marcels", "https://www.youtube.com/watch?v=qoi3TH59ZEs", null, 0));
		poll1Options.add(new PollOption(null, "The Everly Brothers", "https://www.youtube.com/watch?v=tbU3zdAgiX8", null, 0));
		poll1Options.add(new PollOption(null, "The Mamas And The Papas", "https://www.youtube.com/watch?v=N-aK6JnyFmk", null, 0));
		
		poll2Options = new ArrayList<>();
		poll2Options.add(new PollOption(null, "Stanley Kubrick", "https://www.imdb.com/name/nm0000040/", null, 0));
		poll2Options.add(new PollOption(null, "Quentin Tarantino", "https://www.imdb.com/name/nm0000233/", null, 0));
		poll2Options.add(new PollOption(null, "David Lynch", "https://www.imdb.com/name/nm0000186", null, 0));
		poll2Options.add(new PollOption(null, "Edgar Wright", "https://www.imdb.com/name/nm0942367", null, 0));
		poll2Options.add(new PollOption(null, "Martin Scorsese", "https://www.imdb.com/name/nm0000217", null, 0));
		poll2Options.add(new PollOption(null, "David Fincher", "https://www.imdb.com/name/nm0000399", null, 0));
		poll2Options.add(new PollOption(null, "Darren Aranofsky", "https://www.imdb.com/name/nm0004716", null, 0));
	}
	
	/**
	 * Creates POLLS and POLLOPTIONS tables if they don't already exist.
	 * 
	 * @param data data source of the connection for the database
	 * @throws SQLException
	 */
	private void createTablesIfNotExist(DataSource data) throws SQLException {
		Connection con = data.getConnection();
		
		DatabaseMetaData metadata = con.getMetaData();
		try(ResultSet rs = metadata.getTables(null, "IVICA", "POLLS", null)) {
			if(!rs.next()) {
				try(PreparedStatement ps = con.prepareStatement(CREATE_TABLE_POLLS)) {
					ps.execute();
				}
			}
		}
		
		try(ResultSet rs = metadata.getTables(null, "IVICA", "POLLOPTIONS", null)) {
			if(!rs.next()) {
				try(PreparedStatement ps = con.prepareStatement(CREATE_TABLE_POLLOPTIONS)) {
					ps.execute();
				}
			}
		}
		
		con.close();
	}
	
	/**
	 * Populates the POLLS and POLLOPTIONS tables with the initial data if
	 * these tables are empty.
	 * 
	 * @param data data source of the connection for the database
	 * @throws SQLException
	 */
	private void populateTables(DataSource data) throws SQLException {
		Connection con = data.getConnection();
		
		try(PreparedStatement ps = con.prepareStatement("select * from polls");
			ResultSet rs = ps.executeQuery()) {
			
			if(!rs.next()) {
				try(PreparedStatement ps2 = con.prepareStatement(INSERT_INTO_POLLS)) {
					ps2.execute();
				}
			}
		}
		
		try(PreparedStatement ps = con.prepareStatement("select * from polloptions");
			ResultSet rs = ps.executeQuery()) {
				
				if(!rs.next()) {
					PreparedStatement ps2 = con.prepareStatement("select id from polls order by id");
					ResultSet keys = ps2.executeQuery();
					
					keys.next();
					long id = keys.getLong(1);
					
					for(PollOption option : poll1Options) {
						PreparedStatement ps3 = con.prepareStatement(
								"INSERT INTO polloptions (optionTitle, optionLink, pollID, votesCount) values (?,?,?,?)"
						);
						
						ps3.setString(1, option.title);
						ps3.setString(2, option.link);
						ps3.setLong(3, id);
						ps3.setLong(4, option.votes);
						
						ps3.execute();
						
						ps3.close();
					}
					
					keys.next();
					id = keys.getLong(1);
					
					for(PollOption option : poll2Options) {
						PreparedStatement ps3 = con.prepareStatement(
								"INSERT INTO polloptions (optionTitle, optionLink, pollID, votesCount) values (?,?,?,?)"
						);
						
						ps3.setString(1, option.title);
						ps3.setString(2, option.link);
						ps3.setLong(3, id);
						ps3.setLong(4, option.votes);
						
						ps3.execute();
						
						ps3.close();
					}
					
					keys.close();
					ps2.close();
				}
			}
		
		con.close();
	}
	

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		
		String host;
		String port;
		String dbName; 
		String user;
		String password;
		String connectionURL;
		
		Path path = Paths.get(sce.getServletContext().getRealPath("WEB-INF/dbsettings.properties"));
		Properties properties = new Properties();
		try {
			properties.load(Files.newInputStream(path));
			host = properties.getProperty("host");
			port = properties.getProperty("port");
			dbName = properties.getProperty("name");
			user = properties.getProperty("user");
			password = properties.getProperty("password");
			
			Objects.requireNonNull(host, "propertie host not found in WEB-INF/dbsettings.properties");
			Objects.requireNonNull(port , "propertie port not found in WEB-INF/dbsettings.properties");
			Objects.requireNonNull(dbName, "propertie name not found in WEB-INF/dbsettings.properties");
			Objects.requireNonNull(user, "propertie user not found in WEB-INF/dbsettings.properties");
			Objects.requireNonNull(password, "propertie password not found in WEB-INF/dbsettings.properties");
			
			connectionURL ="jdbc:derby://" + host + ":" + port + "/" + dbName + ";user=" + user + ";password=" + password;
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
		
		ComboPooledDataSource cpds = new ComboPooledDataSource();
		try {
			cpds.setDriverClass("org.apache.derby.jdbc.ClientDriver");
		} catch (PropertyVetoException e1) {
			throw new RuntimeException("Pogreška prilikom inicijalizacije poola.", e1);
		}
		cpds.setJdbcUrl(connectionURL);
		
		try {
			createTablesIfNotExist(cpds);
			populateTables(cpds);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		sce.getServletContext().setAttribute("hr.fer.zemris.dbpool", cpds);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		ComboPooledDataSource cpds = (ComboPooledDataSource)sce.getServletContext().getAttribute("hr.fer.zemris.dbpool");
		if(cpds!=null) {
			try {
				DataSources.destroy(cpds);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}