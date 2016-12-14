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


}
