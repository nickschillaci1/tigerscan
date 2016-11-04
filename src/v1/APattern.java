package v1;

import java.util.ArrayList;

/**
 * This class will handle analysis of the information to determine how likely it is that an email is confidential
 * @author Brandon Dixon
 * @version 11/4/16
 *
 * Probabilities are from 0-100, which is where all word scores should fall between
 */

public class APattern {

	private final double pCONFIDENTIAL = 50; //50% chance any email at all is confidential
	//private final double pNotCONFIDENTIAL = 50; //we will need this if the value above changes
	private ArrayList<Double> pConfidentialWithWord;
	
	/**
	 * Initialize the Class
	 */
	public APattern() {
		pConfidentialWithWord = new ArrayList<Double>();
	}
	
	/**
	 * Report that a word with confidentially rating p has been added.  The word itself is not needed here.
	 * @param p int
	 */
	public void addWord(double p) {
		//this will be the probability that a message is confidential given the word is in it
		pConfidentialWithWord.add(p);
	}
	
	/**
	 * This will calculate the total probability that an email is confidential
	 * @return
	 */
	public double calculateProbability() {
		int size = pConfidentialWithWord.size();
		if (size == 0) {
			return pCONFIDENTIAL;
		}
		
		double rOne = 1;
		double rTwo = 1;
		for (int i=0; i<size; i++) {
			rOne*=pConfidentialWithWord.get(i);
			rTwo*=(100-pConfidentialWithWord.get(i));
		}
		
		return rOne/(rOne+rTwo);
	}
	
	
}
