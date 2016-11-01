package v1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class CSVReader {
	
	//Used as indexes for result of getContentFromFile
	public static final int TERM = 0;
	public static final int SCORE = 1;
	
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
	
	public static String[] getContentFromLine(String line) {
		return line.split(",");
	}
	
}
