package com.android.studentapp;

public class Util {

	public static String firstLetterCapitalize(String s) {
		s = s.substring(0, 1).toUpperCase() + s.substring(1);
		return s;
	}
}
