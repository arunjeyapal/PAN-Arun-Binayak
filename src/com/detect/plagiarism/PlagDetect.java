package com.detect.plagiarism;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

import com.detect.plagiarism.XMLfunctions;
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
public class PlagDetect {
	
	/**
	 * Reads the whole file and returns the file data after stemming the tokens.
	 * Further, returns the data in sentence_wordmap format
	 * 
	 * @param filePath
	 * @return string
	 * @throws IOException
	 */
	public static String readFileAsString(File filePath) throws IOException {
		Scanner sc = new Scanner(filePath);
		StringBuilder sb = new StringBuilder();
		while (sc.hasNext()) {
			String line = sc.nextLine().replaceAll("\\n", " ") + " ";
			line = NormalizeWords.removeDiacritics(line);
			sb.append(line);
		}
		return sb.toString().toUpperCase();
	}
	
	public class XMLData{
		public int susp_offset;
		public int src_offset;
		public int susp_length;
		public int src_length;
		public XMLData(int susp_offset, int src_offset, int susp_length, int src_length){
			this.susp_offset = susp_offset;
			this.src_offset = src_offset;
			this.susp_length = susp_length;
			this.src_length = src_length;
		}
	}

	/**
	 * Main method to process the given file pair and performs comparison using
	 * GST (Greedy String Tiling) algorithm and writes the features to Files
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String args[]) throws Exception {
		PlagDetect pd = new PlagDetect();
		if (args.length == 4) {
			String pairs_file = args[0], dest;
			String susp_dir = args[1], src_dir = args[2];

			// Pairs file opened..
			File pair = new File(pairs_file);
			if (args[1].endsWith("/"))
				dest = args[3];
			else
				dest = args[3] + "/";

			// create destination directory
			FileOps.createDirectory(new File(dest));

			Scanner scan = new Scanner(pair);
			System.out.println("Processing documents..\n"
					+ "This will take long time to generate the xml documents");
			while (scan.hasNext()) {
				ArrayList<XMLData> offsets = new ArrayList<XMLData>();
				String[] susp_src = scan.nextLine().split("\\s");
				String susp_file_name, src_file_name;
				
				if (susp_dir.endsWith("/"))
					 susp_file_name = susp_dir+susp_src[0];
				else 
					susp_file_name = susp_dir+"/"+susp_src[0];
				
				if (src_dir.endsWith("/"))
					src_file_name = src_dir+susp_src[1];
				else 
					src_file_name = src_dir+"/"+susp_src[1];
				
//				String susp_file_name = pair.getParent() + "/../susp/"
//						+ susp_src[0];
//				String src_file_name = pair.getParent() + "/../src/"
//						+ susp_src[1];
				File susp_file = new File(susp_file_name);
				File src_file = new File(src_file_name);
				String susp_text = readFileAsString(susp_file);
				String src_text = readFileAsString(src_file);
				HashSet<Integer> sus_offsets = new HashSet<Integer>();
				HashSet<Integer> src_offsets = new HashSet<Integer>();
//				HashSet<String> susp_str_set = new HashSet<String>();
//				HashSet<String> src_str_set = new HashSet<String>();
				susp_text = susp_text.replaceAll("\\s+", " ").replaceAll(
						"[^A-Za-z0-9]", " ");
				src_text = src_text.replaceAll("\\s+", " ").replaceAll(
						"[^A-Za-z0-9]", " ");
				GreedyStringTiling gst = new GreedyStringTiling();
				PlagResult string_tiles = gst.run(susp_text, src_text, 5,
						(float) 0.5);
				String xmlFile = dest + susp_src[0].split("\\.")[0] + "-"
						+ susp_src[1].split("\\.")[0] + ".xml";
				XMLfunctions xmlf = new XMLfunctions(xmlFile, susp_src);
				xmlf.beginXML();
				XMLData xml = null;
				for (MatchVals tiles : string_tiles.getTiles()) {
					int susp_offset = CharOffset.getWordCharOffset(
							tiles.patternPostion, susp_text);
					int src_offset = CharOffset.getWordCharOffset(
							tiles.textPosition, src_text);
					int susp_length = 0, src_length = 0;
					try {
						susp_length = CharOffset.getOffsetStringValue(
								susp_text, tiles.patternPostion,
								tiles.length, true).length()-1;
						src_length = CharOffset.getOffsetStringValue(
								src_text, tiles.textPosition, tiles.length,
								true).length()-1;
					} catch (StringIndexOutOfBoundsException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}
					
					/* Although more detections are found, they are reported
					 * only once
					 */
					if (sus_offsets.contains(susp_offset)
							|| src_offsets.contains(src_offset))
						continue;

					// Problem with identifying offset
					if (src_length == 0 && susp_length == 0) continue;
					if (src_length == 0) src_length = susp_length;
					else if (susp_length == 0) susp_length = src_length;
					
					int offset_list_len = offsets.size();
//					System.out.println(susp_offset+":"+susp_length+":"+src_offset+":"+src_length);
					if (offset_list_len > 0) {
//						System.out.println("Prev:"+offsets.get(offset_list_len-1).susp_offset+
//								":"+offsets.get(offset_list_len-1).susp_length+":"+
//								offsets.get(offset_list_len-1).src_offset+":"+
//								offsets.get(offset_list_len-1).src_length);
						if(susp_offset-(offsets.get(offset_list_len-1).susp_offset+
								offsets.get(offset_list_len-1).susp_length)<=50 && 
								susp_offset-(offsets.get(offset_list_len-1).susp_offset+
										offsets.get(offset_list_len-1).susp_length)>0){
							offsets.get(offset_list_len-1).susp_length = offsets.get(offset_list_len-1).susp_length+
									susp_length+(susp_offset-(offsets.get(offset_list_len-1).susp_offset+
											offsets.get(offset_list_len-1).susp_length));
						}
						if(src_offset-(offsets.get(offset_list_len-1).src_offset+
								offsets.get(offset_list_len-1).src_length)<=50 &&
								src_offset-(offsets.get(offset_list_len-1).src_offset+
										offsets.get(offset_list_len-1).src_length)>0){
							offsets.get(offset_list_len-1).src_length = offsets.get(offset_list_len-1).src_length+
									src_length+(src_offset-(offsets.get(offset_list_len-1).src_offset+
											offsets.get(offset_list_len-1).src_length));
						} else {
							xml = pd.new XMLData(susp_offset, src_offset, susp_length, src_length);
							offsets.add(xml);
						}
					}
					else {
						xml = pd.new XMLData(susp_offset, src_offset, susp_length, src_length);
						offsets.add(xml);
					}
					sus_offsets.add(susp_offset);
					src_offsets.add(src_offset);
				}
				xmlf.genFeaturesXML(offsets);
				xmlf.endXML();
				System.out.println("Output generated at " + xmlFile);
			}
		} else {
			System.err
					.println("Unexpected number of commandline arguments.\n"
							+ "Usage: java -jar DetectPlagiarism.jar {source pairs file} " +
							"{suspicious folder} {source folder} {destination folder}\n");
		}
	}
}