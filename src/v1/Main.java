package v1;

import java.io.IOException;

import db.DatabaseManager;
import gui.ScannerGUI;

public class Main {

	public static void main(String[] args) {
		DatabaseManager db = new DatabaseManager();
		ContentScanner scanner = new ContentScanner(db);
		ScannerGUI frame = new ScannerGUI(scanner, db);
		Config.initConfig();
		//frame.pack();
		frame.setVisible(true);
	}

}

