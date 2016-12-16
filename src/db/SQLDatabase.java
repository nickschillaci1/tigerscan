package db;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

/**
 * Database object containing the SQL database connection and information regarding how to interact with it
 * @author Nick Schillaci, Brandon Dixon
 *
 */
public class SQLDatabase {

	private final double INITIAL_AVERAGE_PROBABILITY = 50;
	private final double INITIAL_AVERAGE_ANY_CONFIDENTIAL = 50;
	//any changes to the above values will require you to delete and re-create the database, or old words will not be updated
	
	private String databaseFileName;
	private Connection c = null;
	private Statement stmt = null;
	
	/**
	 * Create SQLDatabase object and initialize connection to the database file
	 * @throws SQLException 
	 */
	public SQLDatabase(String databaseFileName) throws SQLException {
		this.databaseFileName = databaseFileName;
	    this.initConnection();
	}
	
	/**
	 * Initialize connection to the SQL Database
	 * @throws SQLException 
	 */
	public void initConnection() throws SQLException {
		try {
    		Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:" + databaseFileName);
			c.setAutoCommit(false);
			this.initTable();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Closes connection to the SQLDatabase
	 */
	public void closeConnection() {
		try {
	    	c.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	/**
	 * Ensures that the database file has the proper SQL table for storing terms.
	 * If a file is not empty, but the table is invalid or missing, the database is considered corrupted and should be removed for the program to recreate it 
	 * @throws SQLException
	 */
	public void initTable() throws SQLException {
		try {
			BufferedReader br = new BufferedReader(new FileReader(databaseFileName));
			if (br.readLine() == null) {
				stmt = c.createStatement();
		    	String sql = "CREATE TABLE TERMS " +
		    				 "(TERM TEXT, " +
		    				 "SCORE INTEGER NOT NULL," +
		    				 "EMAILSIN INTEGER NOT NULL," +
		    				 "EMAILSNOTIN INTEGER NOT NULL," +
		    				 "AVGPROB REAL NOT NULL," +
		    				 "PROBCONF REAL NOT NULL)";
		    	stmt.executeUpdate(sql);
		    	stmt.close();
				c.commit();
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Performs a SQL INSERT operation on the database
	 * @param String term to insert
	 * @param int score to assign to the term
	 * @throws SQLException
	 */
	public void addTerm(String term, int score) throws SQLException {
    	stmt = c.createStatement();
    	String sql = "INSERT INTO TERMS (TERM,SCORE,EMAILSIN,EMAILSNOTIN,AVGPROB,PROBCONF) " +
    				 "VALUES (\'" 
    			+ term + "\', "
    			+ score + ", "
    			+ 0 + ", "
    			+ 0 + ", "
    			+ INITIAL_AVERAGE_PROBABILITY + ", "
    			+ INITIAL_AVERAGE_ANY_CONFIDENTIAL +");";
    	stmt.executeUpdate(sql);
    	stmt.close();
		c.commit();
	}
	
	/**
	 * Performs a SQL DELETE operation on the database
	 * @param String term to remove
	 * @throws SQLException
	 */
	public void removeTerm(String term) throws SQLException {
		stmt = c.createStatement();
		String sql = "DELETE FROM TERMS WHERE TERM=\'" + term + "\';";
		stmt.executeUpdate(sql);
		stmt.close();
		c.commit();
	}
	
	/**
	 * Performs a SQL DELETE operation on the database (removes all terms). Maintains the table and columns
	 * (SQLite doesn't support DELETE * or TRUNCATE)
	 * @throws SQLException
	 */
	public void removeAll() throws SQLException {
		stmt = c.createStatement();
		String sql = "DELETE FROM TERMS;";
		stmt.executeUpdate(sql);
		stmt.close();
		c.commit();
	}
	
	/**
	 * Performs a SQL UPDATE operation on the database to change a term's score
	 * @param String term to change score of
	 * @param int score to change previous score to
	 * @throws SQLException
	 */
	public void changeScore(String term, int score) throws SQLException {
		stmt = c.createStatement();
		String sql = "UPDATE TERMS SET SCORE = " + score + " WHERE TERM='" + term + "';";
		stmt.executeUpdate(sql);
		stmt.close();
		c.commit();
	}
	
	/**
	 * Performs a SQL SELECT operation to query the database for all terms and scores
	 * @return HashMap<%TERM%,%SCORE%> of all terms in the database
	 * @throws SQLException 
	 */
	public HashMap<String,Integer> getTerms() throws SQLException {
		HashMap<String,Integer> terms = new HashMap<String,Integer>();
		stmt = c.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * FROM TERMS;");
		while(rs.next()) { //loop through entries in the database
			terms.put(rs.getString("TERM"), rs.getInt("SCORE"));
		}
		rs.close();
		stmt.close();
		c.commit();
		return terms;
	}
	
	/**
	 * Get the number of emails a term has been found in (since it was added)
	 * @param term
	 * @return frequency of the term
	 * @throws SQLException
	 */
	public int getNumbEmailsIn(String term) throws SQLException {
		int freq = 0;
		stmt = c.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * FROM TERMS WHERE TERM='" + term + "';");
		while(rs.next()) {
			freq = rs.getInt("EMAILSIN");
		}
		rs.close();
		stmt.close();
		c.commit();
		return freq;
	}
	
	/**
	 * Get the number of emails a term has not been found in (since the term as added)
	 * @param term
	 * @return frequency of the term
	 * @throws SQLException
	 */
	public int getNumbEmailsNotIn(String term) throws SQLException {
		int freq = 0;
		stmt = c.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * FROM TERMS WHERE TERM='" + term + "';");
		while(rs.next()) {
			freq = rs.getInt("EMAILSNOTIN");
		}
		rs.close();
		stmt.close();
		c.commit();
		return freq;
	}
	
	
	/**
	 * Increment the number of emails a word was found in
	 * @param int term to increment frequency of
	 * @throws SQLException
	 */
	public void incrementNumbEmailsIn(String term) throws SQLException {
		int freq = this.getNumbEmailsIn(term);
		stmt = c.createStatement();
		String sql = "UPDATE TERMS SET EMAILSIN = " + (++freq) + " WHERE TERM='" + term + "';";
		stmt.executeUpdate(sql);
		stmt.close();
		c.commit();
	}
	
	/**
	 * Increment the number of emails a word was found in
	 * @param int term to increment frequency of
	 * @throws SQLException
	 */
	public void incrementNumbEmailsNotIn(String term) throws SQLException {
		int freq = this.getNumbEmailsNotIn(term);
		stmt = c.createStatement();
		String sql = "UPDATE TERMS SET EMAILSNOTIN = " + (++freq) + " WHERE TERM='" + term + "';";
		stmt.executeUpdate(sql);
		stmt.close();
		c.commit();
	}
	
	/**
	 * Get the average probability of a specified term
	 * @param term to get probability of
	 * @return probability of the term
	 * @throws SQLException
	 */
	public double getAverageProbability(String term) throws SQLException {
		double prob = 0;
		stmt = c.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * FROM TERMS WHERE TERM='" + term + "';");
		while(rs.next()) {
			prob = rs.getDouble("AVGPROB");
		}
		rs.close();
		stmt.close();
		c.commit();
		return prob;
	}
	
	/**
	 * Set the new average probability of a specified term after calculation
	 * @param term to set probability for
	 * @param prob new average probability to set for the term
	 * @throws SQLException
	 */
	public void setAverageProbability(String term, double prob) throws SQLException {
		stmt = c.createStatement();
		String sql = "UPDATE TERMS SET AVGPROB = " + prob + " WHERE TERM='" + term + "';";
		stmt.executeUpdate(sql);
		stmt.close();
		c.commit();
	}
	
	/**
	 * Get the probability any email is confidential based on a term
	 * @param term to get probability of
	 * @return probability of the term
	 * @throws SQLException
	 */
	public double getProbabilityAny(String term) throws SQLException {
		double prob = 0;
		stmt = c.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * FROM TERMS WHERE TERM='" + term + "';");
		while(rs.next()) {
			prob = rs.getDouble("PROBCONF");
		}
		rs.close();
		stmt.close();
		c.commit();
		return prob;
	}
	
	/**
	 * Set the new average probability of a specified term after calculation
	 * @param term to set probability for
	 * @param prob new average probability to set for the term
	 * @throws SQLException
	 */
	public void setProbabilityAny(String term, double prob) throws SQLException {
		stmt = c.createStatement();
		String sql = "UPDATE TERMS SET PROBCONF = " + prob + " WHERE TERM='" + term + "';";
		stmt.executeUpdate(sql);
		stmt.close();
		c.commit();
	}
	
	
	/**
	 * @return the file name of the database
	 */
	public String getDatabaseFileName() {
		return databaseFileName;
	}
	
	/**
	 * Set the file name of the database and re-initialize connection to it
	 * @param String filename of the new SQLite database
	 * @throws SQLException 
	 */
	public void setDatabaseFileName(String filename) throws SQLException {
		databaseFileName = filename;
		this.initConnection();
	}
	
}
