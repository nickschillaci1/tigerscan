
public class DatabaseRemoveTermException extends Exception {

    private String term;

    public DatabaseRemoveTermException(String inputTerm) {
        super("The term '"+inputTerm+"' does not exist in the database and therefore cannot be deleted.");
        term = inputTerm;
    }

    public String getTerm() {
        return term;
    }

}
