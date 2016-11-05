package db;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * This class will maintain the Database and will handle adding and removing terms.  It will also, through the SQLDatabase class, handle the external Database file.
 * 
 * @author Brandon Dixon
 * @version 11/4/16
 **/

public class DatabaseManager {

    
    private HashMap<Integer,Integer> terms;
    private SQLDatabase sqld;

    /**
     * This will initialize the database and load in terms if there are any to load
     */
    public DatabaseManager() {
    	terms = new HashMap<Integer,Integer>();
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
	
    	return terms.containsKey(term.hashCode());
    }

    /**
     * This will add a term to the database
     * @param String term to add to the database
     * @param int of the confidentiality value of the term
     * @exception DatabaseAddTermException if the word is already present in the database, or if the confidentiality value: v<0 || v>100
     */
    public void addTerm(String term, int value) throws DatabaseAddTermException {
		//manipulate to root word if necessary
	
    	int t = term.hashCode();
    	
		//throw an exception if the term is there already
		if (terms.containsKey(t)) {
		    throw new DatabaseAddTermException(t);
		}
	
		if (value<0 && value>100) {
    		throw new DatabaseAddTermException(0);
    	}
		
		terms.put(t,value);
		
	    try {
			sqld.addTerm(t,value);
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
		ArrayList<Integer> conflicts = new ArrayList<Integer>();
	
		//add all of the
		Set<String> keySet = values.keySet();
		String[] keys = keySet.toArray(new String[keySet.size()]);
		int length = keys.length;
		
		for (int i=0; i<length; i++) {
		    int temp = keys[i].hashCode();
		    if (terms.containsKey(temp)) {
		    	conflicts.add(temp);
		    } else {
		    	int tValue = values.get(keys[i]);
		    	//if the value is not between 0 and 100
		    	if (tValue<0 && tValue>100) {
		    		throw new DatabaseAddTermException(0);
		    	}
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
    	int t = term.hashCode();
    	if (!terms.containsKey(t)) {
    		throw new DatabaseNoSuchTermException(term);
    	}
    	
    	return terms.get(t);
    }
	
    /**
     * This will remove a term from the database.
     * @param String term to be removed
     * @exception DatabaseRemoveTermException if the word is not present in the database
     *
     */
    public void removeTerm(String term) throws DatabaseRemoveTermException {
		//manipulate the root word if necessary
	
		//throws an exception if the term does not exist
    	int t = term.hashCode();
		if (!terms.containsKey(t)) {
		    throw new DatabaseRemoveTermException(t);
		}
	
		terms.remove(t);
		try {
			sqld.removeTerm(t);
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
	
		ArrayList<Integer> error = new ArrayList<Integer>();
		//remove all of the terms
		int length = termArray.size();
		for (int i=0; i<length; i++) {
		    int temp = termArray.get(i).hashCode();
		    if (!terms.containsKey(temp)) {
		    	error.add(temp);
		    } else {
		    	terms.remove(temp);
		    	try {
					sqld.removeTerm(temp);
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
     * This will remove a term from the database by its already hashed value.
     * @param int term to be removed
     * @exception DatabaseRemoveTermException if the word is not present in the database
     *
     */
    public void removeTermByHash(int term) throws DatabaseRemoveTermException {
		//throws an exception if the term does not exist
		if (!terms.containsKey(term)) {
		    throw new DatabaseRemoveTermException(term);
		}
	
		terms.remove(term);
		try {
			sqld.removeTerm(term);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
    }
	
    /**
     * This will remove all terms from the Database.  This cannot be undone.
     */
	public void removeAllTerms() {
		terms = new HashMap<Integer,Integer>();
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
	public void changeScore(int term, int score) throws SQLException {
		sqld.changeScore(term, score);
	}
	
	/**
	 * Gets a HashMap<Integer, Integer> of terms in the database
	 * @return HashMap<term, score>
	 */
	public HashMap<Integer,Integer> getTerms() {
		return terms;
	}
	
	/**
	 * Gets the frequency of a term in the database
	 * @param term
	 * @return
	 */
	public int getFrequency(String term) {
		int freq = 0;
		try {
			freq = sqld.getFrequency(term.hashCode());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return freq;
	}
	
	/**
	 * Increment the frequency of a term in the database
	 * @param term
	 */
	public void incrementFrequency(String term) {
		try {
			sqld.incrementFrequency(term.hashCode());
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
