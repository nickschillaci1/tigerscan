package v1;

import java.util.ArrayList;

public class APatternReport {

	private double pConfidentialScoreOfThisEmail;
	private double pConfidential;
	private double pNotConfidential;
	private ArrayList<String> words;
	private ArrayList<Integer> nEmails;
	private ArrayList<Double> aPConfidential;
	
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
		words = new ArrayList<String>();
		nEmails = new ArrayList<Integer>();
		aPConfidential = new ArrayList<Double>();
	}
	
	/**
	 * This is used by the APattern class to add a word with its new values.  The Class that receives this instance should get these words and adjust their values accordingly
	 * @param word String
	 * @param nEmailsIn incremented
	 * @param averagePConfidential new value
	 */
	public void addWordAndSetValues(String word, int nEmailsIn, double averagePConfidential) {
		words.add(word);
		nEmails.add(nEmailsIn);
		aPConfidential.add(averagePConfidential);
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
	 * Get the integer number of emails at word i.
	 * @param i index
	 * @return int number of emails
	 */
	public int getNumberOfEmails(int i) {
		return nEmails.get(i); 
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
