package v1;

import java.sql.SQLException;

import db.SQLDatabase;

public class Main {

	public static void main(String[] args) {
		SQLDatabase db = new SQLDatabase();
		ContentScanner scanner = new ContentScanner();
		ScannerGUI frame = new ScannerGUI(scanner, db);
		//frame.pack();
		frame.setVisible(true);
		
		//SEE METHOD BELOW
		sqlDatabaseTesting(db);
	}

	//TODO ONLY FOR TESTING THE BEHAVIOR OF THE SQLDATABASE. This should be removed when we add the database management functionality.
	public static void sqlDatabaseTesting(SQLDatabase db) {
		try {
			//db.initTable(); //remove comment when creating new database file
			
			//feel free to test database methods here. resulting information will be printed below
			
			for(String term : db.getTerms().keySet()) {
				System.out.println(term + " :: " + db.getTerms().get(term).intValue());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}

