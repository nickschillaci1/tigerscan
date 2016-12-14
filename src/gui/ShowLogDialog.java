package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import main.CryptoUtility;
import main.EventLog;

/**
 * This dialog box serves the purpose of displaying the event log associated with the program
 * @author Zackary Flake
 */
public class ShowLogDialog extends JDialog {
	
	public ShowLogDialog(JPanel relativePanel, URL icon_url) {
		super((JDialog) null, "Log", true);
		super.setSize(500, 500);
		ImageIcon icon = new ImageIcon(icon_url);
		super.setIconImage(icon.getImage());
		JDialog dialog = this; //reference is necessary for disposing the window within the actionlistener
		
		try{
		File logFile = new File(EventLog.getLogFilename());
		FileReader fr = new FileReader(logFile);
		BufferedReader br = new BufferedReader(fr);
		
		String line = br.readLine();
		String log = "";
		while(line != null){
			log += "" + CryptoUtility.decryptString(line) + "\n";
			line = br.readLine();
		}
		br.close();

		JTextArea logArea = new JTextArea(25,40);
		logArea.setText(log);
		logArea.setEditable(false);
		
		JPanel logPanel = new JPanel();
		JScrollPane logPane = new JScrollPane(logArea);
		logPanel.add(logPane);
		
		JButton closeButton = new JButton("Close");
		closeButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				dialog.dispose();
			}
		});
		logPanel.add(closeButton);
		super.add(logPanel);
		
		super.setLocationRelativeTo(relativePanel);
		super.setVisible(true);	
		}
		catch(Exception excep)
		{
			JOptionPane.showMessageDialog(dialog, "Event Log Not Found", "Unable to Show Log", JOptionPane.ERROR_MESSAGE);
		}
	}
	
}
