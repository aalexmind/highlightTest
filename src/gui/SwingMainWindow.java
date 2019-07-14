package gui;

import java.awt.Color;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import base.Engine;
import cppImp.ColorJNI;

public class SwingMainWindow extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;

	JTextPane tPane;

	JFrame frame;
	boolean flagHighlight;

	SwingMainWindow() {
		frame = new JFrame("Highlight editor");
		flagHighlight = true;

		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			System.out.println(e);
		}
		frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		tPane = new JTextPane();
		AbstractDocument document = (AbstractDocument) tPane.getDocument();

		document.setDocumentFilter(new DocumentFilter() {
			public void replace(FilterBypass fb, int offs, int length, String str, AttributeSet a)
					throws BadLocationException {
				AttributeSet aset = a;
				if (flagHighlight) {
					ColorJNI[] colorArr = Engine.getColor(str);
					for (int i = 0; i < str.length(); i++) {
						Color cl = new Color(colorArr[i].r, colorArr[i].g, colorArr[i].b);
						aset = changeStyle(cl);
						super.replace(fb, offs + i, length, Character.toString(str.charAt(i)), aset);
					}
				} else {
					super.replace(fb, offs, length, str, aset);
				}
			}

			public void insertString(FilterBypass fb, int offs, String str, AttributeSet a)
					throws BadLocationException {
				super.insertString(fb, offs, str, a);
			}
		});
		JMenuBar menuBar = new JMenuBar();
		JMenu menuEdit = new JMenu("Edit");
		String[] listEdit = { "Highlight", "UnHighlightAll" };
		createMenuItems(menuEdit, listEdit);
		menuBar.add(menuEdit);
		frame.setJMenuBar(menuBar);

		frame.add(tPane);
		appendToPane(tPane, "My Name is Too 1 Good.\nI wish I could be ONE of THE BEST on StackOverflow",
				new Color(0, 0, 0));
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

	private AttributeSet changeStyle(Color c) {
		StyleContext sc = StyleContext.getDefaultStyleContext();
		AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);
		return aset;
	}

	private void appendToPane(JTextPane tp, String msg, Color c) {
		AttributeSet aset = changeStyle(c);

		int len = tp.getDocument().getLength();
		tp.setCaretPosition(len);
		tp.setCharacterAttributes(aset, false);
		tp.replaceSelection(msg);
	}

	private void highlight() {
		flagHighlight = true;
		String input = tPane.getText();
		tPane.setText("");
		ColorJNI[] colorArr = Engine.getColor(input);
		int first = 0;
		int last = 0;
		for (int i = 0; i < input.length(); i++) {
			if (i < input.length() - 1) {
				if (colorArr[i].r == colorArr[i + 1].r && colorArr[i].g == colorArr[i + 1].g
						&& colorArr[i].b == colorArr[i + 1].b) {
					continue;
				}
			}
			last = i + 1;
			Color cl = new Color(colorArr[i].r, colorArr[i].g, colorArr[i].b);
			appendToPane(tPane, input.substring(first, last), cl);
			first = last;
		}
	}

	private void unHighlightAll() {
		flagHighlight = false;
		String input = tPane.getText();
		tPane.setText("");
		appendToPane(tPane, input, Color.red);
	}

	// If a button is pressed
	public void actionPerformed(ActionEvent event) {
		String actionComm = event.getActionCommand();
		if (actionComm.equals("Highlight")) {
			highlight();
		} else if (actionComm.equals("UnHighlightAll")) {
			unHighlightAll();
		}
	}

	public static void createAndShowGUI() {
		new SwingMainWindow();
	}
}
