package com.candidate.retrieval;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;

import opennlp.tools.tokenize.SimpleTokenizer;

/**
 * This piece of code is used to formulate queries for the Plagiarism detection
 * task
 * 
 * @author arunjayapal
 *
 */
public class QueryFormulation {

	StringBuffer sb = new StringBuffer();
	HashSet<String> uniq_words = new HashSet<String>();

	/**
	 * Initialize the default query interface, for which the request is made
	 * to http://webis15.medien.uni-weimar.de/chatnoir/json?query=
	 */
	public static String token;
	public QueryFormulation(String token){
		this.token = token;
		String query = "http://webis15.medien.uni-weimar.de/chatnoir/json?query=";
		sb.append(query);
	}	
	
	/**
	 * Uses String buffer to formulate the queries, which will be passed to the
	 * search interface. Again, simple tokenizer is used to remove certain special
	 * characters attached to the words like "PRESENT(" to "PRESENT"
	 * 
	 * @param candidateWords 
	 * @return Query
	 * @throws FileNotFoundException 
	 */
	@SuppressWarnings("deprecation")
	public String query(ArrayList<String> candidateWords) throws FileNotFoundException{

		int max_cap = candidateWords.size(), i=0;
		SimpleTokenizer st = new SimpleTokenizer();
		StopWordRemoval swr = new StopWordRemoval();
		for(String words:candidateWords){
			if(uniq_words.size() >= 10)
				break;
			int min_cap = st.tokenize(words).length, j=0;
			for(String word:st.tokenize(words)){
				if(swr.stoplist.contains(word.toUpperCase()))
					continue;
				else{
					if(uniq_words.contains(word)){
						continue;		
					}
					else{
						uniq_words.add(word);
						sb.append(word);
						if(++i<=max_cap || ++j<min_cap)
							sb.append("%20");
					}
				}
			}
		}

		sb.append("&resultlength=10&token="+token);
		return sb.toString(); //return query
	}

	/**
	 * Uses String buffer to formulate the queries, which will be passed to the
	 * search interface. Again, simple tokenizer is used to remove certain special
	 * characters attached to the words like "PRESENT(" to "PRESENT"
	 * 
	 * @param candidateWords
	 * @param fileid 
	 * @return Query
	 * @throws FileNotFoundException 
	 */
	@SuppressWarnings("deprecation")
	public String query(ArrayList<String> candidateWords, String fileid) throws FileNotFoundException{

		int max_cap = candidateWords.size(), i=0;
		SimpleTokenizer st = new SimpleTokenizer();
		StopWordRemoval swr = new StopWordRemoval();
		for(String words:candidateWords){
			if(uniq_words.size() >= 9)
				break;
			int min_cap = st.tokenize(words).length, j=0;
			for(String word:st.tokenize(words)){
				if(swr.stoplist.contains(word.toUpperCase()))
					continue;
				else{
					if(uniq_words.contains(word)){
						continue;		
					}
					else{
						uniq_words.add(word);
						sb.append(word);
						if(++i<=max_cap || ++j<min_cap)
							sb.append("%20");
					}
				}
			}
		}

		sb.append("&resultlength=10&token="+token);
		return sb.toString(); //return query
	}
}
