package com.candidate.retrieval;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;

import net.didion.jwnl.JWNLException;

import org.json.simple.parser.ParseException;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.tools.nlputil.POStagger;
import com.tools.nlputil.Tokenizer;
import com.tools.util.FileOps;
import com.tools.util.FileUtil;
import com.tools.util.WordTag;

public class Main {

	/**
	 * This is the main method to formulate query for
	 * retrieving the candidate documents for the 
	 * Plagiarism detection task.
	 * 
	 * All the methods are called in the right sequence
	 * 
	 * @param args
	 * @throws IOException 
	 * @throws ParseException 
	 * @throws JWNLException 
	 */
	public static void main(String[] args) throws IOException, ParseException, JWNLException {
		
		if(args.length == 2)
		{
			/*All Initializations*/
			Tokenizer tk = new Tokenizer();
			POStagger pos = new POStagger();
			StopTags stoptag = new StopTags();
			ArrayList<WordTag> doc_wordtags = new ArrayList<WordTag>();
			//"../PANCompetition/docs/pan12-plagiarism-candidate-retrieval-training-corpus-2012-03-31/";
			String src, dest;
			if (args[0].endsWith("/"))
				src = args[0];
			else
				src = args[0] + "/";
			
			if (args[1].endsWith("/"))
				dest = args[1];
			else
				dest = args[1];
			
			ArrayList<File> filelist = FileUtil.getFilelist(src, "html");
			FileOps.createDirectory(new File(dest));
			/**
			 * For each file from the file list, form queries and retrieve data from the search engine
			 */
			for(File files:filelist){
				File newFile = new File(dest+files.getName()+".json");
				/*Retrieve data from HTML files to get the text*/
				RetrieveData rt = new RetrieveData(files.toString(), true);
				//RetrieveData rt = new RetrieveData("../PANCompetition/docs/pan12-plagiarism-candidate-retrieval-training-corpus-2012-03-31/test.html");
				Elements content = rt.parseData();

				String searchResult;
				FileOutputStream fo = new FileOutputStream(newFile);
				PrintStream ps = new PrintStream(fo);

				/* to compare the results*/
				HashSet<String> resultset = new HashSet<String>();
				for(Element para:content){
					searchResult = ExecFlow.systemFlow(para.text(),tk,pos,stoptag,doc_wordtags,false);
					if(!resultset.contains(searchResult))
						ps.print(searchResult);
					//				if(searchResult.equals("\nNo possible queries"))
					//					continue;

					//				if(jp.getTotalResultCount(searchResult) < 1){
					//					/**
					//					 * If no results are returned by the search engine, frame a new sentence
					//					 * based on synsets and continue. This rephrasing system will not happen
					//					 * more than once 
					//					 */
					//					String newString = WordNet.getModifiedSent(para.text());
					//					searchResult = ExecFlow.systemFlow(newString,tk,pos,stoptag,doc_wordtags,true);
					//					ps.print(searchResult);
					//				}
				}
				ps.close();
				fo.close();
			}
		}
		else{
			System.err
			.println("Unexpected number of commandline arguments.\n"
					+ "Usage: java -jar DocumentRetrieval.jar {Directory where html files are stored} {destination folder}\n");
		}
	}
}