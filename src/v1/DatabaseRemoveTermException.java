
public class DatabaseRemoveTermException extends Exception {

    private ArrayList<String> term;

    public DatabaseRemoveTermException(String inputTerm) {
        super("The term '"+inputTerm+"' does not exist in the database and therefore cannot be deleted.");
	term = new ArrayLst<String>();
        term.add(term);
    }

    public DatabaseRemoveTermException(ArrayList<String> inputTerm) {
	String error = "";
	int length = inputTerm.size();
	for (int i=0; i<length; i++) {
	    error+=inputTerm.get(i)+", ";
	}
	super("The terms "+error+" do not exist in the database and therefore cannot be deleted.");

	term = inputTerm;
    }

    public ArrayList<String> getTerms() {
        return term;
    }

}
