package com.candidate.retrieval;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;

import org.json.simple.parser.ParseException;

import com.tools.nlputil.POStagger;
import com.tools.nlputil.SentenceSegmenter;
import com.tools.nlputil.Tokenizer;
import com.tools.util.FileOps;
import com.tools.util.FileUtil;
import com.tools.util.WordTag;

public class Main {

	/**
	 * This is the main method to formulate query for retrieving the candidate
	 * documents for the Plagiarism detection task.
	 * 
	 * All the methods are called in the right sequence
	 * 
	 * @param args
	 * @throws IOException
	 * @throws ParseException
	 */
	public static void main(String[] args) throws IOException, ParseException {

		if (args.length == 2) {
			/**
			 * All Initializations
			 */
			Tokenizer tk = new Tokenizer();
			POStagger pos = new POStagger();
			StopTags stoptag = new StopTags();
			SentenceSegmenter sentence = new SentenceSegmenter();
			ArrayList<WordTag> doc_wordtags = new ArrayList<WordTag>();

			String src, dest;
			if (args[0].endsWith("/"))
				src = args[0];
			else
				src = args[0] + "/";

			if (args[1].endsWith("/"))
				dest = args[1];
			else
				dest = args[1] + "/";

			ArrayList<File> filelist = FileUtil.getFilelist(src, "txt");

			/**
			 * Directory to store the JSON files
			 */

			FileOps.createDirectory(new File(dest));

			/**
			 * For each file from the file list, form queries and retrieve data
			 * from the search engine
			 */
			for (File files : filelist) {

				String text = FileOps.readFileAsString(files);

				/**
				 * File top store the search results
				 */
				String dest_file = String.format("%s%s%s", dest, files.getName(), ".json");
				File newFile = new File(dest_file);
				String fileid = files.getName().split("suspicious-document")[1]
						.split("\\.")[0];
				String searchResult;
				FileOutputStream fo = new FileOutputStream(newFile);
				PrintStream ps = new PrintStream(fo);

				String[] sentences = sentence.segmenter(text);

				/**
				 * Take paragraphs to reduce the number of queries passed to the
				 * search engine. Also, this avoids junk results avoiding less
				 * number of search terms.
				 */
				int i = 0;
				StringBuilder paragraph = new StringBuilder();
				ArrayList<String> paragraphs = new ArrayList<String>();
				for (String data : sentences) {
					if (i >= 4) {
						paragraphs.add(paragraph.toString());
						i = 0;
						paragraph = new StringBuilder();
					}
					paragraph.append(data + " ");
					++i;
				}
				// System.out.println(paragraphs.size());

				/* to compare the results */
				HashSet<String> resultset = new HashSet<String>();
				for (String sent : paragraphs) {
					searchResult = ExecFlow.systemFlow(sent, tk, pos, stoptag,
							doc_wordtags, fileid, false);
					if (!resultset.contains(searchResult))
						ps.print(searchResult);
				}
				ps.close();
				fo.close();
			}

		} else {
			System.err.println("Unexpected number of commandline arguments.\n"
					+ "Usage: java -jar DocumentRetrieval.jar {Directo"
					+ "ry where html files are stored} {destination folder}\n");
		}
	}
}