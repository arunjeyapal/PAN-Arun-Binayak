package com.candidate.retrieval;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * This code uses the json simple library to parse the json files.
 * The data returned by the methods may be used for detailed analysis
 * of the search results and optimize our query.
 * 
 * @author arunjayapal
 */
public class JsonInterpreter {

	JSONParser parser = new JSONParser();

	public boolean fileExists(String filename){
		File file = new File(filename);
		boolean exists = file.exists();
		if(!exists)
			return false;
		else
			return true;
	}

	/**
	 * The parse() method parses any given JSON file and prints the data
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ParseException
	 */

	public void parse(String s) throws FileNotFoundException, IOException, ParseException{
		Object obj;
		if(fileExists(s))
			obj = parser.parse(new FileReader(s));
		else
			obj = parser.parse(s);

		JSONObject jsonObject = (JSONObject) obj;

		long total_res_count = (long) jsonObject.get("TotalResultCount");
		System.out.println("Total Results Count:\t"+total_res_count+"\n");

		JSONArray page = (JSONArray) jsonObject.get("Page");
		for(Object element:page){

			Object loopObj = parser.parse(element.toString());

			JSONObject loopJsonObject = (JSONObject) loopObj;
			String warc_target_URI = (String) loopJsonObject.get("WarcTargetURI");
			String baseURL = (String) loopJsonObject.get("BaseURL");
			String HTMLTitle = (String) loopJsonObject.get("HTMLTitle");
			double pageRank = (double) loopJsonObject.get("Pagerank");
			long spamrank = (long) loopJsonObject.get("Spamrank");
			long no_sentences = (long) loopJsonObject.get("NumSentences");
			double weight = (double) loopJsonObject.get("Weight");
			double NormalizedPagerank = (double) loopJsonObject.get("NormalizedPagerank");
			long NumWords = (long) loopJsonObject.get("NumWords");
			double Readability = (double) loopJsonObject.get("Readability");
			double Proximity = (double) loopJsonObject.get("Proximity");
			String WarcTrecID = (String) loopJsonObject.get("WarcTrecID");
			String LongID = (String) loopJsonObject.get("LongID");
			long NumSyllables = (long) loopJsonObject.get("NumSyllables");
			long BingRank = (long) loopJsonObject.get("BingRank");
			long NumCharacters = (long) loopJsonObject.get("NumCharacters");

			System.out.println("Warc Target URI:\t"+warc_target_URI);
			System.out.println("Base URL:\t"+baseURL);
			System.out.println("HTML Title:\t"+HTMLTitle);
			System.out.println("Page Rank:\t"+pageRank);
			System.out.println("Spam Rank:\t"+spamrank);
			System.out.println("Number of Sentences:\t"+no_sentences);
			System.out.println("Weight:\t"+weight);
			System.out.println("NormalizedPagerank:\t"+NormalizedPagerank);
			System.out.println("NumWords:\t"+NumWords);
			System.out.println("Readability:\t"+Readability);
			System.out.println("Proximity:\t"+Proximity);
			System.out.println("Warc Trec ID:\t"+WarcTrecID);
			System.out.println("Long ID:\t"+LongID);
			System.out.println("Number of Syllables:\t"+NumSyllables);
			System.out.println("Bing Rank:\t"+BingRank);
			System.out.println("Num Characters:\t"+NumCharacters+"\n");
		}
	}

	/**
	 * This method returns the total results count for the given query
	 * 
	 * @param filepath OR String
	 * @return TotalresultCount
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ParseException
	 */
	public long getTotalResultCount(String s) throws FileNotFoundException, IOException, ParseException{
		Object obj;
		if(fileExists(s))
			obj = parser.parse(new FileReader(s));
		else
			obj = parser.parse(s);
		JSONObject jsonObject = (JSONObject) obj;
		return (long) jsonObject.get("TotalResultCount"); 
	}

	/**
	 * This method returns the target Universal Resource Indicator from the 
	 * JSON file
	 * 
	 * @param filepath or String
	 * @return Target URI
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ParseException
	 */
	public List<String> getWarcTargetURI(String s) throws FileNotFoundException, IOException, ParseException{

		Object obj;
		if(fileExists(s))
			obj = parser.parse(new FileReader(s));
		else
			obj = parser.parse(s);
		JSONObject jsonObject = (JSONObject) obj;
		JSONArray page = (JSONArray) jsonObject.get("Page");
		List<String> warcTarget = new ArrayList<String>();
		for(Object element:page){
			Object loopObj = parser.parse(element.toString());
			JSONObject loopJsonObject = (JSONObject) loopObj;
			warcTarget.add((String) loopJsonObject.get("WarcTargetURI"));
		}
		return warcTarget;
	}

