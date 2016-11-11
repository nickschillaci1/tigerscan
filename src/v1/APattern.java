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
	public APattern(int nTotalEmails) {
		pConfidentialWithWord = new ArrayList<Double>();
		pWords = new ArrayList<Integer>();
		//load value of pConfidential
		pNotConfidential = 100 - pConfidential;
		//load value of number of emails
		pConfidentialWithWord = new ArrayList<Double>();
		pNumberOfEmailsWordIsIn = new ArrayList<Integer>();
		numberOfTotalEmails = nTotalEmails;
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
			double probabilityWordInConfidential = numberOfEmailsWordIsIn/numberOfTotalEmails;
			double partOne = probabilityWordInConfidential*pConfidential;
			double partTwo = (100-probabilityWordInConfidential)*pNotConfidential;
			pConfidentialWithWord.add(partOne/(partOne+partTwo)*weight);
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
			/*if (size == 0) {
				return pConfidential;
			}*/
			
			double rTemp;
			double rOne = 1;
			double rTwo = 1;
			double pThisIsConfidential;
			for (int i=0; i<size; i++) {
				rTemp=pConfidentialWithWord.get(i);
				rOne*=rTemp;
				rTwo*=(100-rTemp);
			}
			
			pThisIsConfidential = rOne/(rOne+rTwo);
			
			numberOfTotalEmails ++;
			pConfidential = (pConfidential*(numberOfTotalEmails-1)+pThisIsConfidential)/numberOfTotalEmails;
			pNotConfidential = (100-pConfidential);
			r = new APatternReport(pThisIsConfidential, pConfidential, pNotConfidential);
			
			//save value of number of emails
			int wordSize = pWords.size();
			for (int i=0; i<wordSize; i++) {
				int nEmailsWordIn = pNumberOfEmailsWordIsIn.get(i);
				double pWC = (pWordInConfidential.get(i)*(nEmailsWordIn-1)+pThisIsConfidential)/nEmailsWordIn;
				r.addWordAndSetValues(pWords.get(i),pWC);
				//store that value
				//store number of emails word is in
			} 
		} else {
			throw new APatternException();
		}
		
		//return the value from this analysis
		
		return r;
	}
}
