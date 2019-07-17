package gui;

import java.awt.Color;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import base.Engine;
import cppImp.CharWithColor;
import cppImp.ColorJNI;

public class SwingMainWindow extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	private static final Color DEFAUL_COLOR = Color.darkGray;
	ScheduledExecutorService executor;

	JTextPane textPane;
	Map<Integer, CharWithColor> coloredTextMap;

	JFrame frame;
	boolean flagHighlight;

	SwingMainWindow() {
		coloredTextMap = new LinkedHashMap<>();
		frame = new JFrame("Highlight editor");
		flagHighlight = true;

		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			System.out.println(e);
		}
		frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		textPane = new JTextPane();
		JScrollPane scrollPane = new JScrollPane(textPane);

		JMenuBar menuBar = new JMenuBar();
		JMenu menuEdit = new JMenu("Edit");
		String[] listEdit = { "Highlight", "UnHighlightAll" };
		createMenuItems(menuEdit, listEdit);
		menuBar.add(menuEdit);
		frame.setJMenuBar(menuBar);

		frame.add(scrollPane);
		int size = 500;
		frame.setSize(size, size);
		Point center = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
		frame.setBounds(center.x - size / 2, center.y - size / 2, size, size);
		frame.setVisible(true);
	}

	public static void createAndShowGUI() {
		SwingMainWindow gui = new SwingMainWindow();
		gui.startUpdater();
	}

	public void actionPerformed(ActionEvent event) {
		String actionComm = event.getActionCommand();
		if (actionComm.equals("Highlight")) {
			highlight();
		} else if (actionComm.equals("UnHighlightAll")) {
			unHighlightAll();
		}
	}

	private void createMenuItems(JMenu menu, String[] list) {
		for (String string : list) {
			JMenuItem menuItem = new JMenuItem(string);
			menuItem.addActionListener(this);
			menu.add(menuItem);
		}
	}

	private AttributeSet changeStyle(Color color) {
		StyleContext styleContext = StyleContext.getDefaultStyleContext();
		AttributeSet attrSet = styleContext.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, color);
		return attrSet;
	}

	private void appendToPane(JTextPane textPane, String msg, Color color) {
		AttributeSet attrSet = changeStyle(color);

		int len = textPane.getDocument().getLength();
		textPane.setCaretPosition(len);
		textPane.setCharacterAttributes(attrSet, false);
		textPane.replaceSelection(msg);
	}

	private void highlight() {
		flagHighlight = true;
		updatePane();
	}

	private void unHighlightAll() {
		flagHighlight = false;
		String input = textPane.getText();
		textPane.setText("");
		appendToPane(textPane, input, DEFAUL_COLOR);
	}

	private void updateColoredText() {
		if (!flagHighlight) {
			return;
		}
		coloredTextMap = Engine.getCharMap(textPane.getText());
	}

	private void updatePane() {
		if (!flagHighlight) {
			return;
		}
		String input = textPane.getText();
		textPane.setText("");
		int first = 0;
		int last = 0;
		for (int i = 0; i < input.length(); i++) {
			ColorJNI currentColorJNI = coloredTextMap.get(i).getColor();
			if (i < input.length() - 1) {
				ColorJNI nextColorJNI = coloredTextMap.get(i + 1).getColor();
				if (currentColorJNI.r == nextColorJNI.r && currentColorJNI.g == nextColorJNI.g
						&& currentColorJNI.b == nextColorJNI.b) {
					continue;
				}
			}
			last = i + 1;
			appendToPane(textPane, input.substring(first, last),
					new Color(currentColorJNI.r, currentColorJNI.g, currentColorJNI.b));
			first = last;
			// fix caret color to default
			appendToPane(textPane, "", DEFAUL_COLOR);
		}
	}

	private void startUpdater() {
		executor = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {

			@Override
			public Thread newThread(Runnable r) {
				Thread thread = new Thread(r);
				thread.setDaemon(true);// die after gui close
				return thread;
			}
		});
		executor.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				updateColoredText();
				updatePane();
			}
		}, 5, 5, TimeUnit.SECONDS);
	}
}
