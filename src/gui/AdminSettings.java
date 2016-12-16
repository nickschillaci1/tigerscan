package gui;

import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import db.DatabaseAddTermException;
import db.DatabaseManager;
import db.DatabaseRemoveTermException;
import main.CSVReader;
import main.Config;
import main.EventLog;
import main.Main;

public class AdminSettings{

	private JTable termsTable;
	private CustomTableModel tableModel;
	static final int FRAME_WIDTH = 500;
	static final int FRAME_HEIGHT = 400;
	Toolkit tk = Toolkit.getDefaultToolkit();
	private int screenWidth = tk.getScreenSize().width;
	private int screenHeight = tk.getScreenSize().height;;
	
	public AdminSettings(DatabaseManager db){
				
			    JDialog dbSettings = new JDialog((JDialog) null, "Settings", true);
			    
				dbSettings.setSize(FRAME_WIDTH, FRAME_HEIGHT);
				dbSettings.setLocation(screenWidth/4, screenHeight/4);
				try {
					URL url = Main.class.getResource("/settings.png");
					Image icon = tk.getImage(url);
					dbSettings.setIconImage(icon);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				dbSettings.setLayout(new GridLayout(1, 2));
				
				JPanel leftPanel = new JPanel();
				FlowLayout leftFlow = new FlowLayout();
				leftFlow.setVgap(20);
				leftPanel.setLayout(leftFlow);
				leftPanel.setBorder(new EmptyBorder(-5, 0 , 0, 0));
				
				JButton changeButton = new JButton("Change Classification");
				changeButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent event)
					{
						if (termsTable.getSelectedRow() != -1) {
							String term = (String)termsTable.getValueAt(termsTable.getSelectedRow(), 0);
							double newScore = Double.parseDouble((JOptionPane.showInputDialog(dbSettings, "Enter new classification score for the term \"" + term + "\"", "Remove Term", JOptionPane.PLAIN_MESSAGE)));
							//if (newScore != term && newScore != null) {
							if (newScore != 0) {
								try {
									db.changeScore(term, newScore);
									tableModel.setValueAt(newScore, termsTable.getSelectedRow(), 1);
									tableModel.fireTableDataChanged();
									EventLog.writeTermClassificationChanged(new File(db.getDatabaseFilename()).getName());
								} catch (NumberFormatException e) {
									JOptionPane.showMessageDialog(dbSettings, "Invalid score!", "Error", JOptionPane.ERROR_MESSAGE);
								} catch (SQLException | DatabaseAddTermException e) {
									JOptionPane.showMessageDialog(dbSettings, "Internal database error!", "Error", JOptionPane.ERROR_MESSAGE);
								}
							}
						}
					}
				});
				changeButton.setPreferredSize(new Dimension(160, 30));
				
				JButton renameButton = new JButton("Rename Term");
				renameButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent event)
					{
						if (termsTable.getSelectedRow() != -1) {
							String term = (String) termsTable.getValueAt(termsTable.getSelectedRow(), 0);
							String newName = (String) JOptionPane.showInputDialog(dbSettings, "Enter new name for the term \"" + term + "\"", "Remove Term", JOptionPane.PLAIN_MESSAGE, null, null, term);
							//if (newName != term && newName != null) {
							if (!newName.equals("")) {
								try {
									db.addTerm(newName, db.getTerms().get(term));
									db.removeTerm(term);
									tableModel = new CustomTableModel(db.getTerms());
									termsTable.setModel(tableModel);
									//tableModel.fireTableDataChanged();
									EventLog.writeTermRenamed(new File(db.getDatabaseFilename()).getName());
								} catch (NumberFormatException | DatabaseRemoveTermException e) {
									JOptionPane.showMessageDialog(dbSettings, "An error occured trying to rename the term!", "Error", JOptionPane.ERROR_MESSAGE);
								} catch (DatabaseAddTermException e) {
									JOptionPane.showMessageDialog(dbSettings, "A term with that name already exists!", "Error", JOptionPane.ERROR_MESSAGE);
								}
							}
						}
					}
				});
				renameButton.setPreferredSize(new Dimension(160, 30));
				
				JButton addButton = new JButton("Add Term");
				addButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent event)
					{	
						//String term = JOptionPane.showInputDialog(dbSettings, "Input term to add:", "Add Term", JOptionPane.PLAIN_MESSAGE);
					//	if (term != null) {
						JPanel termPanel = new JPanel();
						termPanel.setLayout(new GridLayout(1,3));

						JPanel termLeftPanel = new JPanel();
						termLeftPanel.setLayout(new GridLayout(2,1));
						termLeftPanel.setBorder(new EmptyBorder(0,15,0,15));
						JLabel termLabel = new JLabel("Term");
						JTextField termField = new JTextField(10);
						termLeftPanel.add(termLabel);
						termLeftPanel.add(termField);
						
						JPanel termRightPanel = new JPanel();
						termRightPanel.setLayout(new GridLayout(2,1));
						termRightPanel.setBorder(new EmptyBorder(0,15,0,15));
						JLabel classLabel = new JLabel("Classification Score");
						JTextField classField = new JTextField(10);
						termRightPanel.add(classLabel);
						termRightPanel.add(classField);
						
						termPanel.add(termLeftPanel);
						termPanel.add(termRightPanel);
						
						//Set an "Override" so any email containing this term is immediately flagged.
						JCheckBox overCheck = new JCheckBox("IMMEDIATELY FLAG EMAILS");
						overCheck.addActionListener(new ActionListener(){
							public void actionPerformed(ActionEvent ev){
								classField.setEnabled(!classField.isEnabled());
							}
						});
						
						termPanel.add(overCheck);
						JDialog termDialog = new JDialog(dbSettings, "Add Term", true);
						termDialog.setLayout(new GridLayout(2,1));
						termDialog.add(termPanel);
						
						JButton addButton = new JButton("Add");
						addButton.addActionListener(new ActionListener() {
							boolean closeWindowFlag;
							public void actionPerformed(ActionEvent ev) {
								try{
									String term = termField.getText();
									double classScore;
									if(classField.isEnabled())
										classScore = Double.parseDouble(classField.getText());
									else
										classScore = -1;
									db.addTerm(term, classScore);
									tableModel = new CustomTableModel(db.getTerms());
									termsTable.setModel(tableModel);
									//tableModel.fireTableDataChanged();
									EventLog.writeTermAdded(new File(db.getDatabaseFilename()).getName());
									closeWindowFlag = true;
								}
								catch(DatabaseAddTermException e)
								{
									JOptionPane.showMessageDialog(dbSettings, "Term already exists!", "Error", JOptionPane.ERROR_MESSAGE);
									closeWindowFlag = false;
								}
								catch(NumberFormatException e)
								{
									JOptionPane.showMessageDialog(dbSettings, "Invalid score!", "Error", JOptionPane.ERROR_MESSAGE);
									closeWindowFlag = false;
								}
								if(closeWindowFlag) {
									termDialog.dispose(); // close dialog box after adding term
								}
							}
						});
						JPanel addPanel = new JPanel();
						addPanel.add(addButton);
						termDialog.add(addPanel);
						
						termDialog.setSize(650, 110);
						termDialog.setLocation(screenWidth/3, screenHeight/3);
						termDialog.setVisible(true);
					//}
					}
				});
				addButton.setPreferredSize(new Dimension(160, 30));
				
				JButton removeButton = new JButton("Remove Term");
				removeButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent event)
					{
						if (termsTable.getSelectedRow() != -1) {
							String term = (String)termsTable.getValueAt(termsTable.getSelectedRow(), 0);
							int response = JOptionPane.showConfirmDialog(dbSettings, "Are you sure you want to remove the term \"" + term + "\"?", "Remove Term", JOptionPane.YES_NO_OPTION);
							if (response == JOptionPane.YES_OPTION) {
								try{
									db.removeTerm(term);
									tableModel = new CustomTableModel(db.getTerms());
									termsTable.setModel(tableModel);
									//tableModel.fireTableDataChanged();
									EventLog.writeTermRemoved(new File(db.getDatabaseFilename()).getName());
								}
								catch(DatabaseRemoveTermException e)
								{
									JOptionPane.showMessageDialog(dbSettings, "Term does not exist!", "Error", JOptionPane.ERROR_MESSAGE);
								}
							}
						}
					}
				});
				removeButton.setPreferredSize(new Dimension(160, 30));
				
				JButton removeAllButton = new JButton("Remove All Terms");
				removeAllButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent event)
					{
						int response = JOptionPane.showConfirmDialog(dbSettings, "Are you sure you want to remove all terms from the database?", "Remove All Terms", JOptionPane.YES_NO_OPTION);
						if (response == JOptionPane.YES_OPTION) {

							db.removeAllTerms();
							tableModel = new CustomTableModel(db.getTerms());
							termsTable.setModel(tableModel);
							EventLog.writeTermRemovedAll(new File(db.getDatabaseFilename()).getName());
						}
					}
				});
				removeAllButton.setPreferredSize(new Dimension(160, 30));
				
				JButton importButton = new JButton("Import Terms");
				importButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent event)
					{
						FileDialog fd = new FileDialog(new JFrame(), "Choose file to import terms from", FileDialog.LOAD);
						fd.setVisible(true);
						if (fd.getFile() != null) {
							if (fd.getFile().toLowerCase().endsWith(".csv")) { //currently only supports CSV files
								int response = JOptionPane.showConfirmDialog(dbSettings, "Are you sure you want to import all terms from this file?", "Import Terms", JOptionPane.YES_NO_OPTION);
								if (response == JOptionPane.YES_OPTION) {
									for (String line : CSVReader.getLinesFromFile(fd.getDirectory() + fd.getFile())) {
										try {
											db.addTerm(CSVReader.getContentFromLine(line)[CSVReader.TERM], Integer.parseInt(CSVReader.getContentFromLine(line)[CSVReader.SCORE]));
										} catch (NumberFormatException e) {
											e.printStackTrace();
										} catch (DatabaseAddTermException e) {
											JOptionPane.showMessageDialog(dbSettings, "Term already exists!", "Error", JOptionPane.ERROR_MESSAGE);
										}
									}
									tableModel = new CustomTableModel(db.getTerms());
									termsTable.setModel(tableModel);
									//tableModel.fireTableDataChanged();
									EventLog.writeTermImported(new File(db.getDatabaseFilename()).getName(), fd.getDirectory() + fd.getFile());
								}
							}
							else JOptionPane.showMessageDialog(dbSettings, "File format not supported! Please select a '.CSV' file.", "Error", JOptionPane.ERROR_MESSAGE);
						}
					}
				});
				importButton.setPreferredSize(new Dimension(160, 30));
				
				JButton databaseButton = new JButton("Database Settings");
				databaseButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						JDialog databaseDialog = new JDialog(dbSettings, "Select Option", true);
						databaseDialog.setSize(400, 90);
						databaseDialog.setLocation(screenWidth/3, screenHeight/3);
						
						JPanel databasePanel = new JPanel();
						databasePanel.setLayout(new FlowLayout());
						databasePanel.setBorder(new EmptyBorder(5,0,0,0));
						
						JButton renameDataButton = new JButton("Rename Database");
						renameDataButton.addActionListener(new ActionListener(){
							public void actionPerformed(ActionEvent e){
								File oldDBFile = new File(db.getDatabaseFilename());
								String oldAbsoluteFilename = oldDBFile.getAbsolutePath();
								String directory = oldAbsoluteFilename.substring(0, oldAbsoluteFilename.lastIndexOf(File.separator)+1);
								String filename = (String ) JOptionPane.showInputDialog(databasePanel, "Enter new name for database file:", 
																			"Input New Filename", JOptionPane.PLAIN_MESSAGE, null, null, oldDBFile.getName());
								if(filename != null)
								{
									String extension = new File(filename).getName().substring(filename.length() - 3);
									if(!extension.equalsIgnoreCase(".db")) filename += ".db";
									File newDBFile = new File(directory+filename);
									String newAbsoluteFilename = newDBFile.getAbsolutePath();
									if(!newDBFile.exists()) {
										try{
											db.closeSQLConnection(); //close connection to current file name
											if (oldDBFile.renameTo(newDBFile)) { //returns true if FileSystem.rename was successful
												Config.setDatabaseFilename(newAbsoluteFilename);
												db.setDatabaseFilename(newAbsoluteFilename);
												db.initSQLConnection(); //initialize connection to new file name
												EventLog.writeDatabaseRenamed(oldDBFile.getName(), newDBFile.getName());
												JOptionPane.showMessageDialog(databasePanel, "Database Renamed to " + newAbsoluteFilename, "Database Renamed", JOptionPane.PLAIN_MESSAGE);
											}
											else {
												db.initSQLConnection(); //re-initialize connection to the old file
												JOptionPane.showMessageDialog(dbSettings, "An error occured; Please restart the program and try again.", "Database Error", JOptionPane.ERROR_MESSAGE);
											}
										}
										catch(SQLException | IOException exception)
										{
											JOptionPane.showMessageDialog(dbSettings, "Unable to rename database!", "Database Error", JOptionPane.ERROR_MESSAGE);
											try {
												Config.setDatabaseFilename(oldAbsoluteFilename);
												db.initSQLConnection();
											} catch (IOException | SQLException e1) {
												//should never fail, just reverting back to what it was
											}
										
										}
									}
									else {
									JOptionPane.showMessageDialog(dbSettings, "A file already exists with that name!", "Database Error", JOptionPane.ERROR_MESSAGE);
									}
								}
							}
						});
						renameDataButton.setPreferredSize(new Dimension(160, 30));
						
						JButton changeDataButton = new JButton("Change Database");
						changeDataButton.addActionListener(new ActionListener(){
							public void actionPerformed(ActionEvent e){
								FileDialog fd = new FileDialog(new JFrame(), "Select database file", FileDialog.LOAD);
								fd.setVisible(true);
								String filename = fd.getFile();
								if(filename != null){
									String extension = new File(filename).getName().substring(filename.length() - 3);
									if(!extension.equalsIgnoreCase(".db")) {
										JOptionPane.showMessageDialog(null, "Not a valid database file!", "Database Error", JOptionPane.ERROR_MESSAGE);
									}
									else {
										String oldAbsoluteFilename = new File(db.getDatabaseFilename()).getAbsolutePath();
										String newAbsoluteFilename = new File(fd.getDirectory() + filename).getAbsolutePath();
										try{
											db.closeSQLConnection(); //close connection to current file name
											Config.setDatabaseFilename(newAbsoluteFilename);
											db.setDatabaseFilename(newAbsoluteFilename);
											db.initSQLConnection(); //initialize connection to new file name
											db.updateLocalTerms();
											tableModel = new CustomTableModel(db.getTerms());
											termsTable.setModel(tableModel);
											//tableModel.fireTableDataChanged();
											EventLog.writeDatabaseChanged(oldAbsoluteFilename, newAbsoluteFilename);
										}
										catch(SQLException | IOException exception)
										{
											JOptionPane.showMessageDialog(null, "Unable to change database!", "Database Error", JOptionPane.ERROR_MESSAGE);
											try {
												Config.setDatabaseFilename(oldAbsoluteFilename);
												db.initSQLConnection();
											} catch (IOException | SQLException e1) {
												//should never fail, just reverting back to what it was
											}
										}
									}
								}
							}
						});
						changeDataButton.setPreferredSize(new Dimension(160, 30));
						
						databasePanel.add(renameDataButton);
						databasePanel.add(changeDataButton);
						databaseDialog.add(databasePanel);
						databaseDialog.setVisible(true);
					}
				});
				databaseButton.setPreferredSize(new Dimension(160, 30));
				
				leftPanel.add(addButton);
				leftPanel.add(renameButton);
				leftPanel.add(removeButton);
				leftPanel.add(removeAllButton);
				leftPanel.add(changeButton);
				leftPanel.add(importButton);
				leftPanel.add(databaseButton);
					
				JPanel rightPanel = new JPanel();
				
				JLabel termsLabel = new JLabel("Database Terms");
				tableModel = new CustomTableModel(db.getTerms());
				termsTable = new JTable(tableModel);
				JScrollPane tableScroll = new JScrollPane(termsTable);
				tableScroll.setPreferredSize(new Dimension(200, 300));
				
				
				rightPanel.add(termsLabel);
				rightPanel.add(tableScroll);
				
				dbSettings.add(leftPanel);
				dbSettings.add(rightPanel);
				dbSettings.setAlwaysOnTop(true);
				dbSettings.setVisible(true);
	}
	
}
