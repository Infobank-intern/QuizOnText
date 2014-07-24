package net.ib.baseballtext;

public class Strings {
	public static boolean isEmptyString(final String str) {
		return (str != null && str.length() > 0 && !str.trim().equals(""));
	}
}