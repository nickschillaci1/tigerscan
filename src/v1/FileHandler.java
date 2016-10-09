package v1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;


public class FileHandler {

    private static final String ALG = "AES"; //Advanced Encryption Standard
    private static final String KEYString = "0012408884793915";  //randomly generated key
    private static final String dataFileName = "info.info";
	
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
    
    public static String getDatabaseString() {
    	File file = new File(dataFileName);
    	
    	if (!file.exists()) {
    		try {
    			file.createNewFile();
    		} catch (IOException e) {
    			System.out.println(e);
    		}
    		return "";
    	} else {
    		FileInputStream fIn = null;
    		byte[] in = new byte[1];
    		
    		//read file here
    		try {
    			fIn = new FileInputStream(file);
    			in = new byte[fIn.available()];
    		    fIn.read(in);
    		} catch (IOException g) {
    			System.out.println(g);
    		} finally {
    			try {
    				if (fIn!=null) {
    					fIn.close();
    				}
    			} catch (IOException h) {
    				System.out.println(h);
    			}
    		}
    		
    		//decrypt here
    		Key key = new SecretKeySpec(KEYString.getBytes(),ALG);
    		Cipher cipher;
    		
    		try {
    			cipher = Cipher.getInstance(ALG);
    		} catch (NoSuchAlgorithmException | NoSuchPaddingException f) {
    			System.out.println(f);
    			return "";
    		}
    		
    		try {
        		cipher.init(Cipher.DECRYPT_MODE,key);
        		return new String(cipher.doFinal(in));
        	} catch (IllegalBlockSizeException | BadPaddingException | InvalidKeyException e) {
        		System.out.println(e);
        	}
    	}
    	
    	return "";
    }
    
    public static void saveDatabaseString(String contents) {
    	File file = new File(dataFileName);
    	
    	//encrypt string
    	Key key = new SecretKeySpec(KEYString.getBytes(),ALG);
		Cipher cipher = null;
		byte[] in = new byte[1];
		
		try {
			cipher = Cipher.getInstance(ALG);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException f) {
			System.out.println(f);
		}
		
		try {
    		cipher.init(Cipher.ENCRYPT_MODE,key);
    		in = cipher.doFinal(in);
    	} catch (IllegalBlockSizeException | BadPaddingException | InvalidKeyException e) {
    		System.out.println(e);
    	}
    	
		//write file
		FileOutputStream fOut = null;
		try {
			
		    fOut = new FileOutputStream(file);
		    fOut.write(in);
	
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
