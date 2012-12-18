package com.tools.nlputil;

import java.util.ArrayList;
import java.util.List;

/**
 * This piece of code is written to split a given text into 
 * N-grams (as per user's requirement).
 * 
 * @author arunjayapal
 *
 */
public class NGramSplitter{

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
        String[] words = str.split(" ");
        for (int i = 0; i < words.length - n + 1; i++)
            ngrams.add(concat(words, i, i+n));
        return ngrams;
    }

    public static String concat(String[] words, int start, int end) {
        StringBuilder sb = new StringBuilder();
        for (int i = start; i < end; i++)
            sb.append((i > start ? " " : "") + words[i]);
        return sb.toString();
    }

    /**
     * For testing
     * 
     * @param args
     */
    public static void main(String[] args) {
        for (int n = 1; n <= 3; n++) {
            for (String ngram : ngrams(n, "This is my car."))
                System.out.println(ngram);
            System.out.println();
        }
    }
}

