package main;

import db.DatabaseManager;
import gui.ScannerGUI;

public class Main {

	private static final String VERSION = "0.3.2";
	
	public static void main(String[] args) {
		Config.initConfig();
		DatabaseManager db = new DatabaseManager();
		ContentScanner scanner = new ContentScanner(db);
		ScannerGUI frame = new ScannerGUI(scanner, db, VERSION);
		//frame.pack();
		frame.setVisible(true);
	}

}

