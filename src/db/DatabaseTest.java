package db;
import main.Config;

/**
 * This covers test scripts 3,
 */

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Brandon Dixon
 * @version 10/24/16
 */

import org.junit.Test;

public class DatabaseTest {

	@Test
	public void test() {
		Config.initConfig();
		
		testAddAndCheckTerm();
		testDuplicateTermAdd();
		testAddAndCheckMultipleTerms();
		testDuplicateTermsAdd();
		testRemoveSingleTerm();
		testRemoveMultipleTerms();
		testDatabaseEncryption();
		testUpdateTerm();
		
	}
	
	private void testAddAndCheckTerm() {
		String term = "pizza";
		DatabaseManager dM = new DatabaseManager();
		dM.removeAllTerms();
		
		//add the term
		try {
			dM.addTerm(term,1);
		} catch (DatabaseAddTermException e) {
			// TODO Auto-generated catch block
			fail("Add and check term test failed: "+term+" is not in database, no error should be thrown");
		}
		
		//check for the term
		assertTrue("Add and check term test failed: test denied that "+term+" was in the database",dM.hasTerm(term));
	
	}
	
	private void testDuplicateTermAdd() {
		String term = "pizza";
		DatabaseManager dM = new DatabaseManager();
		
		//add the term
		try {
			dM.addTerm(term,1);
			fail("Add duplicate term failed: "+term+" the term is already in, should not be able to be added again");
		} catch (DatabaseAddTermException e) {
			// TODO Auto-generated catch block
		}
		
	}
	
	private void testAddAndCheckMultipleTerms() {
		HashMap<String,Double> terms = new HashMap<String,Double>();
		terms.put("one",1);
		terms.put("two",1);
		terms.put("three",1);
		
		
		DatabaseManager dM = new DatabaseManager();
		
		//add terms
		try {
			dM.addTerm(terms);
		} catch (DatabaseAddTermException e) {
			fail("Add and get multiple term test failed: none of those terms are in database, no error should have been thrown");
		}
		
		//check for the terms
		assertTrue("Add multiple and check failed: test denied that term was in database.",dM.hasTerm("one"));
		assertTrue("Add multiple and check failed: test denied that term was in database.",dM.hasTerm("two"));
		assertTrue("Add multiple and check failed: test denied that term was in database.",dM.hasTerm("three"));
		
	}
	
	private void testDuplicateTermsAdd() {
		HashMap<String,Integer> terms = new HashMap<String,Integer>();
		terms.put("one",1);
		terms.put("two",1);
		terms.put("three",1);
		
		
		DatabaseManager dM = new DatabaseManager();
		//add terms
		try {
			dM.addTerm(terms);
			fail("Add multiple duplicate terms failed, terms already added, should not have been able to be added again.");
		} catch (DatabaseAddTermException e) {
			//
		}
	}
	
	private void testRemoveSingleTerm() {
		String term = "pizza";
		DatabaseManager dM = new DatabaseManager();
		
		try {
			dM.removeTerm(term);
		} catch (DatabaseRemoveTermException e) {
			fail("Remove single term test failed: exception thrown");
		}
		
		try {
			dM.removeTerm(term);
			fail("Remove single term test failed: term has already been deleted, should not be able to be deleted for a second time.");
		} catch (DatabaseRemoveTermException e) {
			//
		}
	}
	
	private void testRemoveMultipleTerms() {
		ArrayList<String> terms = new ArrayList<String>();
		terms.add("one");
		terms.add("two");
		terms.add("three");
		
		DatabaseManager dM = new DatabaseManager();
		
		try {
			dM.removeTerm(terms);
		} catch (DatabaseRemoveTermException e) {
			fail("Remove multiple term test failed: exception thrown");
		}
		
		try {
			dM.removeTerm(terms);
			fail("Remove multiple term test failed: terms already removed, should not be able to be removed again");
		} catch (DatabaseRemoveTermException e) {
			//
		}
		
	}
	
	private void testRemoveAllTerms() {
		HashMap<String,Integer> terms = new HashMap<String,Integer>();
		terms.put("one",1);
		terms.put("two",1);
		terms.put("three",1);
		
		
		DatabaseManager dM = new DatabaseManager();
		//add terms
		try {
			dM.addTerm(terms);
		} catch (DatabaseAddTermException e) {
			//
		}
		
		dM.removeAllTerms();
		
		assertTrue("removeAll test failed.",dM.hasTerm("one")==false);
		
	}
	
	private void testDatabaseEncryption() {
		String word = "apple";
		DatabaseManager dM = new DatabaseManager();
		try {
			dM.addTerm(word, 1);
		} catch (DatabaseAddTermException e) {
			//
		}
		for(String encryptedWord : dM.getTerms().keySet()) {
			if(word.equals(encryptedWord.toString())) {
				fail("Database encryption test failed. The term was not properly encrypted and still matches its original form: "+word+": "+encryptedWord);
			}
		}
	}
	
	private void testUpdateTerm() {
		DatabaseManager dM = new DatabaseManager();
		dM.removeAllTerms();
		String word = "bad";
		
		try {
			dM.addTerm(word,1);
		} catch (DatabaseAddTermException e) {
			//nothing
		}
		
		try {
			dM.changeScore(word,2);
		} catch (SQLException | DatabaseAddTermException e) {
			fail("Update term test failed: failed to update classification score.");
		}
		
		try {
			assertEquals("Score classification did not update properly.",dM.getScore(word),2);
		} catch (DatabaseNoSuchTermException e) {
			fail("Update term test failed: failed to get classification score.");
		}
		
		dM.incrementNumbEmailsIn(word);
		dM.incrementNumbEmailsNotIn(word);
		
		assertEquals("IncrementNumbEmailsIn did not work.",dM.getNumbEmailsIn(word),1);
		assertEquals("IncrementNumbEmailsNotIn did not work.",dM.getNumbEmailsNotIn(word),1);
		
		dM.setAverageProbability(word,57);
		
		assertEquals("Set average probability did not work.",dM.getAverageProbability(word),57,.0001);
		
		dM.setProbabilityAny(word,76);
		
		assertEquals("Set probability any did not work.",dM.getProbabilityAny(word),76,.0001);
		
	}
}
