package v1;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 * Cryptography utility designed for the AES encryption and decryption
 * @author Nick Schillaci
 *
 */
public class CryptoUtility {

	private static final String ALGORITHM = "AES";
	private static final String key = "0aaqdi7fb0k83hdn";
	private static final SecretKeySpec aesKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
	private static Cipher cipher;
	
	/**
	 * Encrypt a String with AES cipher
	 * @param String to encrypt
	 * @return encrypted String
	 */
	public static String encryptString(String s) {
		try {
			cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, aesKey);
			byte[] encryptedBytes = Base64.getEncoder().encode(cipher.doFinal(s.getBytes())); //encrypt before encoding
			return new String(encryptedBytes);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
			System.err.println("Error encrypting data");
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Decrypt a String with AES cipher
	 * @param Encrypted String to decrypt
	 * @return decrypted String
	 */
	public static String decryptString(String s) {
		try {	
			cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, aesKey);
			byte[] decryptedBytes = Base64.getDecoder().decode(s.getBytes()); //decode before decrypting
			return new String(cipher.doFinal(decryptedBytes));
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
			System.err.println("Error decrypting data");
			e.printStackTrace();
		}
		return null;
	}
	
}
