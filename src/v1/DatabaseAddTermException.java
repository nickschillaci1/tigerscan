

public class DatabaseAddTermException extends Exception {

    private String term;

    public DatabaseAddTermException(String inputTerm) {
	super("The term '"+inputTerm+"' has already been added.");
	term = inputTerm;
    }

    public String getTerm() {
	return term;
    }

}
