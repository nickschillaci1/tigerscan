package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Utility class used for interpreting a CSV file for the purpose of adding contained terms to the database
 * @author Nick Schillaci
 */
public class CSVReader {
	
	//Used as indexes for result of getContentFromFile
	public static final int TERM = 0;
	public static final int SCORE = 1;
	
	/**
	 * Return an ArrayList of Strings from each line in the CSV file
	 * @param filename
	 * @return
	 */
	public static ArrayList<String> getLinesFromFile(String filename) {
		ArrayList<String> lines = new ArrayList<String>();
		BufferedReader br = null;
		String line =  "";
		
		try {
			br = new BufferedReader(new FileReader(filename));
			while ((line = br.readLine()) != null) {
				lines.add(line);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return lines;
	}
	
	/**
	 * Return an array of Strings that represent the content from a line delineated by a comma
	 * @param line
	 * @return
	 */
	public static String[] getContentFromLine(String line) {
		return line.split(",");
	}
	
}
