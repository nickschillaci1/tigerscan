package v1;

import db.DatabaseManager;

public class Main {

	public static void main(String[] args) {
		DatabaseManager db = new DatabaseManager();
		ContentScanner scanner = new ContentScanner(db);
		ScannerGUI frame = new ScannerGUI(scanner, db);
		//frame.pack();
		frame.setVisible(true);
	}

}

