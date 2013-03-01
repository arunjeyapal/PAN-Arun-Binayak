package com.identify.author;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

import com.identify.utils.SplitNgramChars;

/**
 * ProcessData class does the following: 1. add _ instead of a space 2. add __
 * instead of newline 3. get char N-gram count
 * 
 * @author arunjayapal
 * 
 */
class ProcessData {

	protected String data;

	/**
	 * The constructor 1. adds '_' instead of spaces 2. adds '__' instead of new
	 * line
	 * 
	 * @param file
	 *            -path
	 */
	protected ProcessData(File each_file) {
		try {
			Scanner scan = new Scanner(each_file);
			StringBuilder sb = new StringBuilder();
			while (scan.hasNext())
				sb.append(scan.nextLine().replace(" ", "_")
						.replace("\n", "__"));
			data = sb.toString();
		} catch (FileNotFoundException e) {
			System.err.format("File %s not found at %s\n", 
					each_file.getName(), each_file.getAbsolutePath());
		}
	}

	/**
	 * Splits into N-gram characters
	 * 
	 * @param Number-of-grams
	 * @return list-of-character-Ngrams
	 */
	protected List<String> getNCharCount(int ngramcount) {
		return SplitNgramChars.ngrams(ngramcount, data.toLowerCase());
	}
}
