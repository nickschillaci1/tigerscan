package db;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DBHash {
	
	/**
	 * This will add salt to a String and return the hashed version.
	 */
	public static int hashCode(String term) {
		String reverse = "";
		int size = term.length();
		for (int i=0; i<size; i++) {
			reverse+=term.charAt(size-i-1);
		}
		return (term+reverse).hashCode();
	}
	
	/**
	 * This will hash all words in a file and return the index of the new file
	 * @param fileName
	 * @return String new Filename
	 * @throws IOException 
	 */
	public static String hashFile(String fileName) throws IOException {
		InputStream in = new FileInputStream(fileName);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		
		String line = br.readLine();
		StringBuilder sb = new StringBuilder();
		
		while (line != null) {
			sb.append(line).append("\n");
			line = br.readLine();
		}
		
		
	}

}
