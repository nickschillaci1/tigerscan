package scoring;

import java.util.ArrayList;

/**
 * This class is used by APattern to send results from the email evaluation back to the calling class
 * @author Brandon Dixon
 *
 */

public class APatternReport {

	private double pConfidentialScoreOfThisEmail;
	private ArrayList<String> words;
	private ArrayList<Double> aPConfidential;
	private ArrayList<Double> aPAnyC;
	boolean isLocked;
	
	/**
	 * Instantiate the report
	 * @param pConfidentialOfThisEmail the probability that the current email is confidential
	 * @param pAnyConfidential the new value of the probability that any email is confidential
	 * @param pAnyNotConfidential (1-pAnyConfidential)
	 */
	public APatternReport(double pConfidentialOfThisEmail) {
		pConfidentialScoreOfThisEmail = pConfidentialOfThisEmail;
		words = new ArrayList<String>();
		aPConfidential = new ArrayList<Double>();
		aPAnyC = new ArrayList<Double>();
		isLocked = false;
	}
	
	/**
	 * This is used by the APattern class to add a word with its new values.  The Class that receives this instance should get these words and adjust their values accordingly
	 * @param word String
	 * @param nEmailsIn incremented
	 * @param averagePConfidential new value
	 * @throws APatternException 
	 */
	public void addWordAndSetValues(String word, double averagePConfidential, double pC) throws APatternException {
		if (isLocked) {
			throw new APatternException();
		}
		words.add(word);
		aPConfidential.add(averagePConfidential);
		aPAnyC.add(pC);
	}
	
	public void lock() {
		isLocked = true;
	}
	
	/**
	 * Get the confidentiality score of this email
	 * @return double
	 */
	public double getConfidentialityScoreOfThisEmail() {
		return pConfidentialScoreOfThisEmail;
	}
	
	/**
	 * Get the String word at index i.
	 * @param i index
	 * @return String word
	 */
	public String getWord(int i) {
		return words.get(i);
	}
	
	/**
	 *Gget the number of words
	 * @return
	 */
	public int getNumbWords() {
		return words.size();
	}
	
	
	/**
	 * Get the average probability that an email which is confidential has the word i.
	 * @param i index
	 * @return double
	 */
	public double getAverageProbabilityConfidential(int i) {
		return aPConfidential.get(i);
	}
	
	/**
	 * Get the probability that any email is confidential (word per word basis)
	 * @param i index
	 * @return double
	 */
	public double getProbabilityConfidentialPerWord(int i) {
		return aPAnyC.get(i);
	}
}
