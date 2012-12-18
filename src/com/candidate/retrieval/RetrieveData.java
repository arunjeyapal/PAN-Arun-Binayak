package com.candidate.retrieval;

import java.io.File;
import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * This piece of code uses jsoup library to parse the data and extract
 * the paragraphs/span data from the html document
 *  
 * @author arunjayapal
 *
 */
public class RetrieveData {
	
	public String file;
	public boolean is_file = true;
	/**
	 * Initializes the file
	 * 
	 * @param file or text
	 */
	public RetrieveData(String file, boolean is_file){
		this.file = file;
		this.is_file = is_file;
	}
	
	/**
	 * Parses the HTML text and gets the Elements by "Span" tag
	 * 
	 * @return content in Elements format
	 * @throws IOException
	 */
	public Elements parseData() throws IOException{
		File input = new File(file);
		Document doc =  Jsoup.parse(input, "utf-8");
		Elements content = doc.getElementsByTag("span");
		return content;			
	}
	
	/**
	 * Parses the HTML text and gets the Elements by any given tag
	 * 
	 * @param tag
	 * @return content in Elements format
	 * @throws IOException
	 */
	public Elements parseData(String tag) throws IOException{
		File input; Document doc;
		if(is_file){
			input = new File(file);
			doc =  Jsoup.parse(input, "utf-8");
		}
		else
			doc = Jsoup.parse(file);
		Elements content = doc.getElementsByTag(tag);
		return content;			
	}
	
	/**
	 * For Testing
	 * @param s
	 * @throws IOException
	 */
	public static void main(String s[]) throws IOException{
		RetrieveData rt = new RetrieveData("../PANCompetition/docs/pan12-plagiarism-candidate-retrieval-training-corpus-2012-03-31/suspicious-document001.html", true);
		Elements content = rt.parseData();
		for(Element para:content){
			System.out.println(para.text());
		}
	}
}
