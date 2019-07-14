package base;

import cppImp.ColorJNI;
import cppImp.highlightIDE;
import gui.SwingMainWindow;

public class Engine {
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				SwingMainWindow.createAndShowGUI();
			}
		});
	}

	public static ColorJNI[] getColor(String input) {
		return highlightIDE.highlightCPP(input, false);

	}
}
