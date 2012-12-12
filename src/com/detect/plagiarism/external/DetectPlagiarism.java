package com.detect.plagiarism.external;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Scanner;

import com.bing.translate.DetectLanguage;
import com.bing.translate.Translator;
import com.memetix.mst.language.Language;
import com.tools.nlputil.SentenceDetector;
import com.tools.nlputil.gst.GreedyStringTiling;
import com.tools.nlputil.gst.MatchVals;
import com.tools.nlputil.gst.PlagResult;
import com.tools.util.CharOffset;
import com.tools.util.FileOps;
import com.tools.util.NormalizeWords;

/**
 * This is the main class which uses the Greedy String Tiling algorithm to
 * detect plagiarism from the given text and generate XML features with the
 * detected plagiarisms.
 * 
 * @author arunjayapal
 */
public class DetectPlagiarism {

	/**
	 * Reads the whole file and returns the file data as a string
	 * 
	 * @param filePath
	 * @return string
	 * @throws IOException
	 */
	public static String readFileAsString(File filePath) throws IOException {
		Scanner sc = new Scanner(filePath);
		StringBuilder sb = new StringBuilder();
		while (sc.hasNext()) {
			String line = sc.nextLine() + " ";
			sb.append(line);
		}
		return sb.toString().toUpperCase();
	}

	/**
	 * Main method to process the given file pair and performs comparison using
	 * GST (Greedy String Tiling) algorithm and writes the features to Files
	 * 
	 * @param args
	 * @throws Exception 
	 */
	@SuppressWarnings("static-access")
	public static void main(String args[]) throws Exception {
		if (args.length == 2) {
			File pair = new File(args[0]);
			String dest;
			if (args[1].endsWith("/"))
				dest = args[1];
			else
				dest = args[1] + "/";

			// create destination directory
			FileOps.createDirectory(new File(dest));

			Scanner scan = new Scanner(pair);
			System.out
					.println("Processing documents..\n This will take long time to generate the xml documents");
			
			while (scan.hasNext()) {
				IdentifyPlagType ipt = new IdentifyPlagType("./model/meta-Randomsubspace-10fold-moreAttr.model");
				String[] sus_src = scan.nextLine().split("\\s+");
				String sus_file = pair.getParent() + "/../susp/" + sus_src[0];
				String src_file = pair.getParent() + "/../src/" + sus_src[1];
				File suspicious_data = new File(sus_file);
				String sus_text = readFileAsString(suspicious_data);
				File source_data = new File(src_file);
				String src_text = readFileAsString(source_data);
				GreedyStringTiling gst = new GreedyStringTiling();
				PlagResult results = gst.run(sus_text, src_text,
						3, (float) 0.3);
				String xmlFile = dest + sus_src[0].split("\\.")[0] + "-"
						+ sus_src[1].split("\\.")[0] + ".xml";
				XMLfunctions xmlf = new XMLfunctions(xmlFile, sus_src);
				xmlf.beginXML();
				
				/**
				 * Identify the type of plagiarism
				 */
				String type_of_plag = "";
				try {
					type_of_plag = ipt.setClassifier(sus_file, src_file, results);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				System.out.println(type_of_plag);
				// To maintain granularity define set of the pattern position
				HashSet<Integer> sus_offsets = new HashSet<Integer>();
				HashSet<Integer> src_offsets = new HashSet<Integer>();
				
				/**
				 * As the type of plagiarism changes for each pair,
				 * the tiles threshold is varied. If the given pair
				 * has no plagiarism, an empty feature set is returned.
				 */
				int threshold = 10;
				if(type_of_plag.equals("no_plagiarism")){
					xmlf.endXML();
					System.out.println("Output generated at " + xmlFile);
					continue;
				}
				else if(type_of_plag.equals("no-obfuscation"))
					threshold = 15;
				else if(type_of_plag.equals("artificial-low"))
					threshold = 7;
				else if(type_of_plag.equals("artificial-high"))
					threshold = 5;
				else if(type_of_plag.equals("translation")){
					String[] srcStrings = src_text.split("[\\s+|\\w+]");
					StringBuilder sb = new StringBuilder();
					for(int i=0; i<5; i++){
						sb.append(srcStrings[i]+" ");
					}
					//Detect the language of the source file
					Language lang = DetectLanguage.identify(sb.toString());
					//String trans_text = Translator.translate(sb.toString(), lang);
					//System.out.println(trans_text);
					String[] sentences;
					if (lang.equals(Language.GERMAN))
						sentences = SentenceDetector.splitSentence("./model/de-sent.bin", src_text);
					else if(lang.equals(Language.PORTUGUESE))
						sentences = SentenceDetector.splitSentence("./model/pt-sent.bin", src_text);
					else if(lang.equals(Language.DANISH))
						sentences = SentenceDetector.splitSentence("./model/da-sent.bin", src_text);
					else
						sentences = SentenceDetector.splitSentence("./model/en-sent.bin", src_text);
					
					StringBuilder trans = new StringBuilder();
					for(int i=0; i<sentences.length; i++){
						String trans_text = Translator.translate(sentences[i], lang);
						String Stripped_text = NormalizeWords.removeDiacritics(trans_text);
						trans.append(Stripped_text +" ");
					}
					
					results = gst.run(sus_text, trans.toString(),
							3, (float) 0.3);
					
					threshold = 5;
				}
				else if(type_of_plag.equals("simulated-paraphrase"))
					threshold = 10;
					
				for (MatchVals tiles : results.getTiles()) {
					if (tiles.length >= threshold) {
						int sus_offset = CharOffset.getWordCharOffset(
								tiles.patternPostion, sus_text);
						int src_offset = CharOffset.getWordCharOffset(
								tiles.textPosition, src_text);
						int sus_length = 0, src_length = 0;
						try {
							sus_length = CharOffset.getOffsetStringValue(
									sus_text, tiles.patternPostion,
									tiles.length, true).length();
							src_length = CharOffset.getOffsetStringValue(
									src_text, tiles.textPosition, tiles.length,
									true).length();
						} catch (StringIndexOutOfBoundsException e) {
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							e.printStackTrace();
						}

						// Although more detections are found, they are reported
						// only once
						if (sus_offsets.contains(sus_offset)
								|| src_offsets.contains(src_offset))
							continue;

						// Problem with identifying offset
						if (src_length == 0 && sus_length == 0)
							continue;

						if (src_length == 0)
							src_length = sus_length;
						else if (sus_length == 0)
							sus_length = src_length;

						xmlf.genFeaturesXML(sus_offset, src_offset, sus_length,
								src_length);
						sus_offsets.add(sus_offset);
						src_offsets.add(src_offset);
					}
				}
				xmlf.endXML();
				System.out.println("Output generated at " + xmlFile);
			}
			System.out.println("\n\nAll documents are processed.");
		} else {
			System.err
					.println("Unexpected number of commandline arguments.\n"
							+ "Usage: DetectPlagiarism {source pairs file} {destination folder}\n");
		}
	}
}