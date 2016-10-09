package v1;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;

public class FileHandler {


    /**
     * This will get the contents of a file as a String
     * @param String filename
     * @param Boolean isEncrypted
     * NOTE: this will have added implementation for "Attachement" files
     */
    public static String getStringFromFile(String fileName) throws FileNotFoundException {
		File file = null;
		FileReader fIn = null;
		String result = "";
	
		try {
		    file = new File(fileName);
		    fIn = new FileReader(file);
		    int temp;
		    
		    //read file in
		    while ((temp = fIn.read()) != -1) {
		    	result += Character.toString((char)temp);
		    }
	
	
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

    public static void writeStringToFile(String contents, String fileName) {
		FileWriter fOut = null;
		File file;
	
		try {
		    file = new File(fileName);
		    file.createNewFile();
	
		    fOut = new FileWriter(file);
		    fOut.write(contents);
	
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
