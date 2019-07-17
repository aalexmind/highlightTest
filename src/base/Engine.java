package base;

import java.util.LinkedHashMap;
import java.util.Map;

import cppImp.CharWithColor;
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
		ColorJNI[] colorJNIArray = { new ColorJNI() };
		try {
			colorJNIArray = highlightIDE.highlightCPP(input, false);
		} catch (java.lang.UnsatisfiedLinkError e) {
			System.out.println(e);
		}
		return colorJNIArray;

	}

	public static Map<Integer, CharWithColor> getCharMap(String input) {
		Map<Integer, CharWithColor> map = new LinkedHashMap<>();
		ColorJNI[] colorJNIArray = getColor(input);
		for (int i = 0; i < colorJNIArray.length; i++) {
			map.put(i, new CharWithColor(input.charAt(i), colorJNIArray[i]));
		}
		return map;
	}
}
