package net.ib.baseballtext.util;

public class Strings {
	public static boolean isNotEmptyString(final String str) {
		return (str != null && str.length() > 0 && !str.trim().equals(""));
	}
}