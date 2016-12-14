package auth;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Pattern;

import main.CryptoUtility;
import main.EventLog;

/**
 * This class will handle User authentication to differentiate users from administrators and apply necessary local security restrictions
 * @author Nick Schillaci
 */
public class UserAuthentication {
	private static final String usersFilename = "data/users.cfg";
	private static BufferedReader br;
	private static BufferedWriter bw;
	private static final String PASSREQ_REGEX = "^[a-zA-Z0-9]";
	private static final int PASSREQ_MIN_SIZE = 8;
	private static final int SALT_LENGTH = 5;
	//The default administrator entry is pre-created by developers. DO NOT EDIT THIS INFORMATION
	private static final String DEFAULT_ADMINISTRATOR_ENTRY = "QdHtDHgoF5e4rbOUawedTN7a5fRxhsn/COEP1jacQUR/I9yb7/tiEsx5n9fCgNqVw0tAUpNZtFdS/uy0BHzFgg==";
	
	/**
	 * Attempt to log in with supplied credentials and return matching User object.
	 * @param username
	 * @param passwordAttempt
	 * @return logged-in User object
	 * @throws InvalidCredentialsException if no username/password match is found
	 * @throws DefaultLoginChangeNeededException 
	 * @throws IOException 
	 */
	public static User login(String username, char[] passwordAttempt) throws InvalidCredentialsException, IOException {
		String passwordAttemptString = new String(passwordAttempt);
		if(!validPassword(passwordAttemptString)) { //don't even use resources to check if invalid passwords are correct
			throw new InvalidCredentialsException();
		}
		String line = null;
		br = new BufferedReader(new FileReader(usersFilename));
		while((line = br.readLine()) != null) {
			line = CryptoUtility.decryptString(line);
			String entry[] = line.split(","); //entry[0] is user name, entry[1] is secure password, entry[2] is administrator status
			boolean admin;
			if(username.equals(entry[0])) {
				if(passwordMatch(passwordAttemptString, entry[1])) {
					if(entry[2].equals("admin"))
						admin = true;
					else
						admin = false;
					br.close();
					EventLog.writeUserLoggedIn(username);
					return new User(entry[0], admin); //return object that represents logged-in user
				}
			}
		}
		br.close();
		throw new InvalidCredentialsException();
	}
	
	/**
	 * Create a new administrator with submitted credentials and record it in the users file
	 * @param username
	 * @param pass1
	 * @param pass2
	 * @param admin
	 * @return newly created logged-in User object
	 * @throws InvalidCredentialsException if pass1 and pass2 do not match (password confirmation)
	 * @throws IOException 
	 */
	public static User newDefaultAdmin(String username, char[] pass1, char[] pass2) throws InvalidCredentialsException, IOException {
		String pass1String = new String(pass1);
		String pass2String = new String(pass2);
		if(pass1String.equals(pass2String) && validPassword(pass1String)) {
			new File(usersFilename); //create new users file
				bw = new BufferedWriter(new FileWriter(usersFilename));
				bw.write(CryptoUtility.encryptString(username + "," + CryptoUtility.encryptString(saltedString(pass1String)) + "," + "admin"));
				bw.newLine();
				bw.close();
				EventLog.writeAdminCredentialsChanged(username);
			return new User(username, true); //return object that represents newly created, logged-in administrator
		}
		else
			throw new InvalidCredentialsException();
	}
	
	/**
	 * Verify that the information entered is that of the system's default administrator.
	 * Immediately following verification, this information is changed for security purposes.
	 * @param username
	 * @param passwordAttempt
	 * @return
	 */
	public static boolean verifyDefaultAdmin(String username, char[] passwordAttempt) {
		String passwordAttemptString = new String(passwordAttempt);
		String line = CryptoUtility.decryptString(DEFAULT_ADMINISTRATOR_ENTRY);
		String entry[] = line.split(","); //entry[0] is user name, entry[1] is secure (encrypted) password, entry[2] is administrator status
		if(username.equals(entry[0])) {
			if(passwordMatch(passwordAttemptString, entry[1])) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Check the password attempt during login process against the saved password and return true if it matches.
	 * The recorded password itself is never decrypted, so it remains secure even if the password attempt is incorrect. 
	 * @param passwordAttempt
	 * @param password
	 * @return
	 */
	private static boolean passwordMatch(String passwordAttempt, String password) {
		String temp = CryptoUtility.encryptString(saltedString(passwordAttempt));
		return temp.equals(password); //temporary
	}
	
	/**
	 * Check the String the user tries to use to create a new password to ensure it matches security requirements
	 * @param password
	 * @return
	 */
	private static boolean validPassword(String password) {
		return password.length() >= PASSREQ_MIN_SIZE;
		//return password.length() >= PASSREQ_MIN_SIZE && Pattern.matches(PASSREQ_REGEX, password); //temporarily disabling character requirements
	}
	
	/**
	 * Return a string that has been salted to enhance security of authentication
	 * Takes the last $SALT_LENGTH$ digits of a password, reverses them, and appends them to the original to create a new password
	 * @param original
	 * @return
	 */
	private static String saltedString(String original) {
		StringBuilder salt = new StringBuilder();
		for(int i = 0; i < SALT_LENGTH; i++) {
			salt.append(original.charAt(original.length() - i - 1));
		}
		return original + salt.toString();
	}
	
	
	
}
