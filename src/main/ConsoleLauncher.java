package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Launcher for the console version of the program that is intended to be called from an external email provider like Outlook
 * @author Nick Schillaci
 */
public class ConsoleLauncher {

	private ContentScanner scanner;
	
	/**
	 * Instantiate the console launcher object and organize the files to be scanned from the command-line
	 * @param args
	 * @param scanner
	 * @param version
	 */
	public ConsoleLauncher(String[] args, ContentScanner scanner, String version) {
		this.scanner = scanner;
		
		System.out.println(new String(new char[70]).replace("\0", "-"));
		System.out.println("TigerScan  -  build version " + version + "");
		System.out.println(new String(new char[70]).replace("\0", "-"));
		
		ArrayList<String> filenames = new ArrayList<String>();
		for(int i=0; i < args.length; i++) {
			filenames.add(args[i]);
		}
		this.scanFiles(filenames);
	}
	
	/**
	 * Scan files specified by the command-line and return if the program successfully runs
	 * @param filenames
	 * @return
	 */
	private boolean scanFiles(ArrayList<String> filenames) {
		HashMap<String,Double> r = scanner.scanFiles(filenames);
		String sReport = "";
		int size = r.size();
		String[] fileNames = r.keySet().toArray(new String[0]);
		
		for (int i=0; i<size; i++) {
			sReport+=fileNames[i]+": "+r.get(fileNames[i])+"\n";
		}
		try {
			Config.emailScanned();
		} catch (IOException e) {
			System.err.println("Error accessing config file.");
		}
		System.out.println("Scanning complete:\n"+sReport);
		return true;
	}
	
	
}
