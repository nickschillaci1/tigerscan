package scoring;

import static org.junit.Assert.*;

import org.junit.Test;

public class APatternTest {

	@Test
	public void test() {
		System.out.println("This will test different cases.  It will give information about the email before and after it is scanned.  Cases are independent of eachother.\n");
		
		//test what happens when the first email ever is scanned and a word is found
		testVeryFirstEmailWithWordFound();
		
		//test second email
		testSecondEmailWordFound();
	}

	private void testVeryFirstEmailWithWordFound() {
		//test what happens when the first email ever is scanned
		System.out.println("Test case: first email ever, word found");
		String cWord = "yellow";
		double pConfidentialWord = 50;
		double averageConfidentialityWord = 0;
		int wordInEmail = 0;
		int wordNotInEmail = 0;
		double cValue = 1;
		
		System.out.println("\tOne word found");
		System.out.println("\tP any Email: "+pConfidentialWord);
		System.out.println("\tFound in "+wordInEmail+" emails prior");
		System.out.println("\tNot found in "+wordNotInEmail+" email prior");
		System.out.println("\tWord weight: "+cValue);
		System.out.println("\n\tAfter scanning:");
		
		APattern p = new APattern();
		APatternReport r = null;
		
		try {
			p.addWord(cWord.hashCode(),cValue,averageConfidentialityWord,wordInEmail,wordNotInEmail,pConfidentialWord);
			r = p.calculateProbability();
		} catch (APatternException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//look at the results
		double rPConfidential = r.getProbabilityConfidentialPerWord(0);
		double rCEmail = r.getConfidentialityScoreOfThisEmail();
		double aPWord = r.getAverageProbabilityConfidential(0);
		
		System.out.println("\tP of this Email: "+rCEmail);
		System.out.println("\tP any email: "+rPConfidential);
		System.out.println("\tAverage P Confidential for word: "+aPWord);
		System.out.println();
	}
	
	private void testSecondEmailWordFound() {
		System.out.println("Test case: second email, word found");
		String cWord = "yellow";
		double pConfidentialWord = 75;
		double averageConfidentialityWord = 0;
		int wordInEmail = 0;
		int wordNotInEmail = 1;
		double cValue = 1;
		
		System.out.println("\tOne word found");
		System.out.println("\tP any Email: "+pConfidentialWord);
		System.out.println("\tFound in "+wordInEmail+" emails prior");
		System.out.println("\tNot found in "+wordNotInEmail+" email prior");
		System.out.println("\tWord weight: "+cValue);
		System.out.println("\n\tAfter scanning:");
		
		
		APattern p = new APattern();
		APatternReport r = null;
		
		try {
			p.addWord(cWord.hashCode(),cValue,averageConfidentialityWord,wordInEmail,wordNotInEmail,pConfidentialWord);
			r = p.calculateProbability();
		} catch (APatternException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//look at the results
		double rPConfidential = r.getProbabilityConfidentialPerWord(0);
		double rCEmail = r.getConfidentialityScoreOfThisEmail();
		double aPWord = r.getAverageProbabilityConfidential(0);
		
		System.out.println("\tP of this Email: "+rCEmail);
		System.out.println("\tP any email: "+rPConfidential);
		System.out.println("\tAverage P Confidential for word: "+aPWord);
		System.out.println();
		
	}

}
