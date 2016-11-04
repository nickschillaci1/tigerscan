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
	private int numberOfEmails;
	private ArrayList<Double> pConfidentialWithWord;
	private ArrayList<String> pWords;
	private ArrayList<Double> pWordInConfidential;
	
	/**
	 * Initialize the Class
	 */
	public APattern() {
		pConfidentialWithWord = new ArrayList<Double>();
		pWords = new ArrayList<String>();
		//load value of pConfidential
		pNotConfidential = 100 - pConfidential;
		//load value of number of emails
		pConfidentialWithWord = new ArrayList<Double>();
	}
	
	/**
	 * Report that a word with confidentially rating p has been added.  The word itself is not needed here.
	 * @param probabilityWordInConfidential
	 */
	public void addWord(String word, double weight) {
		double probabilityWordInConfidential; //load this in: number of emails word in/number total emails
		//this will be the probability that a message is confidential given the word is in it, multiplied by the probability that any given message is confidential
		double partOne = probabilityWordInConfidential*pConfidential;
		double partTwo = (100-probabilityWordInConfidential)*pNotConfidential;
		pConfidentialWithWord.add(partOne/(partOne+partTwo)*weight);
		pWords.add(word);
		pWordInConfidential.add(probabilityWordInConfidential);
	}
	
	/**
	 * This will calculate the total probability that an email is confidential
	 * @return probability that an email is confidential
	 */
	public double calculateProbability() {
		int size = pConfidentialWithWord.size();
		if (size == 0) {
			return pConfidential;
		}
		
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
		
		//TODO learn from the analysis of this email
		/* Record information to the database:
		 * Increment the total number of emails scanned
		 * Adjust the probability that any given email is confidential:
		 * Average the values of current (prob*(numb emails -1)+(current email prob))/(total number of emails)
		 * 
		 * Adjust probability that any given word appears in confidential messages
		 */
		
		numberOfEmails ++;
		pConfidential = (pConfidential*(numberOfEmails-1)+pThisIsConfidential)/numberOfEmails;
		pNotConfidential = (100-pConfidential);
		
		//save value of number of emails
		int wordSize = pWords.size();
		for (int i=0; i<wordSize; i++) {
			int nEmailsWordIn; //load in, increment by one
			double pWC = (pWordInConfidential.get(i)*(nEmailsWordIn-1)+pThisIsConfidential)/nEmailsWordIn;
			//store that value
			//store number of emails word is in
		}
		
		//return the value from this analysis
		
		return pThisIsConfidential;
	}
}
