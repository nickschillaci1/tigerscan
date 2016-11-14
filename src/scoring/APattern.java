package scoring;

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

	private ArrayList<Double> pConfidentialWithWord;
	private ArrayList<Integer> pWords;
	//private ArrayList<Double> pWordInConfidential;
	private ArrayList<Integer> pNumberOfEmailsWordIsIn;
	private ArrayList<Double> pAveragePerWord;
	//private ArrayList<Double> pConfPerWord;
	private ArrayList<Integer> pWordsEmailsIn;
	private ArrayList<Integer> pWordsEmailsNotIn;
	boolean hasAlreadyScanned;
	
	/**
	 * Initialize the Class
	 * int nTotalEmails - number of total emails scanned (not including this one)
	 * double confidential - the probability any email is confidential (times 100)
	 */
	public APattern() {
		pConfidentialWithWord = new ArrayList<Double>();
		pWords = new ArrayList<Integer>();
		pConfidentialWithWord = new ArrayList<Double>();
		pNumberOfEmailsWordIsIn = new ArrayList<Integer>();
		//pWordInConfidential = new ArrayList<Double>();
		pAveragePerWord = new ArrayList<Double>();
		//pConfPerWord = new ArrayList<Double>();
		pWordsEmailsIn = new ArrayList<Integer>();
		pWordsEmailsNotIn = new ArrayList<Integer>();
		hasAlreadyScanned = false;
	}
	
	/**
	 * Report that a word with confidentially rating p has been added.  The word itself is not needed here.
	 * @param word to add (hashed version)
	 * @param weight - the value given by the user
	 * @param ac - the average probability of the emails the word has appeared in
	 * @param numberOfEmailsWordIsIn
	 * @param emailsBeforeWord - the number of emails scanned before the word was added to the database
	 * @throws APatternException
	 */
	public void addWord(int word, double weight, double aC, int numberOfEmailsWordIsIn, int numberOfEmailsWordIsNotIn, double pConf) throws APatternException {
		if (!hasAlreadyScanned) {
			//this will be the probability that a message is confidential given the word is in it, multiplied by the probability that any given message is confidential
			numberOfEmailsWordIsIn++;
			double probabilityWordInConfidential = (numberOfEmailsWordIsIn/(numberOfEmailsWordIsIn+numberOfEmailsWordIsNotIn))*100;
			double partOne = probabilityWordInConfidential*pConf;
			double partTwo = (100-probabilityWordInConfidential)*(100-pConf);
			pConfidentialWithWord.add(partOne/(partOne+partTwo)*(aC/100)*weight*100);
			pAveragePerWord.add(aC);
			pWords.add(word);
			//pWordInConfidential.add(probabilityWordInConfidential);
			pNumberOfEmailsWordIsIn.add(numberOfEmailsWordIsIn);
			pWordsEmailsIn.add(numberOfEmailsWordIsIn);
			pWordsEmailsNotIn.add(numberOfEmailsWordIsNotIn);
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

			r = new APatternReport(pThisIsConfidential);
			
			//save value of number of emails
			int wordSize = pWords.size();
			for (int i=0; i<wordSize; i++) {
				int nEmailsWordIn = pNumberOfEmailsWordIsIn.get(i);
				double pWC = (pAveragePerWord.get(i)*(nEmailsWordIn-1)+pThisIsConfidential)/nEmailsWordIn;
				int emailsIn = pWordsEmailsIn.get(i);
				double confWord = (pAveragePerWord.get(i)*emailsIn+pWC)/(emailsIn+pWordsEmailsNotIn.get(i));
				r.addWordAndSetValues(pWords.get(i),pWC,confWord);
			} 
		} else {
			throw new APatternException();
		}
		
		//return the value from this analysis
		
		return r;
	}
}
