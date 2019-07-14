package gui;

import java.awt.Color;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.OceanTheme;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.HighlightPainter;

import base.Engine;
import cppImp.ColorJNI;

public class SwingMainWindow2 extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;

	JEditorPane textPane;

	JFrame frame;

	SwingMainWindow2() {
		frame = new JFrame("Highlight editor");

		try {
			// Set Metal look and feel
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			// Set theme to ocean
			MetalLookAndFeel.setCurrentTheme(new OceanTheme());
		} catch (Exception e) {
			System.out.println(e);
		}
		frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		DefaultStyledDocument document = new DefaultStyledDocument();
		textPane = new JEditorPane();
		textPane.setContentType("text/html");
		JMenuBar menuBar = new JMenuBar();

		JMenu menuFile = new JMenu("File");
		String[] listFile = { "New", "Open", "Save", "Print" };
		createMenuItems(menuFile, listFile);

		JMenu menuEdit = new JMenu("Edit");

		String[] listEdit = { "Cut", "Copy", "Paste", "Highlight", "UnHighlightAll" };
		createMenuItems(menuEdit, listEdit);

		menuBar.add(menuFile);
		menuBar.add(menuEdit);

		frame.setJMenuBar(menuBar);
		frame.add(textPane);
		int size = 500;
		frame.setSize(size, size);
		Point center = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
		frame.setBounds(center.x - size / 2, center.y - size / 2, size, size);
		frame.setVisible(true);
	}

	private void createMenuItems(JMenu menu, String[] list) {
		for (String string : list) {
			JMenuItem menuItem = new JMenuItem(string);
			menuItem.addActionListener(this);
			menu.add(menuItem);
		}
	}

	private File fileWork() {
		JFileChooser fileChooser = new JFileChooser("f:");
		int resultShowDialog = fileChooser.showSaveDialog(null);
		if (resultShowDialog != JFileChooser.APPROVE_OPTION) {
			JOptionPane.showMessageDialog(frame, "the user cancelled the operation");
			return null;
		} else {
			File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
			return file;
		}

	}

	private void save() {
		File file = fileWork();
		if (file == null) {
			return;
		}
		try {
			FileWriter writer = new FileWriter(file, false);
			BufferedWriter buffWriter = new BufferedWriter(writer);
			buffWriter.write(textPane.getText());
			buffWriter.flush();
			buffWriter.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(frame, e.getMessage());
		}
	}

	private void open() {
		File file = fileWork();
		if (file == null) {
			return;
		}
		try {
			String s1 = "", sl = "";
			FileReader fr = new FileReader(file);
			try (BufferedReader br = new BufferedReader(fr);) {
				sl = br.readLine();
				while ((s1 = br.readLine()) != null) {
					sl = sl + "\n" + s1;
				}
				textPane.setText(sl);
			}
		} catch (Exception evt) {
			JOptionPane.showMessageDialog(frame, evt.getMessage());
		}

	}

	private void highlight() {
		String input = textPane.getText();
		Highlighter highlighter = textPane.getHighlighter();
		ColorJNI[] colorArr = Engine.getColor(input);
		for (int i = 0; i < colorArr.length; i++) {
			Color cl = new Color(colorArr[i].r, colorArr[i].g, colorArr[i].b);
			HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(cl);
			try {
				highlighter.addHighlight(i, i + 1, painter);
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		}
	}

	private void unHighlightAll() {
		String before = textPane.getText();
		String text = "<font color = FF0000>" + before + "</font>";
		textPane.setText("");
		textPane.setText(text);
	}

	// If a button is pressed
	public void actionPerformed(ActionEvent event) {
		String actionComm = event.getActionCommand();
		if (actionComm.equals("Cut")) {
			textPane.cut();
		} else if (actionComm.equals("Copy")) {
			textPane.copy();
		} else if (actionComm.equals("Paste")) {
			textPane.paste();
		} else if (actionComm.equals("New")) {
			textPane.setText("");
		} else if (actionComm.equals("Save")) {
			save();
		} else if (actionComm.equals("Open")) {
			open();
		} else if (actionComm.equals("Highlight")) {
			highlight();
		} else if (actionComm.equals("UnHighlightAll")) {
			unHighlightAll();
		} else if (actionComm.equals("Print")) {
			try {
				textPane.print();
			} catch (Exception e) {
				JOptionPane.showMessageDialog(frame, e.getMessage());
			}
		}
	}

	public static void createAndShowGUI() {
		new SwingMainWindow2();
	}
}
