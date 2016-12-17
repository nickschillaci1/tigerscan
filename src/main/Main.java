package main;

import db.DatabaseManager;
import gui.ScannerGUI;

public class Main {

	private static final String VERSION = "0.5";
	
	/**
	 * Main entry point to the program. Activates console version if a filename parameter is specified in the command-line
	 * @param args
	 */
	public static void main(String[] args) {
		Config.initConfig();
		DatabaseManager db = new DatabaseManager();
		ContentScanner scanner = new ContentScanner(db);
		if (args.length != 0)
			new ConsoleLauncher(args, scanner, VERSION);
		else {
			ScannerGUI frame = new ScannerGUI(scanner, db, VERSION);
			//frame.pack();
			frame.setVisible(true);
		}
	}

}

