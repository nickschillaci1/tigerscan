package auth;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * This class will handle User authentication to differentiate users from administrators and apply necessary local security restrictions
 * @author Nick Schillaci
 */
public class UserAuthentication {
	private static String usersFilename = "data/users.cfg";
	private static BufferedReader br;
	
	/**
	 * Attempt to log in with supplied credentials and return matching User object.
	 * @param username
	 * @param passwordAttempt
	 * @return logged-in User object
	 * @throws InvalidCredentialsException if no username/password match is found
	 */
	public static User login(String username, String passwordAttempt) throws InvalidCredentialsException {
		try {
			String line = null;
			br = new BufferedReader(new FileReader(usersFilename));
			while((line = br.readLine()) != null) {
				//TODO encrypt/decrypt contents of users file. I plan to test further before implementing this
				String entry[] = line.split(","); //entry[0] is user name, entry[1] is secure password, entry[2] is administrator status
				boolean admin;
				if(username.equals(entry[0])) {
					if(passwordMatch(passwordAttempt, entry[1])) {
						if(entry[2].equals("admin"))
							admin = true;
						else
							admin = false;
						return new User(entry[0], admin); //return object that represents logged-in user
					}
				}
			}
			br.close();
		} catch (IOException e) {
			System.err.println("Unable to properly access users file.");
		}
		throw new InvalidCredentialsException();
	}
	
	/**
	 * Create a new user with submitted credentials and record it in the users file
	 * @param username
	 * @param pass1
	 * @param pass2
	 * @param admin
	 * @return newly created logged-in User object
	 * @throws InvalidCredentialsException if pass1 and pass2 do not match (password confirmation)
	 */
	public static User newUserLogin(String username, String pass1, String pass2, boolean admin) throws InvalidCredentialsException {
		if(pass1.equals(pass2)) {
			//TODO manipulate password to record secure password instead of direct
			return new User(username, admin); //return object that represents newly created, logged-in user
		}
		else
			throw new InvalidCredentialsException();
	}
	
	/**
	 * Check the password attempt during login process against the saved password and return true if it matches
	 * @param passwordAttempt
	 * @param password
	 * @return
	 */
	private static boolean passwordMatch(String passwordAttempt, String password) {
		//TODO manipulate passwordAttempt to check against secure password
		return false;
	}
	
	
	
}
