package db;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import main.Config;
import main.CryptoUtility;

/**
 * This class will maintain the Database and will handle adding and removing terms.  It will also, through the SQLDatabase class, handle the external Database file.
 * 
 * @author Brandon Dixon, Nick Schillaci
 * @version 11/4/16
 **/

public class DatabaseManager {

    
    private HashMap<String,Double> terms;
    private SQLDatabase sqld;

    /**
     * This will initialize the database and load in terms if there are any to load
     */
    public DatabaseManager() {  //HASHINTOVALUE - the O, not zero, is the separator
    	terms = new HashMap<String,Double>();
    	this.updateLocalTerms();
    }

    /**
     * This will update the local HashMap terms for proper manipulation and analysis
     */
    public void updateLocalTerms() {
    	try {
    		sqld = new SQLDatabase(Config.getDatabaseFilename());
			terms = sqld.getTerms();
		} catch (SQLException e) {
			System.err.println("A proper SQL connection could not be made to the \""+Config.getDatabaseFilename()+"\"");
		}
    }

    /**
     * This will check to see if the database contains a certain term.
     * @param String term to check for
     * @return boolean of whether the database has the term or not
     */
    public boolean hasTerm(String term) {
		//root word manipulation will happen here - for now, use single line
    	String encryptedTerm = CryptoUtility.encryptString(term);
    	return terms.containsKey(encryptedTerm);
    }

    /**
     * This will add a term to the database
     * @param String term to add to the database
     * @param int of the confidentiality value of the term
     * @exception DatabaseAddTermException if the word is already present in the database, or if the confidentiality value: v<0 || v>100
     */
    public void addTerm(String term, double value) throws DatabaseAddTermException {
		//manipulate to root word if necessary
    	
    	String encryptedTerm = CryptoUtility.encryptString(term);
		//throw an exception if the term is there already
		if (terms.containsKey(encryptedTerm)) {
		    throw new DatabaseAddTermException(term);
		}
		
		terms.put(encryptedTerm,value);
	    try {
			sqld.addTerm(encryptedTerm,value);
		} catch (SQLException e) {
			e.printStackTrace();
		}

    }

    /**
     * This will add multiple terms to the database
     * @param ArrayList<String> terms to add to the database
     * @param ArrayList<Integer> of the values for each String
     * @exception DabaseAddTermException if one or more words is already present in the database, or if the confidentiality value: v<0 || v>100
     */
    public void addTerm(HashMap<String,Double> values) throws DatabaseAddTermException {
		//manipulate root words as necessary
		ArrayList<String> conflicts = new ArrayList<String>();
	
		//add all of the
		Set<String> keySet = values.keySet();
		String[] keys = keySet.toArray(new String[keySet.size()]);
		int length = keys.length;
		
		for (int i=0; i<length; i++) {
		    String temp = keys[i];
		    String encryptedTerm = CryptoUtility.encryptString(temp);
		    if (terms.containsKey(encryptedTerm)) {
		    	conflicts.add(temp);
		    } else {
		    	double tValue = values.get(keys[i]);
		    	//if the value is not between 0 and 100
		    	/*if (tValue<0 && tValue>100) {
		    		throw new DatabaseAddTermException("");
		    	}*/
		    	terms.put(encryptedTerm,tValue);
		    	try {
					sqld.addTerm(encryptedTerm,tValue);
				} catch (SQLException e) {
					e.printStackTrace();
				}
		    }
		}
	
	
		if (conflicts.size()>0) {
		    throw new DatabaseAddTermException(conflicts);
		}
	}
    
    /**
     * This will get the score of a particular term
     * @param String term to search score for 
     * @return int score
     * @throws DatabaseNoSuchTermException if that term is not in the Database
     */
    public double getScore(String term) throws DatabaseNoSuchTermException {
    	String encryptedTerm = CryptoUtility.encryptString(term);
    	if (!terms.containsKey(encryptedTerm)) {
    		throw new DatabaseNoSuchTermException(term);
    	}
    	
    	return terms.get(encryptedTerm);
    }
	
