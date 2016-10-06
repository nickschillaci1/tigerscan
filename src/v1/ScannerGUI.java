package v1;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import java.awt.Font;
import javax.swing.SwingConstants;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.JButton;
import javax.swing.JSplitPane;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;

import java.awt.FlowLayout;
import javax.swing.JList;
import javax.swing.Box;
import javax.swing.AbstractListModel;
import java.awt.Component;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.JScrollPane;


/**
 * Graphic interface to operate the security scanner
 * 
 * TODO -- JList<String> and DefaultListModel<String> instead of
 * 		JList and DefaultListModel. breaks window builder (do before release)
 * TODO currently operates as intended, but could look cleaner
 * TODO program will run without gui from command line (work with Main/Scanner)
 * @author Nick Schillaci
 *
 */
public class ScannerGUI extends JFrame{

	static final String TITLE = "Security Scanner";
	static final String TITLE_FULL = "Team Tiger Security Scanner";
	static final int FRAME_WIDTH = 400;
	static final int FRAME_HEIGHT = 300;
	
	private ArrayList<String> filenames;
	
	public ScannerGUI() {
		initializeUI();
		filenames = new ArrayList<String>();
	}
	
	private void initializeUI() {
		
		this.setTitle(TITLE);
		this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		//this.setJMenuBar(mainMenuBar());
		try {
			this.setIconImage(ImageIO.read(new File("res/icon.png")));
		} catch (IOException errIcon) {
			errIcon.printStackTrace();
		}
		Toolkit tk = Toolkit.getDefaultToolkit();
		int screenWidth = tk.getScreenSize().width;
		int screenHeight = tk.getScreenSize().height;
		setLocation(screenWidth/3, screenHeight/3);
		setContentPane(mainPanel());
		
	}
	
	/**
	 * unused as of right now
	 */
	private JMenuBar mainMenuBar() {
		JMenuBar menu = new JMenuBar();
		//TODO add menu and items to menu bar
		return menu;
	}
	
	private JPanel mainPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(northPanel(), BorderLayout.NORTH);
		panel.add(southPanel(), BorderLayout.SOUTH);
		panel.add(centerPanel(), BorderLayout.CENTER);
		return panel;
	}
	
	private JPanel northPanel() {
		JPanel nPanel = new JPanel();
		//nPanel.setLayout(new BoxLayout(nPanel, BoxLayout.PAGE_AXIS));
		nPanel.setBorder(new EmptyBorder(10, 0, 10, 10));
		
		JLabel nLabel = new JLabel(TITLE_FULL);
		nLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		nPanel.add(nLabel);
		return nPanel;
	}
	
	private JPanel centerPanel() {
		DefaultListModel listModel = new DefaultListModel();
		
		JPanel cPanel = new JPanel();
		cPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel fileListPanel = new JPanel();
		fileListPanel.setBorder(new EmptyBorder(00, 140, 30, 30));
		cPanel.add(fileListPanel, BorderLayout.CENTER);
		fileListPanel.setLayout(new BoxLayout(fileListPanel, BoxLayout.X_AXIS));
		
		
		
		JList fileListBox = new JList();
		fileListBox.setFixedCellWidth(100);
		//fileListBox.setFixedCellHeight(25);
		fileListBox.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		fileListBox.setModel(listModel);
		fileListBox.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane scrollPane = new JScrollPane();
		
		scrollPane.add(fileListBox);
		scrollPane.setViewportView(fileListBox);
		fileListPanel.add(scrollPane);
		
		JPanel fileActionPanel = new JPanel();
		cPanel.add(fileActionPanel, BorderLayout.EAST);
		fileActionPanel.setLayout(new BoxLayout(fileActionPanel, BoxLayout.Y_AXIS));
		
		JButton fileAddButton = new JButton("Add File");
		fileAddButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		fileActionPanel.add(fileAddButton);
		fileAddButton.setMnemonic(KeyEvent.VK_A);
		fileAddButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				FileDialog fd = new FileDialog(new JFrame(), "Choose file(s) to add", FileDialog.LOAD);
				fd.setVisible(true);
				filenames.add(fd.getDirectory() + fd.getFile()); // ArrayList filenames has the exact directory and file name
				listModel.addElement(/*fd.getDirectory() + */fd.getFile()); // currently displaying file name without directory
			}
		});
		
		JButton fileRemoveButton = new JButton("Remove File");
		fileRemoveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		fileActionPanel.add(fileRemoveButton);
		fileRemoveButton.setMnemonic(KeyEvent.VK_R);
		fileRemoveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if (!fileListBox.isSelectionEmpty()) {
					filenames.remove(fileListBox.getSelectedIndex());
					listModel.remove(fileListBox.getSelectedIndex());
				}	
				else
					System.out.println("No file selected.");
			}
		});
		
		JPanel fileScanPanel = new JPanel();
		cPanel.add(fileScanPanel, BorderLayout.SOUTH);
		fileScanPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JButton fileScanButton = new JButton("Scan All");
		fileScanPanel.add(fileScanButton);
		fileScanButton.setMnemonic(KeyEvent.VK_S);
		fileScanButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				for(int i = 0; i < filenames.size(); i++) {
					System.out.println("Scanning file: " + filenames.get(i)); // exact directory and file name
					System.out.println("Scanning file (simple): " + listModel.getElementAt(i).toString()); // file name only
				}
				if(filenames.size() == 0)
					System.out.println("No files to scan.");
			}
		});
		
		return cPanel;
	}
	
	private JPanel southPanel() {
		JPanel sPanel = new JPanel();
		JLabel labelVersion = new JLabel("Version 0.7"); // arbitrary, could use a global version number preferably
		labelVersion.setForeground(Color.GRAY);
		labelVersion.setHorizontalAlignment(SwingConstants.LEFT);
		labelVersion.setFont(new Font("Tahoma", Font.PLAIN, 9));
		sPanel.add(labelVersion);
		return sPanel;
	}
	
	// this won't be needed when we can add the scanner to the gui
	public ArrayList<String> getFilesToScan() {
		return filenames;
	}
	
	
}
