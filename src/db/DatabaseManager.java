package db;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import v1.CryptoUtility;

/**
 * This class will maintain the Database and will handle adding and removing terms.  It will also, through the SQLDatabase class, handle the external Database file.
 * 
 * @author Brandon Dixon, Nick Schillaci
 * @version 11/4/16
 **/

public class DatabaseManager {

    
    private HashMap<String,Integer> terms;
    private SQLDatabase sqld;

    /**
     * This will initialize the database and load in terms if there are any to load
     */
    public DatabaseManager() {  //HASHINTOVALUE - the O, not zero, is the separator
    	new File("data/").mkdir(); //ensure data folder exists for first execution
    	terms = new HashMap<String,Integer>();
    	sqld = new SQLDatabase();
    	
		try {
			terms = sqld.getTerms();
		} catch (SQLException e) {
			e.printStackTrace();
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
    public void addTerm(String term, int value) throws DatabaseAddTermException {
		//manipulate to root word if necessary
    	
		//throw an exception if the term is there already
		if (terms.containsKey(term)) {
		    throw new DatabaseAddTermException(term);
		}

		String encryptedTerm = CryptoUtility.encryptString(term);
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
    public void addTerm(HashMap<String,Integer> values) throws DatabaseAddTermException {
		//manipulate root words as necessary
		ArrayList<String> conflicts = new ArrayList<String>();
	
		//add all of the
		Set<String> keySet = values.keySet();
		String[] keys = keySet.toArray(new String[keySet.size()]);
		int length = keys.length;
		
		for (int i=0; i<length; i++) {
		    String temp = keys[i];
		    if (terms.containsKey(temp)) {
		    	conflicts.add(temp);
		    } else {
		    	int tValue = values.get(keys[i]);
		    	//if the value is not between 0 and 100
		    	/*if (tValue<0 && tValue>100) {
		    		throw new DatabaseAddTermException("");
		    	}*/
		    	terms.put(temp,tValue);
		    	try {
					sqld.addTerm(temp,tValue);
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
    public int getScore(String term) throws DatabaseNoSuchTermException {
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
    	String encryptedTerm = CryptoUtility.encryptString(term);
		//throws an exception if the term does not exist
		if (!terms.containsKey(encryptedTerm)) {
		    throw new DatabaseRemoveTermException(term);
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
		    	error.add(temp);
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
		terms = new HashMap<String,Integer>();
		try {
			sqld.removeAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
	
	/**
	 * Change/update the score of a term in the database
	 * @param int term is a hashed term to change the classification score of
	 * @param int score is the new score of the term
	 * @throws SQLException
	 */
	public void changeScore(String term, int score) throws SQLException {
		String encryptedTerm = CryptoUtility.encryptString(term);
		sqld.changeScore(encryptedTerm, score);
	}
	
	/**
	 * Gets a HashMap<String, Integer> of terms in the database
	 * @return HashMap<term, score>
	 */
	public HashMap<String,Integer> getTerms() {
		HashMap<String,Integer> decryptedTerms = new HashMap<String,Integer>();
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
		int freq = 0;
		try {
			freq = sqld.getNumbEmailsIn(term.hashCode());
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
		try {
			sqld.incrementNumbEmailsIn(term.hashCode());
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
		int freq = 0;
		try {
			freq = sqld.getNumbEmailsNotIn(term.hashCode());
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
		try {
			sqld.incrementNumbEmailsNotIn(term.hashCode());
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
		double prob = 0;
		try {
			prob = sqld.getAverageProbability(term.hashCode());
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
		try {
			sqld.setAverageProbability(term.hashCode(), prob);
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
		double prob = 0;
		try {
			prob = sqld.getProbabilityAny(term.hashCode());
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
		try {
			sqld.setProbabilityAny(term.hashCode(), prob);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Calls the SQLDatabase method to initialize the SQL connection to the database file
	 */
	public void initSQLConnection() {
		sqld.initConnection();
	}
	
	/**
	 * Calls the SQLDatabase method to close SQL connection to the database file
	 */
	public void closeSQLConnection() {
		sqld.closeConnection();
	}
    
}
