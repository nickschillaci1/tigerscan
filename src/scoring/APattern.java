package scoring;

import java.util.ArrayList;

/**
 * This class will handle analysis of the information to determine how likely it is that an email is confidential
 * This is a modification of Bayes Spam Filtering 
 * @author Brandon Dixon
 * @version 11/28/16
 *
 */



public class APattern {

	
	private final double TOTAL_MULTIPLIER = 0.75;
	
	private ArrayList<Double> pConfidentialWithWord;
	private ArrayList<String> pWords;
	//private ArrayList<Double> pWordInConfidential;
	private ArrayList<Integer> pNumberOfEmailsWordIsIn;
	private ArrayList<Double> pAveragePerWord;
	//private ArrayList<Double> pConfPerWord;
	private ArrayList<Integer> pNumberOfEmailsWordIsNotIn;
	private ArrayList<Double> pConfTotal;
	private boolean hasAlreadyScanned;
	private boolean isHighestProb;
	
	/**
	 * Initialize the Class
	 * int nTotalEmails - number of total emails scanned (not including this one)
	 * double confidential - the probability any email is confidential (times 100)
	 */
	public APattern() {
		pConfidentialWithWord = new ArrayList<Double>();
		pWords = new ArrayList<String>();
		pConfidentialWithWord = new ArrayList<Double>();
		pNumberOfEmailsWordIsIn = new ArrayList<Integer>();
		//pWordInConfidential = new ArrayList<Double>();
		pAveragePerWord = new ArrayList<Double>();
		//pConfPerWord = new ArrayList<Double>();
		pNumberOfEmailsWordIsNotIn = new ArrayList<Integer>();
		pConfTotal = new ArrayList<Double>();
		hasAlreadyScanned = false;
		isHighestProb = false;
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
	public void addWord(String word, double weight, double aC, int numberOfEmailsWordIsIn, int numberOfEmailsWordIsNotIn, double pConf) throws APatternException {
		if (!hasAlreadyScanned) {
			//this will be the probability that a message is confidential given the word is in it, multiplied by the probability that any given message is confidential
			numberOfEmailsWordIsIn++;
			double probabilityWordInEmail = ((double)numberOfEmailsWordIsIn/(numberOfEmailsWordIsIn+numberOfEmailsWordIsNotIn));//*100;
			
			if (weight>-1) {
				double pWC;
				if (numberOfEmailsWordIsIn>1) {
					pWC = ((double)numberOfEmailsWordIsIn/(numberOfEmailsWordIsIn+numberOfEmailsWordIsNotIn))*(aC/100);
				} else if (numberOfEmailsWordIsNotIn>0) {
					pWC = probabilityWordInEmail;
				} else {
					pWC = 0.5;
				}
				double pTwo = ((1-pWC)*(100-pConf));
				double pWCC = pWC*pConf;
				pConfidentialWithWord.add(Math.min(pWCC/(pWCC+pTwo)*weight*100,100));
			} else {
				pConfidentialWithWord.add((double) 100);
				isHighestProb = true;
			}
			
			
			pAveragePerWord.add(aC);
			pWords.add(word);
			pConfTotal.add(pConf);
			//pWordInConfidential.add(probabilityWordInConfidential);
			pNumberOfEmailsWordIsIn.add(numberOfEmailsWordIsIn);
			pNumberOfEmailsWordIsNotIn.add(numberOfEmailsWordIsNotIn);
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
		
			double pThisIsConfidential;
			if (isHighestProb) {
				pThisIsConfidential = 100;
			} else {
				if (size>0) {
					double rTemp;
					double rOne = 1;
					double rTwo = 1;
					for (int i=0; i<size; i++) {
						rTemp=pConfidentialWithWord.get(i);
						rOne*=rTemp;
						//rTwo*=Math.max((100-rTemp)*TOTAL_MULTIPLIER,1);
						rTwo*=(100-rTemp)*TOTAL_MULTIPLIER;
					}
					
					pThisIsConfidential = rOne/(rOne+rTwo)*100;
				} else {
					pThisIsConfidential = 0;
				}
			}

			r = new APatternReport(pThisIsConfidential);
			
			//save value of number of emails
			int wordSize = pWords.size();
			for (int i=0; i<wordSize; i++) {
				int nEmailsWordIn = pNumberOfEmailsWordIsIn.get(i);
				int nEmailsWordNotIn = pNumberOfEmailsWordIsNotIn.get(i);
				int totalWords = nEmailsWordIn+nEmailsWordNotIn;
				double pWC = (pAveragePerWord.get(i)*(nEmailsWordIn-1)+pThisIsConfidential)/nEmailsWordIn;
				//double confWord = (pAveragePerWord.get(i)*(nEmailsWordIn-1)+pWC)/(nEmailsWordIn+pNumberOfEmailsWordIsNotIn.get(i));
				double confWord = ((pConfTotal.get(i)*(totalWords-1))+pThisIsConfidential)/totalWords;
				r.addWordAndSetValues(pWords.get(i),pWC,confWord);
			} 
		} else {
			throw new APatternException();
		}
		
		
		hasAlreadyScanned = true;
		//return the value from this analysis
		
		return r;
	}

	public void addWord(String term, int score, double averageProbability, int numbEmailsIn, int numbEmailsNotIn,
			double probabilityAny) {
		// TODO Auto-generated method stub
		
	}
}