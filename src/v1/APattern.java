package v1;

import java.util.ArrayList;

/**
 * This class will handle analysis of the information to determine how likely it is that an email is confidential
 * This is a modification of Bayes Spam Filtering 
 * @author Brandon Dixon
 * @version 11/4/16
 *
 * Probabilities are from 0-100, which is where all word scores should fall between
 */

public class APattern {

	private final double pCONFIDENTIAL = 10; //arbitrary 10% chance any email at all is confidential
	//private final double pNotCONFIDENTIAL = 90; //we will need this if the value above changes
	private ArrayList<Double> pConfidentialWithWord;
	
	/**
	 * Initialize the Class
	 */
	public APattern() {
		pConfidentialWithWord = new ArrayList<Double>();
	}
	
	/**
	 * Report that a word with confidentially rating p has been added.  The word itself is not needed here.
	 * @param p double
	 */
	public void addWord(double p) {
		//this will be the probability that a message is confidential given the word is in it, multiplied by the probability that any given message is confidential
		pConfidentialWithWord.add(p*pCONFIDENTIAL);
	}
	
	/**
	 * This will calculate the total probability that an email is confidential
	 * @return probability that an email is confidential
	 */
	public double calculateProbability() {
		int size = pConfidentialWithWord.size();
		if (size == 0) {
			return pCONFIDENTIAL;
		}
		
		double rTemp;
		double rOne = 1;
		double rTwo = 1;
		double rValue;
		for (int i=0; i<size; i++) {
			rTemp=pConfidentialWithWord.get(i);
			rOne*=rTemp;
			rTwo*=(100-rTemp);
		}
		
		//TODO learn from the analysis of this email
		/* Record information to the database:
		 * Increment the total number of emails scanned
		 * Adjust the probability that any given email is confidential:
		 * Average the values of current (prob*(numb emails -1)+(current email prob))/(total number of emails)
		 * 
		 * Adjust probability that any given word appears in confidential messages
		 */
		
		
		//return the value from this analysis
		rValue = rOne/(rOne+rTwo);
		
		return rValue;
	}
}
