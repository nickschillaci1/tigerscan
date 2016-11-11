package v1;

import java.util.ArrayList;

/**
 * This class will handle analysis of the information to determine how likely it is that an email is confidential
 * This is a modification of Bayes Spam Filtering 
 * @author Brandon Dixon
 * @version 11/4/16
 *
 */


/**
 * The database should save:
 * Words
 * Each score's word
 * For each word: number of emails it has appeared in
 * For each word: average of probabilities of each email it has apperaed in, 
 * Total number of emails we've scanned
 * Probability that any given email is confidential
 *
 */

public class APattern {

	private double pConfidential;
	private double pNotConfidential;
	private ArrayList<Double> pConfidentialWithWord;
	private ArrayList<Integer> pWords;
	private ArrayList<Double> pWordInConfidential;
	private ArrayList<Integer> pNumberOfEmailsWordIsIn;
	int numberOfTotalEmails;
	boolean hasAlreadyScanned;
	
	/**
	 * Initialize the Class
	 */
	public APattern(int nTotalEmails, double confidential) {
		pConfidentialWithWord = new ArrayList<Double>();
		pWords = new ArrayList<Integer>();
		pConfidential = confidential;
		pNotConfidential = 100 - pConfidential;
		pConfidentialWithWord = new ArrayList<Double>();
		pNumberOfEmailsWordIsIn = new ArrayList<Integer>();
		pWordInConfidential = new ArrayList<Double>();
		numberOfTotalEmails = nTotalEmails+1;
		hasAlreadyScanned = false;
	}
	
	/**
	 * Report that a word with confidentially rating p has been added.  The word itself is not needed here.
	 * @param probabilityWordInConfidential
	 * @throws APatternException 
	 */
	public void addWord(int word, double weight, int numberOfEmailsWordIsIn) throws APatternException {
		if (!hasAlreadyScanned) {
			//this will be the probability that a message is confidential given the word is in it, multiplied by the probability that any given message is confidential
			numberOfEmailsWordIsIn++;
			double probabilityWordInConfidential = (numberOfEmailsWordIsIn/numberOfTotalEmails)*100;
			double partOne = probabilityWordInConfidential*pConfidential;
			double partTwo = (100-probabilityWordInConfidential)*pNotConfidential;
			pConfidentialWithWord.add(partOne/(partOne+partTwo)*weight*100);
			pWords.add(word);
			pWordInConfidential.add(probabilityWordInConfidential);
			pNumberOfEmailsWordIsIn.add(numberOfEmailsWordIsIn);
		} else {
			throw new APatternException();
		}
	}
	
	/**
	 * This will calculate the total probability that an email is confidential
	 * @return probability that an email is confidential
	 * @throws APatternException 
	 */
	public APatternReport calculateProbability() throws APatternException {
		APatternReport r;
		
		if (!hasAlreadyScanned) {
			int size = pConfidentialWithWord.size();
			
			double rTemp;
			double rOne = 1;
			double rTwo = 1;
			double pThisIsConfidential;
			for (int i=0; i<size; i++) {
				rTemp=pConfidentialWithWord.get(i);
				rOne*=rTemp;
				rTwo*=(100-rTemp);
			}
			
			pThisIsConfidential = rOne/(rOne+rTwo)*100;
				
			//handle the case that this is the first email being scanned
			if (numberOfTotalEmails>1) {
				pConfidential = (pConfidential*(numberOfTotalEmails-1)+pThisIsConfidential)/numberOfTotalEmails;
			} else {
				pConfidential = (pConfidential+pThisIsConfidential)/2;
			}
				
			pNotConfidential = (100-pConfidential);
			r = new APatternReport(pThisIsConfidential, pConfidential, pNotConfidential);
			
			//save value of number of emails
			int wordSize = pWords.size();
			for (int i=0; i<wordSize; i++) {
				int nEmailsWordIn = pNumberOfEmailsWordIsIn.get(i);
				double pWC = (pWordInConfidential.get(i)*(nEmailsWordIn-1)+pThisIsConfidential)/nEmailsWordIn;
				r.addWordAndSetValues(pWords.get(i),pWC);
			} 
		} else {
			throw new APatternException();
		}
		
		//return the value from this analysis
		
		return r;
	}
}
