package scoring;

import static org.junit.Assert.*;

import org.junit.Test;

import db.DBHash;

public class APatternTest {

	@Test
	public void test() {

		testFirstEmail();
		testSecondEmail();
		testThirdEmailTwoWords();
	}
	
	private void testFirstEmail() {
		//first email
		APattern aP = new APattern();
		int word = DBHash.hashCode("banana");
		double weight = 1;
		double aC = 50;
		int numberOfEmailsWordIsIn = 0;
		int numberOfEmailsWordIsNotIn = 0;
		double pConf = 50;
		
		//test script to add a word
		//public void addWord(int word, double weight, double aC, int numberOfEmailsWordIsIn, int numberOfEmailsWordIsNotIn, double pConf) throws APatternException {
		aP.addWord(word,weight,aC,numberOfEmailsWordIsIn,numberOfEmailsWordIsNotIn,pConf);
		
		//check this email
		APatternReport aR = aP.calculateProbability();
		
		assertEquals(57.1428,aR.getConfidentialityScoreOfThisEmail(),0.0001); 
		assertEquals(57.1428,aR.getAverageProbabilityConfidential(0),0.0001);
	}
	
	private void testSecondEmail() {
		//information is taken from the results above
		APattern aP = new APattern();
		int word = DBHash.hashCode("banana");
		double weight = 1;
		double aC = 57.1428;
		int numberOfEmailsWordIsIn = 1;
		int numberOfEmailsWordIsNotIn = 0;
		double pConf = 57.1428;
		
		//test script to add a word
		//public void addWord(int word, double weight, double aC, int numberOfEmailsWordIsIn, int numberOfEmailsWordIsNotIn, double pConf) throws APatternException {
		aP.addWord(word,weight,aC,numberOfEmailsWordIsIn,numberOfEmailsWordIsNotIn,pConf);
		
		//check this email
		APatternReport aR = aP.calculateProbability();
		
		assertEquals(70.3296,aR.getConfidentialityScoreOfThisEmail(),0.0001); 
		assertEquals(63.7362,aR.getAverageProbabilityConfidential(0),0.0001);
		
	}
	
	private void testThirdEmailTwoWords() {
		//information for word one is taken from results above
		APattern aP = new APattern();
		int word1 = DBHash.hashCode("banana");
		double weight1 = 1;
		double aC1 = 63.7362;
		int numberOfEmailsWordIsIn1 = 2;
		int numberOfEmailsWordIsNotIn1 = 0;
		double pConf1 = 63.7362;
		
		aP.addWord(word1,weight1,aC1,numberOfEmailsWordIsIn1,numberOfEmailsWordIsNotIn1,pConf1);
		
		int word2 = DBHash.hashCode("orange");
		double weight2 = 2;
		double aC2 = 50;
		int numberOfEmailsWordIsIn2 = 0;
		int numberOfEmailsWordIsNotIn2 = 2;
		double pConf2 = 50;

		aP.addWord(word2,weight2,aC2,numberOfEmailsWordIsIn2,numberOfEmailsWordIsNotIn2,pConf2);
		
		//check this email
		APatternReport aR = aP.calculateProbability();
		
		assertEquals(91.655059,aR.getConfidentialityScoreOfThisEmail(),0.0001); 
	}

}
