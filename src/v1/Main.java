package v1;

public class Main {

	public static void main(String[] args) {
		ContentScanner scanner = new ContentScanner();
		ScannerGUI frame = new ScannerGUI(scanner);
		//frame.pack();
		frame.setVisible(true);
	}

}