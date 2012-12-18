package com.tools.util;

import java.lang.reflect.InvocationTargetException;

/**
 * 
 * Character offset functions are implemented
 * 
 * @author arunjayapal
 * 
 */
public class CharOffset {

	/**
	 * Although String functions are available for converting the string to
	 * character array, this function has been created for easy implementation.
	 * 
	 * @param string
	 * @return character array
	 */
	public static char[] getCharArray(String string) {
		return string.toCharArray();
	}

	/**
	 * Returns the length of any given string
	 * 
	 * @param string
	 * @return length of the given String
	 */
	public static int getNoCharacters(String string) {
		return string.toCharArray().length;
	}

	/**
	 * Although String functions are available for retrieving the character
	 * offset values, this function has been created for easy implementation.
	 * 
	 * @param character
	 *            array
	 * @param startOffset
	 * @param no_chars
	 *            or count of characters
	 * @return string
	 */
	public static String getOffsetStringValue(char[] array, int startOffset,
			int no_chars) {
		return String.valueOf(array, startOffset, no_chars);
	}

	public static String getOffsetStringValue(String string, int startOffset,
			int length, boolean wordOffset)
			throws StringIndexOutOfBoundsException, InvocationTargetException {
		if (!wordOffset)
			return String.valueOf(string.toCharArray(), startOffset, length);
		else {
			int startCharOffset = getWordCharOffset(startOffset, string);
			int no_chars = getWordCharLength(string, startOffset, length);
			try {
				return String.valueOf(string.toCharArray(), startCharOffset,
						no_chars);
			} catch (StringIndexOutOfBoundsException si) {
				if (startCharOffset + no_chars > string.toCharArray().length)
					return String.valueOf(string.toCharArray(),
							startCharOffset, string.toCharArray().length
									- startCharOffset - 1);
				else
					return String.valueOf(string.toCharArray(),
							startCharOffset, no_chars - 1);
			}
		}
	}

	public static int getlength(String string, int startOffset, int length) {
		int startCharOffset = getWordCharOffset(startOffset, string);
		int no_chars = getWordCharLength(string, startOffset, length);
		try {
			String.valueOf(string.toCharArray(), startCharOffset, no_chars);
			return no_chars;
		} catch (StringIndexOutOfBoundsException si) {
			if (startCharOffset + no_chars > string.toCharArray().length)
				return string.toCharArray().length - startCharOffset - 1;
			else
				return no_chars - 1;
		}
	}

	public static int getWordCharOffset(int startOffset, String string) {
		int charOffset = 0;
		if (startOffset != 0) {
			String[] strings = string.split("[\\s+|\\W+]");
			// for(int i=0; i < startOffset; i++){
			// int strlen = strings[i].length()+1;
			// charOffset += strlen;
			// }
			int i = 0;
			for (String s : strings) {
				if (i < startOffset) {
					int strlen = s.length() + 1;
					charOffset += strlen;
					++i;
				} else
					break;
			}
			return charOffset;
		} else
			return charOffset;
	}

	public static int getWordCharLength(String string, int startOffset,
			int length) {

		String[] strings = string.split("[\\s+|\\W+]");
		int charOffset = 0;
		int strlen;
		int itr = 0;

		for (String s : strings) {
			if (itr < startOffset) {
				itr++;
				continue;
			} else if (itr >= startOffset && itr < startOffset + length) {
				if (itr == length)
					strlen = s.length();
				else
					strlen = s.length() + 1;
				charOffset += strlen;
				itr++;
			} else {
				itr++;
				break;
			}
		}
		return charOffset;
	}
}
