package com.candidate.retrieval;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import com.tools.nlputil.POStagger;
import com.tools.nlputil.Tokenizer;
import com.tools.util.WordTag;

/**
 * This piece of code executes the Execution flow of the system.
 * 
 * @author arunjayapal
 *
 */
public class ExecFlow {

	/**
	 * This method implements the complete system's flow
	 * 
	 * @param string
	 * @param tk
	 * @param pos
	 * @param stoptag
	 * @param doc_wordtags
	 * @return Query String or "No possible queries"
	 * @throws IOException
	 */
	public static String systemFlow(String string, Tokenizer tk, POStagger pos, StopTags stoptag, ArrayList<WordTag> doc_wordtags, Boolean stem) throws IOException{

		System.out.println("Paragraph"+" | Tokenized | POStagged | StopWords removed | Keywords");

		/*Tokenize the text*/
		String[] tokens = tk.tokenize(string);

		/*Tag with POS*/
		String[] posTok = pos.tag(tokens);
		ArrayList<WordTag> all_wordtags = new ArrayList<WordTag>();
		ArrayList<WordTag> req_wordtags = new ArrayList<WordTag>();

		for(int i=0; i<tokens.length; i++){

			WordTag t = new WordTag(tokens[i],posTok[i]);
			doc_wordtags.add(t);
			all_wordtags.add(i, t);

			/*Remove words with unwanted tags*/
			if(stoptag.containTags(posTok[i]))
				req_wordtags.add(t);				
		}

		/*Remove Stopwords*/
		StopWordRemoval s1 = new StopWordRemoval();
		ArrayList<String> candidateWords = new ArrayList<String>();
		candidateWords = s1.removeWords(req_wordtags, stem);

		/*Word counted here*/
		WordCount wc = new WordCount(candidateWords); //not used anywhere

		/*Formulate Queries*/
		if(wc.countWords().keySet().size() < 1){
			//System.out.println("No possible queries");
			return "\nNo possible queries";
		}

		/*Get JSON data from the response returned by the search engine*/
		String JsonVal = FormQuerySearch(candidateWords);
		System.out.println("JSON Output -------> "+JsonVal);
		return JsonVal;
	}
	
	/**
	 * This method implements the complete system's flow
	 * 
	 * @param string
	 * @param tk
	 * @param pos
	 * @param stoptag
	 * @param doc_wordtags
	 * @param fileid
	 * @return Query String or "No possible queries"
	 * @throws IOException
	 */
	public static String systemFlow(String string, Tokenizer tk, POStagger pos, StopTags stoptag, ArrayList<WordTag> doc_wordtags, String fileid, Boolean stem) throws IOException{

		System.out.println("Paragraph"+" | Tokenized | POStagged | StopWords removed | Keywords");

		/*Tokenize the text*/
		String[] tokens = tk.tokenize(string);

		/*Tag with POS*/
		String[] posTok = pos.tag(tokens);
		ArrayList<WordTag> all_wordtags = new ArrayList<WordTag>();
		ArrayList<WordTag> req_wordtags = new ArrayList<WordTag>();

		for(int i=0; i<tokens.length; i++){

			WordTag t = new WordTag(tokens[i],posTok[i]);
			doc_wordtags.add(t);
			all_wordtags.add(i, t);

			/*Remove words with unwanted tags*/
			if(stoptag.containTags(posTok[i]))
				req_wordtags.add(t);				
		}

		/*Remove Stopwords*/
		StopWordRemoval s1 = new StopWordRemoval();
		ArrayList<String> candidateWords = new ArrayList<String>();
		candidateWords = s1.removeWords(req_wordtags, stem);

		/*Word counted here*/
		WordCount wc = new WordCount(candidateWords); //not used anywhere

		/*Formulate Queries*/
		if(wc.countWords().keySet().size() < 1){
			//System.out.println("No possible queries");
			return "\nNo possible queries";
		}

		/*Get JSON data from the response returned by the search engine*/
		String JsonVal = FormQuerySearch(candidateWords, fileid);
		System.out.println("JSON Output -------> "+JsonVal);
		return JsonVal;
	}
	
	/**
	 * Pass queries to the search interface
	 * 
	 * @param candidateWords
	 * @param fileid 
	 * @return JSON output from the search engine in String format
	 * @throws IOException
	 */
	private static String FormQuerySearch(ArrayList<String> candidateWords){
		QueryFormulation qf = new QueryFormulation();
		String query = "";
		try {
			query = qf.query(candidateWords);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		System.out.println(query);
		String response = "";
		try
		{
			System.out.println("Searching...");
			GetResponse gr = new GetResponse(new URL(query));
			response = gr.returnResponse();
		} catch (MalformedURLException e) {
			System.out.println("GetResponse.main - wrong url: " +e );
		} catch (IOException e) {
			e.printStackTrace();
		}
		//		System.out.println(response);
		return response;
	}

	/**
	 * Pass queries to the search interface
	 * 
	 * @param candidateWords
	 * @param fileid 
	 * @return JSON output from the search engine in String format
	 * @throws IOException
	 */
	private static String FormQuerySearch(ArrayList<String> candidateWords, String fileid){
		QueryFormulation qf = new QueryFormulation();
		String query = "";
		try {
			query = qf.query(candidateWords, fileid);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		System.out.println(query);
		String response = "";
		try
		{
			System.out.println("Searching...");
			GetResponse gr = new GetResponse(new URL(query));
			response = gr.returnResponse();
		} catch (MalformedURLException e) {
			System.out.println("GetResponse.main - wrong url: " +e );
		} catch (IOException e) {
			e.printStackTrace();
		}
		//		System.out.println(response);
		return response;
	}
}
