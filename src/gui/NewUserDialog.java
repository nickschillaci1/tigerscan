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
 * This dialog box is intended to be used when the users file was not present and the default administrator credentials have been used for the first time
 * @author Nick Schillaci
 * @author Zackary Flake
 */
public class NewUserDialog extends JDialog {
	
	private User user = null;
	
	public NewUserDialog(JPanel relativePanel, URL icon_url) {
		super((JDialog) null, "Change Administrator Credentials", true);
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
		JLabel passLabelConfirm = new JLabel("Confirm Password");
		JPasswordField passFieldConfirm = new JPasswordField();
		JButton loginButton = new JButton("Log In");
		loginButton.setMnemonic(KeyEvent.VK_L);
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				String usernameString = userText.getText();
				try {
					user = UserAuthentication.newDefaultAdmin(usernameString, passField.getPassword(), passFieldConfirm.getPassword());
					dialog.dispose();
				} catch (InvalidCredentialsException e1) {
					JOptionPane.showMessageDialog((JDialog) null, "Invalid credentials", "Create User Failed", JOptionPane.ERROR_MESSAGE);
				} catch (IOException e2) {
					JOptionPane.showMessageDialog((JDialog) null, "Unable to create a new user because a new file cannot be created \n"
							+ "in the /data/ directory. Check permissions.", "Create User Failed", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		authPanel.add(userLabel);
		authPanel.add(userText);
		authPanel.add(passLabel);
		authPanel.add(passField);
		authPanel.add(passLabelConfirm);
		authPanel.add(passFieldConfirm);
		
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
	
	/**
	 * Return the user created by the dialog
	 * @return
	 */
	public User getUser() {
		return user;
	}
	
}
