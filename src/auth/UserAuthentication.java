package auth;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class UserAuthentication {
	private static String usersFilename = "data/users.cfg";
	private static BufferedReader br;
	
	public static User login(String username, String passwordAttempt) throws InvalidCredentialsException {
		try {
			String line = null;
			br = new BufferedReader(new FileReader(usersFilename));
			while((line = br.readLine()) != null) {
				//TODO encrypt/decrypt contents of users file
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
	
	private static boolean passwordMatch(String passwordAttempt, String password) {
		//TODO manipulate passwordAttempt to check against secure password
		return false;
	}
	
}
