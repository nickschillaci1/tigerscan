package v1;

import javax.swing.JFrame;
import javax.swing.JMenuBar;

public class ScannerGUI extends JFrame{

	static final int FRAME_WIDTH = 800;
	static final int FRAME_HEIGHT = 500;
	
	private JMenuBar menuBar;
	
	public ScannerGUI() {
		initializeUI();
		
	}
	
	private void initializeUI() {
		menuBar = new JMenuBar();
		
		setTitle("Tiger Scrum Security Scanner");
		setSize(FRAME_WIDTH, FRAME_HEIGHT);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setJMenuBar(menuBar);
		setIconImage(null);
	}
	
	
	
}
