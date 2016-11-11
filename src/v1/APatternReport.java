package v1;

import java.util.ArrayList;

/**
 * This class is used by APattern to send results from the email evaluation back to the calling class
 * @author Brandon Dixon
 *
 */

public class APatternReport {

	private double pConfidentialScoreOfThisEmail;
	private double pConfidential;
	private double pNotConfidential;
	private ArrayList<Integer> words;
	private ArrayList<Double> aPConfidential;
	boolean isLocked;
	
	/**
	 * Instantiate the report
	 * @param pConfidentialOfThisEmail the probability that the current email is confidential
	 * @param pAnyConfidential the new value of the probability that any email is confidential
	 * @param pAnyNotConfidential (1-pAnyConfidential)
	 */
	public APatternReport(double pConfidentialOfThisEmail, double pAnyConfidential, double pAnyNotConfidential) {
		pConfidentialScoreOfThisEmail = pConfidentialOfThisEmail;
		pConfidential = pAnyConfidential;
		pNotConfidential = pAnyNotConfidential;
		words = new ArrayList<Integer>();
		aPConfidential = new ArrayList<Double>();
		isLocked = false;
	}
	
	/**
	 * This is used by the APattern class to add a word with its new values.  The Class that receives this instance should get these words and adjust their values accordingly
	 * @param word String
	 * @param nEmailsIn incremented
	 * @param averagePConfidential new value
	 * @throws APatternException 
	 */
	public void addWordAndSetValues(int word, double averagePConfidential) throws APatternException {
		if (isLocked) {
			throw new APatternException();
		}
		words.add(word);
		aPConfidential.add(averagePConfidential);
	}
	
	public void lock() {
		isLocked = true;
	}
	
	/**
	 * Get the probability any given email is not confidential.
	 * @return pNotConfidential
	 */
	public double getProbabilityAnyEmailIsNotConfidential() {
		return pNotConfidential;
	}
	
	/**
	 * Get the probability any given email is confidential.
	 * @return pConfidential
	 */
	public double getProbabilityAnyEmailIsConfidential() {
		return pConfidential;
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
	public int getWord(int i) {
		return words.get(i);
	}
	
	
	/**
	 * Get the average probability that an email which is confidential has the word i.
	 * @param i index
	 * @return double
	 */
	public double getAverageProbabilityConfidential(int i) {
		return aPConfidential.get(i);
	}
}