	/**
	 * This method returns the Base URL of the retrieved file for the given query
	 * 
	 * @param filepath or string
	 * @return Base URL
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ParseException
	 */
	public List<String> getBaseURL(String s) throws FileNotFoundException, IOException, ParseException{

		Object obj;
		if(fileExists(s))
			obj = parser.parse(new FileReader(s));
		else
			obj = parser.parse(s);

		JSONObject jsonObject = (JSONObject) obj;
		JSONArray page = (JSONArray) jsonObject.get("Page");
		List<String> baseURL = new ArrayList<String>();
		for(Object element:page){
			Object loopObj = parser.parse(element.toString());
			JSONObject loopJsonObject = (JSONObject) loopObj;
			baseURL.add((String) loopJsonObject.get("BaseURL"));
		}
		return baseURL;
	}

	/**
	 * This method is used to return the HTML title of the rendered page
	 * 
	 * @param filepath or string
	 * @return HTMLTitle
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ParseException
	 */
	public List<String> getHTMLTitle(String s) throws FileNotFoundException, IOException, ParseException{

		Object obj;
		if(fileExists(s))
			obj = parser.parse(new FileReader(s));
		else
			obj = parser.parse(s);
		JSONObject jsonObject = (JSONObject) obj;
		JSONArray page = (JSONArray) jsonObject.get("Page");
		List<String> htmlTitle = new ArrayList<String>();
		for(Object element:page){
			Object loopObj = parser.parse(element.toString());
			JSONObject loopJsonObject = (JSONObject) loopObj;
			htmlTitle.add((String) loopJsonObject.get("HTMLTitle"));
		}
		return htmlTitle;
	}

	/**
	 * This method is used to return the page rank assigned by the search engine for the 
	 * returned page
	 * 
	 * @param filepath or string
	 * @return pagerank
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ParseException
	 */
	public List<Double> getPageRank(String s) throws FileNotFoundException, IOException, ParseException{

		Object obj;
		if(fileExists(s))
			obj = parser.parse(new FileReader(s));
		else
			obj = parser.parse(s);
		JSONObject jsonObject = (JSONObject) obj;
		JSONArray page = (JSONArray) jsonObject.get("Page");
		List<Double> Pagerank = new ArrayList<Double>();
		for(Object element:page){
			Object loopObj = parser.parse(element.toString());
			JSONObject loopJsonObject = (JSONObject) loopObj;
			Pagerank.add((double) loopJsonObject.get("Pagerank"));
		}
		return Pagerank;
	}

	/**
	 * This method returns the spam rank of the page returned by the search engine
	 * 
	 * @param filepath or string
	 * @return Spam Rank
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ParseException
	 */
	public List<Long> getSpamRank(String s) throws FileNotFoundException, IOException, ParseException{

		Object obj;
		if(fileExists(s))
			obj = parser.parse(new FileReader(s));
		else
			obj = parser.parse(s);
		JSONObject jsonObject = (JSONObject) obj;
		JSONArray page = (JSONArray) jsonObject.get("Page");
		List<Long> Spamrank = new ArrayList<Long>();
		for(Object element:page){
			Object loopObj = parser.parse(element.toString());
			JSONObject loopJsonObject = (JSONObject) loopObj;
			Spamrank.add((long) loopJsonObject.get("Spamrank"));
		}
		return Spamrank;
	}

	/**
	 * This method returns the number of sentences in the file returned by
	 * the search engine
	 * 
	 * @param filepath or string
	 * @return Number of sentences available in the file
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ParseException
	 */
	public List<Long> getNumSentences(String s) throws FileNotFoundException, IOException, ParseException{
		Object obj;
		if(fileExists(s))
			obj = parser.parse(new FileReader(s));
		else
			obj = parser.parse(s);
		JSONObject jsonObject = (JSONObject) obj;
		JSONArray page = (JSONArray) jsonObject.get("Page");
		List<Long> NumSentences = new ArrayList<Long>();
		for(Object element:page){
			Object loopObj = parser.parse(element.toString());
			JSONObject loopJsonObject = (JSONObject) loopObj;
			NumSentences.add((long) loopJsonObject.get("NumSentences"));
		}
		return NumSentences;
	}

