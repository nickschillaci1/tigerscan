package v1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class FileHandler {


    /**
     * This will get the contents of a file as a String
     * @param String filename
     * @param Boolean isEncrypted
     * NOTE: this will have added implementation for "Attachement" files
     */
    public static String getStringFromFile(String fileName) throws FileNotFoundException {
        File file = null;
	FileInputStream fIn = null;
	String result = "";
	
	try {
	    file = new File(fileName);
	    fIn = new FileInputStream(file);
	    
	    //read file in
	    byte[] in = new byte[1];
	    try {
		int length = fIn.read(in);
		
		for (int i=0; i<length; i++) {
		    result += Character.toString((char)in[i]);
		}
	    } catch (IOException f) {
		System.out.println(f);
	    }

	    
	} finally {
	    try {
		if (fIn != null) {
		    fIn.close();
		}
	    } catch (IOException e) {
		System.out.println(e);
	    }
	}
	
	return result;
    }

    public static void writeStringToFile(String contents, String fileName) {
	FileOutputStream fOut = null;
	File file;

	try {
	    file = new File(fileName);
	    if (!file.exists()) {
		file.createNewFile();
	    }

	    fOut = new FileOutputStream(file);
	    byte[] contentBytes = contents.getBytes();
	    fOut.write(contentBytes);
	    fOut.flush();
	    fOut.close();
	    
	} catch (IOException e) {
	    System.out.println();
	} finally {
	    try {
		if (fOut!=null) {
		    fOut.close();
		}
	    } catch (IOException e) {
		System.out.println(e);
	    }
	}
    }

}
