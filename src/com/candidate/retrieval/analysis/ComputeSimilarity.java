package com.candidate.retrieval.analysis;

import java.io.EOFException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import org.json.simple.parser.ParseException;
import org.jsoup.nodes.Element;

import com.candidate.retrieval.GetResponse;
import com.candidate.retrieval.JsonInterpreter;
import com.candidate.retrieval.RetrieveData;
import com.detect.plagiarism.external.DetectPlagiarism;
import com.tools.nlputil.NGramSplitter;
import com.tools.nlputil.PorterStemmer;
import com.tools.util.DocGram;
import com.tools.util.FileUtil;
import com.tools.util.JSONdata;

public class ComputeSimilarity {
	ArrayList<File> filelistJSON = new ArrayList<File>();
	ArrayList<File> filelistSuspicious = new ArrayList<File>();
	PorterStemmer pm = new PorterStemmer();
	/**
	 * Constructor initializes (extracts the files from the given directories) the filelist.
	 *  
	 * @param JSONdirectory
	 * @param Sus_directory
	 */
	public ComputeSimilarity(String JSONdirectory, String Sus_directory){
		this.filelistJSON = FileUtil.getFilelist(JSONdirectory, "json");
		this.filelistSuspicious = FileUtil.getFilelist(Sus_directory, "txt");
	}

	/**
	 * Uses the NGramSplitter to split the given text into ngrams
	 * 
	 * @param text
	 * @param ngram
	 * @return Ngrams of the text in List<String> format
	 */
	public List<String> getNGrams(String text, int ngram){
		return NGramSplitter.ngrams(ngram, text);
	}


	/**
	 * getHTMLData method is used to retrieve the text data from the HTML file 
	 * 
	 * @param htmlfile
	 * @return text parsed from HTML
	 * @throws IOException
	 */
	public String getHTMLData(File htmlfile) throws IOException{
		StringBuffer text = new StringBuffer();
		RetrieveData rd = new RetrieveData(htmlfile.toString(), true);
		for(Element element:rd.parseData("body")){
			text.append(element.text());
		}
		return text.toString();
	}

	public boolean isHTML(File file){
		if(file.getName().toString().split("\\.")[1].equals("html"))
			return true;
		else
			return false;
	}

	/**
	 * Computes N-grams of Suspicious files (after stemming) and stored in ArrayList along with filenames 
	 * 
	 * @param ngram
	 * @return N-grams of Suspicious files
	 * @throws IOException
	 */
	public ArrayList<DocGram> getSuspiciousFileGrams(int ngram) throws IOException{
		ArrayList<DocGram> docgrams = new ArrayList<DocGram>();
		for(File file:filelistSuspicious){
			String fileName = file.getName().toString();
			List<String> ngrams = new ArrayList<String>();
			
			if(isHTML(file))
				ngrams = this.getNGrams(pm.stem(this.getHTMLData(file).toLowerCase()),ngram);
			else
				ngrams = this.getNGrams(pm.stem(DetectPlagiarism.readFileAsString(file).toLowerCase()),ngram);
			
			DocGram dg = new DocGram(fileName.split("\\.")[0],ngrams);
			docgrams.add(dg);
		}
		return docgrams;
	}

	/**
	 * Computes N-grams of all the candidate documents (after stemming) 
	 * for the suspicious documents in HashMap format
	 * 
	 * @param ngram value
	 * @return ngrams of the candidate files in HashMap format
	 * @throws ParseException 
	 * @throws IOException 
	 */
	public HashMap<String,ArrayList<JSONdata>> getCandidateFileGrams(int ngram) throws IOException, ParseException, EOFException, NullPointerException, InvocationTargetException{
		JsonInterpreter jp = new JsonInterpreter();
		HashMap<String,ArrayList<JSONdata>> all_data = new HashMap<String,ArrayList<JSONdata>>();
		for(File jsonfile:filelistJSON){
			//System.out.println("\n\n\n" + "New File: "+jsonfile);
			//String fileName = jsonfile.getName().toString();
			//FileOps.createDirectory(new File(fileName.split("\\.")[0]));
			//File fn = new File(fileName);
			String fileid = jsonfile.toString().split("suspicious-document")[1].split("\\.")[0];
			Scanner scan = new Scanner(jsonfile);
			ArrayList<JSONdata> jsonVals = new ArrayList<JSONdata>();
			HashSet<String> uniqueIds = new HashSet<String>();
			while(scan.hasNext()){
				ArrayList<List<String>> ngramData = new ArrayList<List<String>>();
				String valJson = scan.nextLine();
				//System.out.println("Am here:"+valJson);
				JSONdata jd = null;
				if(valJson.equals("No possible queries"))
					continue;
				else{
					if(valJson.length() <= 0)
						continue;

					if(jp.getTotalResultCount(valJson) <= 0)
						continue;
					
					String first = "http://webis15.medien.uni-weimar.de/chatnoir/clueweb?id=";
					String last = "&token=525885a76c1a47d56987b1c87edbb2f9";
					for(String long_data:jp.getLongID(valJson)){
						//Reduces the number of server accesses
						if(uniqueIds.contains(long_data))
							continue;
						else
							uniqueIds.add(long_data);
						
						//FileWriter fw = new FileWriter(new File(long_data));
						StringBuilder sb = new StringBuilder();
						/**
						 * for each document, get the HTML data corresponding to the long id
						 */
						String url = sb.append(first).append(long_data.split("#")[1]).append(last).append(fileid).toString();
						System.out.println(long_data+" Query: "+url);
						GetResponse gr = new GetResponse(new URL(url));
						String resp = gr.returnResponse();
						//fw.write(resp);

						/**
						 * The HTML data is parsed to retrieve the data from the body 
						 */
						RetrieveData rd = new RetrieveData(resp,false);
						for(Element element:rd.parseData("body")){
							/**
							 * get N-grams of the retrieved text
							 */
							ngramData.add(this.getNGrams(pm.stem(element.text().toLowerCase()),ngram));
						}
					}
					//jd = new JSONdata(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
					jd = new JSONdata(jp.getWarcTargetURI(valJson), jp.getBaseURL(valJson), jp.getHTMLTitle(valJson), jp.getPageRank(valJson), jp.getSpamRank(valJson), jp.getNumSentences(valJson), jp.getWeight(valJson), jp.getNormalizedPagerank(valJson), jp.getNumWords(valJson), null, jp.getProximity(valJson), jp.getWarcTrecID(valJson), jp.getLongID(valJson), jp.getNumSyllables(valJson), jp.getNumCharacters(valJson), ngramData);
				}
				jsonVals.add(jd);
			}
			all_data.put(jsonfile.getName().split("\\.")[0], jsonVals);
			//System.out.println(all_data);
		}

		//System.out.println(all_data);
		return all_data;
	}

