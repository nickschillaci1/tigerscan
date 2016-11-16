package v1;

import db.DatabaseManager;
import gui.ScannerGUI;

public class Main {

	public static void main(String[] args) {
		Config.initConfig();
		DatabaseManager db = new DatabaseManager();
		ContentScanner scanner = new ContentScanner(db);
		ScannerGUI frame = new ScannerGUI(scanner, db);
		//frame.pack();
		frame.setVisible(true);
	}

}