	/**
	 * This method returns the weight assigned to the rendered page by the search engine
	 * 
	 * @param filepath or string
	 * @return Weight
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ParseException
	 */
	public List<Double> getWeight(String s) throws FileNotFoundException, IOException, ParseException{
		Object obj;
		if(fileExists(s))
			obj = parser.parse(new FileReader(s));
		else
			obj = parser.parse(s);
		JSONObject jsonObject = (JSONObject) obj;
		JSONArray page = (JSONArray) jsonObject.get("Page");
		List<Double> Weight = new ArrayList<Double>();
		for(Object element:page){
			Object loopObj = parser.parse(element.toString());
			JSONObject loopJsonObject = (JSONObject) loopObj;
			Weight.add((double) loopJsonObject.get("Weight"));
		}
		return Weight;
	}

	/**
	 * This method returns the normalized page rank assigned by the search engine 
	 * to the rendered page
	 * 
	 * @param filepath or string
	 * @return Normalized Page Rank
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ParseException
	 */
	public List<Double> getNormalizedPagerank(String s) throws FileNotFoundException, IOException, ParseException{
		Object obj;
		if(fileExists(s))
			obj = parser.parse(new FileReader(s));
		else
			obj = parser.parse(s);
		JSONObject jsonObject = (JSONObject) obj;
		JSONArray page = (JSONArray) jsonObject.get("Page");
		List<Double> NormalizedPagerank = new ArrayList<Double>();
		for(Object element:page){
			Object loopObj = parser.parse(element.toString());
			JSONObject loopJsonObject = (JSONObject) loopObj;
			NormalizedPagerank.add((double) loopJsonObject.get("NormalizedPagerank"));
		}
		return NormalizedPagerank;
	}

	/**
	 * This method returns the number of words found in the file rendered by the search engine
	 * 
	 * @param filepath or string
	 * @return Number of Words
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ParseException
	 */
	public List<Long> getNumWords(String s) throws FileNotFoundException, IOException, ParseException{
		Object obj;
		if(fileExists(s))
			obj = parser.parse(new FileReader(s));
		else
			obj = parser.parse(s);
		JSONObject jsonObject = (JSONObject) obj;
		JSONArray page = (JSONArray) jsonObject.get("Page");
		List<Long> NumWords = new ArrayList<Long>();
		for(Object element:page){
			Object loopObj = parser.parse(element.toString());
			JSONObject loopJsonObject = (JSONObject) loopObj;
			NumWords.add((long) loopJsonObject.get("NumWords"));
		}
		return NumWords;
	}

	/**
	 * This method returns the readability index of the file returned by the search engine
	 * 
	 * @param filepath or string
	 * @return Readability index
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ParseException
	 */
	public List<Double> getReadability(String s) throws FileNotFoundException, IOException, ParseException{
		Object obj;
		if(fileExists(s))
			obj = parser.parse(new FileReader(s));
		else
			obj = parser.parse(s);
		JSONObject jsonObject = (JSONObject) obj;
		JSONArray page = (JSONArray) jsonObject.get("Page");
		List<Double> Readability = new ArrayList<Double>();
		for(Object element:page){
			Object loopObj = parser.parse(element.toString());
			JSONObject loopJsonObject = (JSONObject) loopObj;
			Readability.add((double) loopJsonObject.get("Readability"));
		}
		return Readability;
	}

	/**
	 * This method returns the proximity rank of the file returned by the search engine
	 * 
	 * @param filepath or string
	 * @return Proximity rank
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ParseException
	 */
	public List<Double> getProximity(String s) throws FileNotFoundException, IOException, ParseException{
		Object obj;
		if(fileExists(s))
			obj = parser.parse(new FileReader(s));
		else
			obj = parser.parse(s);
		JSONObject jsonObject = (JSONObject) obj;
		JSONArray page = (JSONArray) jsonObject.get("Page");
		List<Double> Proximity = new ArrayList<Double>();
		for(Object element:page){
			Object loopObj = parser.parse(element.toString());
			JSONObject loopJsonObject = (JSONObject) loopObj;
			Proximity.add((double) loopJsonObject.get("Proximity"));
		}
		return Proximity;
	}

	/**
	 * This method returns the WarcTrecID, which is a unique id corresponding to the file
	 * 
	 * @param filepath or string
	 * @return Unique ID corresponding to the file
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ParseException
	 */
	public List<String> getWarcTrecID(String s) throws FileNotFoundException, IOException, ParseException{
		Object obj;
		if(fileExists(s))
			obj = parser.parse(new FileReader(s));
		else
			obj = parser.parse(s);
		JSONObject jsonObject = (JSONObject) obj;
		JSONArray page = (JSONArray) jsonObject.get("Page");
		List<String> WarcTrecID = new ArrayList<String>();
		for(Object element:page){
			Object loopObj = parser.parse(element.toString());
			JSONObject loopJsonObject = (JSONObject) loopObj;
			WarcTrecID.add((String) loopJsonObject.get("WarcTrecID"));
		}
		return WarcTrecID;
	}

