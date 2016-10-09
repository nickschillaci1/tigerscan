package v1;

import java.io.File;
import java.io.FileInputStream;
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
    public static String getStringFromFile(String fileName, boolean isEncrypted) throws FileNotFoundException {
    	ArrayList<Bytes> bytes = new ArrayList<Bytes>();
	
	File file = null;
	FileInputStream fIn = null;
	
	try {
	    file = new File(fileName);
	    fIn = new FileInputStream(file);
	    
	    //read file in
	    byte temp;
	    
	    while ((temp = fIn.read()) != -1) {
		bytes.add(temp);
	    }
	} finally {
	    if (fIn != null) {
		fIn.close();
	    }
	}

	if (isEncrypted) {
	    //handle encryption
	}

	
	return byteToString(bytes);
    }

}
