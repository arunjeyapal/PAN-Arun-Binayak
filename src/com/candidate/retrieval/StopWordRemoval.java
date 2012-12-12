package com.candidate.retrieval;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

import com.tools.util.WordTag;
import com.tools.nlputil.PorterStemmer;

/**
 * Stop words are removed and stemming is implemented
 * 
 * @author arunjayapal
 *
 */
public class StopWordRemoval{
	
	HashSet<String> stoplist = new HashSet<String>();
	PorterStemmer ps = new PorterStemmer();
	
	/**
	 * Constructor to add the set of stopwords from stoplist
	 * and store them in HashSet datastructure
	 * @throws FileNotFoundException
	 */
	public StopWordRemoval() throws FileNotFoundException{
		File file = new File("./model/stoplist");
		Scanner s = new Scanner(new FileInputStream(file));
		while(s.hasNext()){
			stoplist.add(s.next().toUpperCase());
		}
	}
	
	/**
	 * removeWords method is used to remove the stop words from the 
	 * list of tokens and return the list in the ArrayList data format
	 * 
	 * @param tokens
	 * @return word list
	 */
	public ArrayList<String> removeWords(ArrayList<WordTag> tokens, boolean stem){
		ArrayList<String> wordList = new ArrayList<String>();
		String new_word;
		for(WordTag words:tokens){
			
			if(stem)
				new_word = stemWord(words.word);
			else
				new_word = words.word.toUpperCase();
			
			if(stoplist.contains(new_word) || new_word.contains("."))
				continue;
			else
				wordList.add(new_word);
			
		}
		return wordList;
	}
	
	/**
	 * Porter stemmer method called here
	 * 
	 * @param word
	 * @return stemmed word
	 */
	public String stemWord(String word){
		return ps.stem(word.toLowerCase()).toUpperCase();
	}
}
