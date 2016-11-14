package scoring;

import static org.junit.Assert.*;

import org.junit.Test;

public class APatternTest {

	@Test
	public void test() {
		//test what happens when the first email ever is scanned and a word is found
		//testVeryFirstEmailWithWordFound();\

		//when a word is not found
		//testVeryFirstEmailWithWordNotFound();
		
		//test second email
		testSecondEmailWordFound();
	}
	
	/*private void testVeryFirstEmailWithWordNotFound() {
		System.out.println("Test case: first email ever, word not found");
		//test what happens when the first email ever is scanned
		double pConfidential = 50;
		int totalEmails = 0;
		
		APattern p = new APattern(totalEmails,pConfidential);
		APatternReport r = null;
		
		r = p.calculateProbability();
		
		//look at the results
		double rPConfidential = r.getProbabilityAnyEmailIsConfidential();
		double rCEmail = r.getConfidentialityScoreOfThisEmail();
		
		System.out.println("P any email: "+rPConfidential);
		System.out.println("P of this Email: "+rCEmail);
		System.out.println();
		
	}*/

	private void testVeryFirstEmailWithWordFound() {
		//test what happens when the first email ever is scanned
		System.out.println("Test case: first email ever, word found");
		String cWord = "yellow";
		double pConfidentialWord = 50;
		double averageConfidentialityWord = 0;
		int wordInEmail = 0;
		int wordNotInEmail = 0;
		double cValue = 1;
		
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
		
		System.out.println("P any email: "+rPConfidential);
		System.out.println("P of this Email: "+rCEmail);
		System.out.println("Average P Confidential for word: "+aPWord);
		System.out.println();
	}
	
	private void testSecondEmailWordFound() {
		System.out.println("Test case: second email, word found");
		String cWord = "yellow";
		double pConfidentialWord = 75;
		double averageConfidentialityWord = 50;
		int wordInEmail = 0;
		int wordNotInEmail = 1;
		double cValue = 1;
		
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
		
		System.out.println("P any email: "+rPConfidential);
		System.out.println("P of this Email: "+rCEmail);
		System.out.println("Average P Confidential for word: "+aPWord);
		System.out.println();
		
	}

}
