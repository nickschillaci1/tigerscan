import java.ArrayList;


public class DatabaseAddTermException extends Exception {

    private ArrayList<String> term;

    public DatabaseAddTermException(String inputTerm) {
	super("The term '"+inputTerm+"' has already been added.");
	term = inputTerm;
    }

    public DatabaseAddTermException(ArrayList<String> inputTerm) {
	String error = "";
        int length = inputTerm.size();
        for (int i=0; i<length; i++) {
            error+=inputTerm.get(i)+", ";
        }
	super("The terms "+error+" have already been added.");

	term = inputTerm;
    }

    public ArrayList<String> getTerms() {
	return term;
    }

}