	/**
	 * Computes the similarity score of given set of documents and
	 * prints them in the descending order.
	 * 
	 * @param ngram
	 * @throws IOException 
	 * @throws ParseException 
	 */
	public void similarity(int ngram) throws IOException, ParseException, NullPointerException, InvocationTargetException {
		HashMap<String, HashMap<String, Double>> all_scores = Comparefiles.getComparisonScore(getSuspiciousFileGrams(ngram),getCandidateFileGrams(ngram));
		String first = "http://webis15.medien.uni-weimar.de/chatnoir/clueweb?id=";
		String last = "&token=525885a76c1a47d56987b1c87edbb2f9";
		for(String documents:all_scores.keySet()){
			FileWriter fw = new FileWriter(new File(documents+".res"));
			TreeMap<String, Double> sorted_map = sort(all_scores.get(documents));
			System.out.println(documents+" :");
			//int threshold = 10, i=0;
			for(String key:sorted_map.keySet()){
				StringBuilder sb = new StringBuilder();
				String toWrite = sb.append(sorted_map.get(key)+" ").append(first).append(key).append(last).append(documents.split("suspicious-document")[1]).append("\n").toString();
				System.out.println("\t"+key+" : "+sorted_map.get(key));
				fw.write(toWrite);
			}
			fw.close();
		}
	}

	@SuppressWarnings("unchecked")
	public TreeMap<String, Double> sort(HashMap<String,Double> unsortedMap){
		ValueComparator bvc =  new ValueComparator(unsortedMap);
		@SuppressWarnings("rawtypes")
		TreeMap<String,Double> sorted_map = new TreeMap(bvc);
		sorted_map.putAll(unsortedMap);
		return sorted_map;
	}

	@SuppressWarnings("rawtypes")
	class ValueComparator implements Comparator {

		Map base;
		public ValueComparator(Map base) {
			this.base = base;
		}

		public int compare(Object a, Object b) {

			if((Double)base.get(a) < (Double)base.get(b)) {
				return 1;
			} else if((Double)base.get(a) == (Double)base.get(b)) {
				return 0;
			} else {
				return -1;
			}
		}
	}

	public static void main(String s[]) throws IOException, ParseException{
		//../PANCompetition/newDoc/
		//../PANCompetition/docs/pan12-plagiarism-candidate-retrieval-training-corpus-2012-03-31/
		//"/home/arunjayapal/Public/PANCompetition/docs/pan12-plagiarism-candidate-retrieval-test-corpus/Results","/home/arunjayapal/Public/PANCompetition/docs/pan12-plagiarism-candidate-retrieval-test-corpus"
		ComputeSimilarity cs = new ComputeSimilarity(s[0],s[1]);
//		ComputeSimilarity cs = new ComputeSimilarity("/home/arunjayapal/Public/Competition/PANCompetition/docs/second-phase-pan12-plagiarism-candidate-retrieval-test-corpus-2012-07-01-Output/","/home/arunjayapal/Public/Competition/PANCompetition/docs/second-phase-pan12-plagiarism-candidate-retrieval-test-corpus-2012-07-01/");
		try {
			cs.similarity(5);
		} catch (NullPointerException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//cs.getCandidateFileGrams(3);
	}
}