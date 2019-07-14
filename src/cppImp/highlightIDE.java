package cppImp;

import cppImp.ColorJNI;

public class highlightIDE {
	static {
		try {
			System.loadLibrary("libhighlightIDE");
		} catch (java.lang.UnsatisfiedLinkError e) {
			System.out.println(e);
		}
	}

	public static native ColorJNI[] highlightCPP(String input, boolean isCanceled);
}
