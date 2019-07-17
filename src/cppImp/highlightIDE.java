package cppImp;

public class highlightIDE {
	static {
		try {
			System.loadLibrary("libhighlightTest");
		} catch (java.lang.UnsatisfiedLinkError e) {
			System.out.println(e);
		}
	}

	public static native ColorJNI[] highlightCPP(String input, boolean isCanceled);
}
