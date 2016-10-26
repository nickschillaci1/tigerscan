package v1;

import db.DatabaseManager;

public class Main {

	public static void main(String[] args) {
		DatabaseManager db = new DatabaseManager();
		ContentScanner scanner = new ContentScanner(db);
		ScannerGUI frame = new ScannerGUI(scanner, db);
		//frame.pack();
		frame.setVisible(true);
		
		//SEE METHOD BELOW
		sqlDatabaseTesting(db);
	}

	//TODO ONLY FOR TESTING THE BEHAVIOR OF THE SQLDATABASE. This should be removed when we add the database management functionality.
	public static void sqlDatabaseTesting(DatabaseManager db) {
		try {
			//db.initTable(); //remove comment when creating new database file
			
			//feel free to test DatabaseManager methods here. resulting database contents will be printed below
			
			for(Integer term : db.getTerms().keySet()) {
				System.out.println(term + " :: " + db.getTerms().get(term).intValue());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}

