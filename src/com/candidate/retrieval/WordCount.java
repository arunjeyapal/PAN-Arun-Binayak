package com.candidate.retrieval;

import java.util.ArrayList;
import java.util.HashMap;

public class WordCount {
	
	public ArrayList<String> words;
	
	public WordCount(ArrayList<String> words){
		this.words = words;
	}
	
	public HashMap<String, Integer> countWords(){
		HashMap<String,Integer> wordCnt = new HashMap<String,Integer>();
		for (String eachWord:words){
			if (wordCnt.containsKey(eachWord))
				wordCnt.put(eachWord,wordCnt.get(eachWord)+1);
			else
				wordCnt.put(eachWord, 1);
		}
		return wordCnt;
	}
}
