package v1;

import static org.junit.Assert.*;

import org.junit.Test;

public class APatternTest {

	@Test
	public void test() {
		//test what happens when the first email ever is scanned
		String cWord = "yellow";
		double pConfidential = 50;
		int wordInEmail = 0;
		double cValue = 1;
		int totalEmails = 0;
		
		APattern p = new APattern(totalEmails,pConfidential);
		APatternReport r = null;
		
		try {
			p.addWord(cWord.hashCode(),cValue,wordInEmail);
			r = p.calculateProbability();
		} catch (APatternException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//look at the results
		double rPConfidential = r.getProbabilityAnyEmailIsConfidential();
		double rCEmail = r.getConfidentialityScoreOfThisEmail();
		double aPWord = r.getAverageProbabilityConfidential(0);
		
		System.out.println("P any email: "+rPConfidential);
		System.out.println("P of this Email: "+rCEmail);
		System.out.println("Average P Confidential for word: "+aPWord);
	}

}
