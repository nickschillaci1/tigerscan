package auth;

/**
 * This class is used to identify the properties of a logged-in user
 * @author Nick Schillaci
 */
public class User {
	
	private String username;
	private boolean admin;
	
	/**
	 * Returns a User object representative of the currently logged-in user
	 * @param username
	 * @param admin
	 */
	public User(String username, boolean admin) {
		this.username = username;
		this.admin = admin;
	}
	
	/**
	 * Return whether or not the user is an administrator
	 * @return
	 */
	public boolean isAdmin() {
		return admin;
	}
	
}
