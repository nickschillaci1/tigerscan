package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class ConsoleLauncher {

	private ContentScanner scanner;
	
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