    /**
     * This will remove a term from the database.
     * @param String term to be removed
     * @exception DatabaseRemoveTermException if the word is not present in the database
     *
     */
    public void removeTerm(String term) throws DatabaseRemoveTermException {
		//manipulate the root word if necessary
    	String encryptedTerm = CryptoUtility.encryptString(""+term);
		//throws an exception if the term does not exist
		if (!terms.containsKey(encryptedTerm)) {
		    throw new DatabaseRemoveTermException(""+term);
		}
	
		terms.remove(encryptedTerm);
		try {
			sqld.removeTerm(encryptedTerm);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
    }

    /**
     * This will remove one or more terms from the database.
     * @param ArrayList<String> terms to be removed
	 * @exception DatabaseRemoveTermException if one or more words is not present in the database
	 */
    public void removeTerm(ArrayList<String> termArray) throws DatabaseRemoveTermException {
		//manipulate the root word if necessary
	
		ArrayList<String> error = new ArrayList<String>();
		String encryptedTerm = null;
		//remove all of the terms
		int length = termArray.size();
		for (int i=0; i<length; i++) {
		    String temp = termArray.get(i);
		    encryptedTerm = CryptoUtility.encryptString(temp);
		    if (!terms.containsKey(encryptedTerm)) {
		    	error.add(""+temp);
		    } else {
		    	terms.remove(encryptedTerm);
		    	try {
					sqld.removeTerm(encryptedTerm);
				} catch (SQLException e) {
					e.printStackTrace();
				}
		    }
		}
	
		if (error.size()>0) {
		    throw new DatabaseRemoveTermException(error);
		}
	
	}
	
    /**
     * This will remove all terms from the Database.  This cannot be undone.
     */
	public void removeAllTerms() {
		terms = new HashMap<String,Double>();
		try {
			sqld.removeAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
	
	/**
	 * Change/update the score of a term in the database
	 * @param String term is a hashed term to change the classification score of
	 * @param int score is the new score of the term
	 * @throws SQLException
	 * @throws DatabaseAddTermException 
	 */
	public void changeScore(String term, double score) throws SQLException, DatabaseAddTermException {
		String encryptedTerm = CryptoUtility.encryptString(""+term);
		if (terms.containsKey(""+term)) {
		    throw new DatabaseAddTermException(""+term);
		}
		
		sqld.changeScore(encryptedTerm, score);
		terms.put(encryptedTerm,score); //overwrite the current term with the new entry sporting the new score
	}
	
	/**
	 * Gets a HashMap<String, Integer> of terms in the database
	 * @return HashMap<term, score>
	 */
	public HashMap<String,Double> getTerms() {
		HashMap<String,Double> decryptedTerms = new HashMap<String,Double>();
		for (String term : terms.keySet()) {
			decryptedTerms.put(CryptoUtility.decryptString(term), terms.get(term));
		}
		return decryptedTerms;
	}
	
	/**
	 * Gets the number of emails a term has appeared in since it was added
	 * @param term
	 * @return
	 */
	public int getNumbEmailsIn(String term) {
		String encryptedTerm = CryptoUtility.encryptString(""+term);
		int freq = 0;
		try {
			freq = sqld.getNumbEmailsIn(encryptedTerm);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return freq;
	}
	
	/**
	 * Increment the frequency of a term in the database
	 * @param term
	 */
	public void incrementNumbEmailsIn(String term) {
		String encryptedTerm = CryptoUtility.encryptString(""+term);
		try {
			sqld.incrementNumbEmailsIn(encryptedTerm);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets the number of emails a term has not appeared in since it was added
	 * @param term
	 * @return
	 */
	public int getNumbEmailsNotIn(String term) {
		String encryptedTerm = CryptoUtility.encryptString(""+term);
		int freq = 0;
		try {
			freq = sqld.getNumbEmailsNotIn(encryptedTerm);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return freq;
	}
	
	/**
	 * Increment the frequency of a term in the database
	 * @param term
	 */
	public void incrementNumbEmailsNotIn(String term) {
		String encryptedTerm = CryptoUtility.encryptString(""+term);
		try {
			sqld.incrementNumbEmailsNotIn(encryptedTerm);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Get the average probability of a term
	 * @param term
	 * @return
	 */
	public double getAverageProbability(String term) {
		String encryptedTerm = CryptoUtility.encryptString(""+term);
		double prob = 0;
		try {
			prob = sqld.getAverageProbability(encryptedTerm);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return prob;
	}
	
	/**
	 * Set the average probability of a term
	 * @param term
	 * @param prob
	 */
	public void setAverageProbability(String term, double prob) {
		String encryptedTerm = CryptoUtility.encryptString(""+term);
		try {
			sqld.setAverageProbability(encryptedTerm, prob);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Get the average probability of a term
	 * @param term
	 * @return
	 */
	public double getProbabilityAny(String term) {
		String encryptedTerm = CryptoUtility.encryptString(""+term);
		double prob = 0;
		try {
			prob = sqld.getProbabilityAny(encryptedTerm);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return prob;
	}
	
	/**
	 * Set the average probability of a term
	 * @param term
	 * @param prob
	 */
	public void setProbabilityAny(String term, double prob) {
		String encryptedTerm = CryptoUtility.encryptString(""+term);
		try {
			sqld.setProbabilityAny(encryptedTerm, prob);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Get the file name of the database
	 * @return file name of the database
	 */
	public String getDatabaseFilename() {
		return sqld.getDatabaseFileName();
	}
	
	/**
	 * Set the file name of the database
	 * @param filename
	 * @throws SQLException 
	 */
	public void setDatabaseFilename(String filename) throws SQLException {
		sqld.setDatabaseFileName(filename);
	}
	
	/**
	 * Calls the SQLDatabase method to initialize the SQL connection to the database file
	 * @throws SQLException 
	 */
	public void initSQLConnection() throws SQLException {
		sqld.initConnection();
	}
	
	/**
	 * Calls the SQLDatabase method to close SQL connection to the database file
	 */
	public void closeSQLConnection() {
		sqld.closeConnection();
	}
	
	
}
