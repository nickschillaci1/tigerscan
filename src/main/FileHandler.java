package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileNotFoundException;

/**
 * This class currently handles reading in and writing out text files.  It also has read/write methods for use of the Database that includes encryption
 * All methods in this Class are static
 * @author Brandon Dixon
 * @version 10/11/16
 * 
 *
 */

public class FileHandler {

    private static final String ALG = "AES"; //Advanced Encryption Standard
    private static final String KEYString = "0012408884793915";  //this is a random key
    private static final String dataFileName = "info.info";
	
    /**
     * This will get the contents of a file as a String
     * @param String filename
     * @return String contents of the file
     * @exception FileNotFoundException
     * NOTE: this will have added implementation for "Attachement" files
     */
    public static String getStringFromFile(String fileName) throws FileNotFoundException {
		File file = null;
		FileInputStream fIn = null;
		String result = "";
	
		try {
		    file = new File(fileName);
		    fIn = new FileInputStream(file);
		    byte[] in = new byte[fIn.available()];
		    fIn.read(in);
	
		    result = new String(in);
	
		} catch (IOException a) {
			if (!(a instanceof FileNotFoundException)) {
				System.out.println(a);
			}
		}finally {	
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

    /**
     * This will write a String to a specified file
     * @param String contents to write
     * @param String filename
     */
    public static void writeStringToFile(String contents, String fileName) {
		FileOutputStream fOut = null;
		File file;
	
		try {
		    file = new File(fileName);
		    file.createNewFile();
	
		    fOut = new FileOutputStream(file);
		    fOut.write(contents.getBytes());
	
		} catch (IOException e) {
		    System.out.println(e);
		} finally {
		    try {
			if (fOut!=null) {
				fOut.flush();
			    fOut.close();
			}
		    } catch (IOException e) {
		    	System.out.println(e);
		    }
		}
    }


}