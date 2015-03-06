package com.techstar.modules.elfinder.util;

import org.apache.commons.lang3.ArrayUtils;

public final class LetterUtils {

	private static final String[] LETTERS = new String[26];
	static {
		for (int i = (int) 'a'; i < 'a' + 26; i++) {
			LETTERS[i - 97] = ("" + (char) i).toUpperCase();
		}
		ArrayUtils.reverse(LETTERS);
	}

	public static String getLetter(final int i) {
		return LETTERS[i];
	}
}
