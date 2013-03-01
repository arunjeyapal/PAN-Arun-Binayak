package com.tools.util;

/**
 * This class defines the data storage format
 * of the words and their respective tags
 * 
 * @author arunjayapal
 *
 */
public class WordTag {
	public final String word;
	public final String tag;
	public WordTag(String word, String tag){
		this.word = word;
		this.tag = tag;
	}
}