package com.detect.plagiarism.external;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Scanner;

import com.tools.nlputil.gst.GreedyStringTiling;
import com.tools.nlputil.gst.MatchVals;
import com.tools.nlputil.gst.PlagResult;
import com.tools.util.CharOffset;
import com.tools.util.FileOps;

/**
 * This is the main class which uses the Greedy String Tiling algorithm to
 * detect plagiarism from the given text and generate XML features with the
 * detected plagiarisms.
 * 
 * @author arunjayapal
 */
public class DetectPlagWin {

	/**
	 * Reads the whole file and returns the file data as a string
	 * 
	 * @param filePath
	 * @return string
	 * @throws IOException
	 */
	private static String readFileAsString(File filePath) throws IOException {
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
	 * @throws IOException
	 * @throws InvocationTargetException
	 * @throws StringIndexOutOfBoundsException
	 */
	public static void main(String args[]) throws IOException,
			StringIndexOutOfBoundsException, InvocationTargetException {
		if (args.length == 2) {
			File pair = new File(args[0]);
			String dest;
			if (args[1].endsWith("\\"))
				dest = args[1];
			else
				dest = args[1] + "\\";

			FileOps.createDirectory(new File(dest));
			// String argval =
			// "C:\\PANCompetition\\docs\\pan12-detailed-comparison-training-corpus\\02_no_obfuscation\\pairs1";
			Scanner scan = new Scanner(pair);
			
			while (scan.hasNext()) {
				IdentifyPlagType ipt = new IdentifyPlagType(".\\models\\final-meta-Randomsubspace-EntireTraining-89Acc.model");
				String[] sus_src = scan.nextLine().split("\\s+");
				String sus_file = pair.getParent() + "\\..\\susp\\"
						+ sus_src[0];
				String src_file = pair.getParent() + "\\..\\src\\" + sus_src[1];
				File suspicious_data = new File(sus_file);
				String sus_text = readFileAsString(suspicious_data);
				File source_data = new File(src_file);
				String src_text = readFileAsString(source_data);
				PlagResult results = GreedyStringTiling.run(sus_text, src_text,
						4, (float) 0.3);

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
				else if(type_of_plag.equals("translation"))
					threshold = 3;
				else if(type_of_plag.equals("simulated-paraphrase"))
					threshold = 5;

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