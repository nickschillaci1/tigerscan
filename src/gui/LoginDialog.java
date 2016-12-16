package gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import auth.InvalidCredentialsException;
import auth.User;
import auth.UserAuthentication;
/**
 * This dialog box is used as an interface for the user to log into the system through
 * @author Nick Schillaci
 * @author Zackary Flake
 */
public class LoginDialog extends JDialog {
	
	private User user = null;
	
	public LoginDialog(JPanel relativePanel, URL icon_url, Runnable action) {
		super((JDialog) null, "Login", true);
		super.setLayout(new GridLayout(2,1));
		ImageIcon icon = new ImageIcon(icon_url);
		super.setIconImage(icon.getImage());
		JDialog dialog = this; //reference is necessary for disposing the window within the actionlistener
		
		JPanel authPanel = new JPanel();
		GridLayout authLayout = new GridLayout(4,1);
		authLayout.setVgap(2);
		authPanel.setLayout(authLayout);
		
		JLabel userLabel = new JLabel("Username");
		JTextField userText = new JTextField();
		JLabel passLabel = new JLabel("Password");
		JPasswordField passField = new JPasswordField();
		
		JButton loginButton = new JButton("Log In");
		loginButton.setMnemonic(KeyEvent.VK_L);
		loginButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
				User user = null;
				String usernameString = userText.getText();
				try{
					user = UserAuthentication.login(usernameString, passField.getPassword());
				}
				catch(InvalidCredentialsException e1) //invalid username/password
				{
					JOptionPane.showMessageDialog(dialog, "Invalid credentials", "Log-In Failed", JOptionPane.ERROR_MESSAGE);
				}
				catch(IOException e2) //users file not found
				{
					if (UserAuthentication.verifyDefaultAdmin(usernameString, passField.getPassword())) {
						JOptionPane.showMessageDialog(dialog, "The default administrator information must be changed.\n"
								+ "Please enter new log-in credentials.", "Administrator", JOptionPane.WARNING_MESSAGE);
						
						user = new NewUserDialog(relativePanel, icon_url).getUser();
					}
					else
						JOptionPane.showMessageDialog(dialog, "Log-in information not found.\n"
								+ "Please contact your system administrator.", "Log-In Failed", JOptionPane.ERROR_MESSAGE);
				}
				if(user != null && user.isAdmin())
				{
					dialog.dispose();
					action.run();
				}
			}
		});
		
		authPanel.add(userLabel);
		authPanel.add(userText);
		authPanel.add(passLabel);
		authPanel.add(passField);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(loginButton);
		buttonPanel.setBorder(new EmptyBorder(20, 0, 0, 0));
		
		super.add(authPanel);
		super.add(buttonPanel);
		super.setSize(400, 200);
		super.setLocationRelativeTo(relativePanel);
		super.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		super.setVisible(true);
	}
	
	public User getUser() {
		return user;
	}
	
}
