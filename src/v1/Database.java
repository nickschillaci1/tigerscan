package v1;

import java.util.Scanner;
import java.util.ArrayList;

/**
 * This class will maintain the Database and will handle adding and removing terms.  It will also, through the FileHandler, handle the external file.
 * 
 * @author Brandon Dixon
 * @version 10/24/16
 **/

public class Database {

    private ArrayList<Integer> terms = new ArrayList<Integer>();
    private ArrayList<Integer> values = new ArrayList<Integer>();
   


    /**
     * This will initalize the database and load in terms if there are any to load
     */
    public Database() {
	    String in = FileHandler.getDatabaseString();
	    
	    //TODO: decide what input will look like and parse
	    String inTerms=""; //these will be the terms themselves
	    String inValues=""; //these will be the confidentiality values of each term
	    
	    Scanner strScan = new Scanner(inTerms);
	    
	    while (strScan.hasNextLine()) { 
	    	terms.add(strScan.nextLine());	
	    }
	    
	    strScan = new Scanner(inValues);
	    
	    while (strScan.hasNextLine()) {
	    	values.add(strScan.nextLine());
	    }
	    
	    strScan.close();

    }


    /**
     * This will check to see if the database contains a certain term.
     * @param String term to check for
     * @return boolean of whether the database has the term or not
     */
    public boolean hasTerm(String term) {
		//root word manipulation will happen here - for now, use single line
	
		return terms.contains(term.hashCode());
    }

    /**
     * This will add a term to the database
     * @param String term to add to the database
     * @param int of the confidentiality value of the term
     * @exception DatabaseAddTermException if the word is already present in the database
     */
    public void addTerm(String term, int value) throws DatabaseAddTermException {
		//manipulate to root word if necessary
	
		//throw an exception if the term is there already
    	int t = term.hashCode();
		if (terms.contains(t)) {
		    throw new DatabaseAddTermException(t);
		}
	
		terms.add(term.hashCode());
		values.add(value);
	
		//rewrite the file
		rewriteFile();

    }

    /**
     * This will add multiple terms to the database
     * @param ArrayList<Strimg> terms to add to the database
     * @param ArrayList<Integer> of the values for each String
     * @exception DabaseAddTermException if one or more words is already present in the database
     */
    public void addTerm(ArrayList<String> termArray, ArrayList<Integer> valueArray) throws DatabaseAddTermException {
		//manipulate root words as necessary
		ArrayList<Integer> conflicts = new ArrayList<Integer>();
	
		//add all of the
		int length = termArray.size();
		for (int i=0; i<length; i++) {
		    String temp = termArray.get(i);
		    if (terms.contains(temp.hashCode())) {
		    	conflicts.add(temp.hashCode());
		    } else {
		    	terms.add(temp.hashCode());
		    	values.add(valueArray.get(i));
		    }
		}
	
		rewriteFile();
	
		if (conflicts.size()>0) {
		    throw new DatabaseAddTermException(conflicts);
		}
	}
	
    /**
     * This will remove a term from the database.
     * @param String term to be removed
     * @exception DatabaseRemoveTermException if the word is not present in the database
     *
     */
    public void removeTerm(String term) throws DatabaseRemoveTermException {
		//manipulate the root word if neccesary
	
		//throws an exception if the term does not exist
    	int t = term.hashCode();
		if (!terms.contains(t)) {
		    throw new DatabaseRemoveTermException(t);
		}
	
		int i = terms.indexOf(t);
		terms.remove(term);
		values.remove(i);
	
		//rewrite the file
		rewriteFile();
    }

    /**
     * This will remove one or more terms from the database.
     * @param ArrayList<String> terms to be removed
	 * @exception DatabaseRemoveTermException if one or more words is not present in the database
	 */
    public void removeTerm(ArrayList<String> termArray) throws DatabaseRemoveTermException {
		//manipulate the root word if neccessary
	
		ArrayList<Integer> error = new ArrayList<Integer>();
		//remove all of the terms
		int length = termArray.size();
		for (int i=0; i<length; i++) {
		    int temp = termArray.get(i).hashCode();
		    if (!terms.contains(temp)) {
		    	error.add(temp);
		    } else {
		    	int ind = terms.indexOf(temp);
		    	terms.remove(temp);
		    	values.remove(ind);
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
		terms = new ArrayList<Integer>();
		values = new ArrayList<Integer>();
	
		rewriteFile();
    }


    /**
     * This is a private method that will totally rewrite the file with the contents of the ArrayList terms
     */
    private void rewriteFile() {
		String allTerms = "";
		int length = terms.size();
	
		for (int i=0; i<length; i++) {
		    allTerms += terms.get(i)+"\n";
		}
	
		if (length>0) {
			allTerms = allTerms.substring(0,allTerms.length()-1);
		}
		
		FileHandler.saveDatabaseString(allTerms);
    }
    
}
