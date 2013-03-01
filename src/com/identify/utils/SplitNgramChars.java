package com.identify.utils;

import java.util.ArrayList;
import java.util.List;

public class SplitNgramChars {
	/**
	 * This static method takes a String input and number of grams, 
	 * returning a list of n-grams
	 * 
	 * @param number of grams
	 * @param str
	 * @return n-grams of any given document
	 */
    public static List<String> ngrams(int n, String str) {
        List<String> ngrams = new ArrayList<String>();
        char[] characters = str.toCharArray();
        for (int i = 0; i < characters.length - n + 1; i++)
            ngrams.add(concat(characters, i, i+n));
        return ngrams;
    }

    public static String concat(char[] characters, int start, int end) {
        StringBuilder sb = new StringBuilder();
        for (int i = start; i < end; i++)
            sb.append((i > start ? "" : "") + characters[i]);
        return sb.toString();
    }

    /**
     * For testing
     * 
     * @param args
     */
    public static void main(String[] args) {
        for (int n = 1; n <= 3; n++) {
            for (String ngram : ngrams(n, "This_is_my_car."))
                System.out.println(ngram);
            System.out.println();
        }
    }
}
