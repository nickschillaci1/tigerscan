package v1;

import static org.junit.Assert.*;

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
		testRemoveAllTerms();
		testAddTermStringInt();
		testHasTerm();
		testRemoveTermString();
		testAddTermHashMapOfStringInteger();
		testRemoveTermArrayListOfString();
		
	}
	
	@Test
	public void testRemoveAllTerms() {
		Database db = new Database();
		db.removeAllTerms();
	}

	@Test
	public void testAddTermStringInt() {
		Database db = new Database();
		db.removeAllTerms();
		
		//test adding a term that does not exist
		try {
			db.addTerm("hello",10);
		} catch (DatabaseAddTermException e) {
			fail("The add term test failed: "+e);
		}
		
		//test a term that should exist
		try {
			db.addTerm("hello",10);
			fail("The add term test failed: 'hello' has already been added and should not be able to be added again");
		} catch (DatabaseAddTermException e) {
			//nothing, test succeeded here
		}
	}

	@Test
	public void testHasTerm() {
		Database db = new Database();

		//test a term we do have
		assertTrue("The term 'hello' should exist in the database.",db.hasTerm("hello"));
		
		//test a term we do not have
		assertTrue("The term 'goodbye' should not exist in the database.",db.hasTerm("goodbye")==false);
	}
	
	@Test
	public void testRemoveTermString() {
		Database db = new Database();
		
		//remove a term we do have
		try {
			db.removeTerm("hello");
		} catch (DatabaseRemoveTermException e) {
			fail("The remove term test has failed: "+e);
		}
		
		//remove test, we no longer have this term
		try {
			db.removeTerm("hello");
			fail("The remove term test has failed: 'hello' has already been removed");
		} catch (DatabaseRemoveTermException e) {
			//the test has succeeded
		}
	}
	
	@Test
	public void testAddTermHashMapOfStringInteger() {
		Database db = new Database();
		
		HashMap<String,Integer> test = new HashMap<String,Integer>();
		test.put("banana",10);
		test.put("apple",10);
		test.put("carrot",20);
		
		//this should work 
		try {
			db.addTerm(test);
		} catch (DatabaseAddTermException e) {
			fail("Adding multiple terms test has failed: "+e);
		}
		
		//this should work 
		try {
			db.addTerm(test);
			fail("Adding multiple terms test has failed: terms already added");
		} catch (DatabaseAddTermException e) {
			//worked
		}
		
		
	}

	@Test
	public void testRemoveTermArrayListOfString() {
		Database db = new Database();
		
		ArrayList<String> termsToRemove = new ArrayList<String>();
		termsToRemove.add("banana");
		termsToRemove.add("apple");
		
		//this should work
		try {
			db.removeTerm(termsToRemove);
		} catch (DatabaseRemoveTermException e) {
			fail("Removing multiple terms test has failed: "+e);
		}
		
		//should not work below, terms already removed
		try {
			db.removeTerm(termsToRemove);
			fail("Removing multiple terms test has failed: terms alredy removed");
		} catch (DatabaseRemoveTermException e) {
			//test worked
		}
	}

}