	/**
	 * This method returns the Unique identified of the fetched document, 
	 * which may later be used to retrieve the contents of the document
	 * 
	 * @param filepath or string
	 * @return LongID
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ParseException
	 */
	public List<String> getLongID(String s) throws FileNotFoundException, IOException, ParseException{
		Object obj;
		if(fileExists(s))
			obj = parser.parse(new FileReader(s));
		else
			obj = parser.parse(s);
		JSONObject jsonObject = (JSONObject) obj;
		JSONArray page = (JSONArray) jsonObject.get("Page");
		List<String> LongID = new ArrayList<String>();
		for(Object element:page){
			Object loopObj = parser.parse(element.toString());
			JSONObject loopJsonObject = (JSONObject) loopObj;
			LongID.add((String) loopJsonObject.get("LongID"));
		}
		return LongID;
	}

	/**
	 * This method returns the number of syllables found in the retuned document
	 * 
	 * @param filepath or string
	 * @return Number of Syllables
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ParseException
	 */
	public List<Long> getNumSyllables(String s) throws FileNotFoundException, IOException, ParseException{
		Object obj;
		if(fileExists(s))
			obj = parser.parse(new FileReader(s));
		else
			obj = parser.parse(s);
		JSONObject jsonObject = (JSONObject) obj;
		JSONArray page = (JSONArray) jsonObject.get("Page");
		List<Long> NumSyllables = new ArrayList<Long>();
		for(Object element:page){
			Object loopObj = parser.parse(element.toString());
			JSONObject loopJsonObject = (JSONObject) loopObj;
			NumSyllables.add((long) loopJsonObject.get("NumSyllables"));
		}
		return NumSyllables;
	}

	/**
	 * This method returns the bing rank assigned to the document. 
	 * But this is mostly not returned by the search engine.
	 * 
	 * @param filepath or string
	 * @return Bing rank
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ParseException
	 */
	public List<Long> getBingRank(String s) throws FileNotFoundException, IOException, ParseException{
		Object obj;
		if(fileExists(s))
			obj = parser.parse(new FileReader(s));
		else
			obj = parser.parse(s);
		JSONObject jsonObject = (JSONObject) obj;
		JSONArray page = (JSONArray) jsonObject.get("Page");
		List<Long> BingRank = new ArrayList<Long>();
		for(Object element:page){
			Object loopObj = parser.parse(element.toString());
			JSONObject loopJsonObject = (JSONObject) loopObj;
			BingRank.add((long) loopJsonObject.get("BingRank"));
		}
		return BingRank;
	}

	/**
	 * This method returns the number of characters found in the document returned by the search engine
	 * 
	 * @param filepath or string
	 * @return Number of Characters
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ParseException
	 */
	public List<Long> getNumCharacters(String s) throws FileNotFoundException, IOException, ParseException{
		Object obj;
		if(fileExists(s))
			obj = parser.parse(new FileReader(s));
		else
			obj = parser.parse(s);
		JSONObject jsonObject = (JSONObject) obj;
		JSONArray page = (JSONArray) jsonObject.get("Page");
		List<Long> NumCharacters = new ArrayList<Long>();
		for(Object element:page){
			Object loopObj = parser.parse(element.toString());
			JSONObject loopJsonObject = (JSONObject) loopObj;
			NumCharacters.add((long) loopJsonObject.get("NumCharacters"));
		}
		return NumCharacters;
	}

	public static void main(String args[]) throws IOException, ParseException{
		JsonInterpreter jp = new JsonInterpreter();
		Scanner sc = new Scanner(new File("/home/arunjayapal/Public/PANCompetition/newDoc/suspicious-document003.html.json"));
		while(sc.hasNext()){
			String jsonVal = sc.nextLine();
			if(jsonVal.equals("No possible queries"))
				continue;
			else{
				if(jsonVal.length() <= 0)
					continue;

				if(jp.getTotalResultCount(jsonVal) <= 0)
					continue;
				
				System.out.println(jp.getHTMLTitle(jsonVal));
			}
		}
		//System.out.println(jp.getBaseURL("../PANCompetition/docs/JSON.txt"));
	}
}
