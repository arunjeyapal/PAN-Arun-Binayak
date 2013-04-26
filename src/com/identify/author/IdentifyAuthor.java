package com.identify.author;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import com.identify.utils.IntersectSimilarity;
import com.identify.utils.CosineSimilarity;

public class IdentifyAuthor {
	public static void main(String args[]) {
		if (args.length == 2) {
			String base_dir = args[0];
			Map<String, Boolean> Klass_list = new TreeMap<String, Boolean>();
			String target_dir = args[1];
			File dir = new File(base_dir);
			File[] firstLevelFiles = dir.listFiles();
			for (File path : firstLevelFiles) {
				if (path.isDirectory()) {
					System.err.format("Starting %s set\n", path.getName());
					File[] secondLevelFiles = path.listFiles();
					HashMap<String, Integer> known_count = new HashMap<String, Integer>();
					HashMap<String, Integer> unknown_count = new HashMap<String, Integer>();
					for (File each_file : secondLevelFiles) {
						String filename = each_file.getName();
						ProcessData pd = new ProcessData(each_file);
						if (filename.equals("unknown.txt")) {
							for (String strs : pd.getNCharCount(4)) {
								if (unknown_count.containsKey(strs)) {
									int value = unknown_count.get(strs);
									unknown_count.put(strs, ++value);
								} else
									unknown_count.put(strs, 1);
							}
						} else {
							for (String strs : pd.getNCharCount(4)) {
								if (known_count.containsKey(strs)) {
									int value = known_count.get(strs);
									known_count.put(strs, ++value);
								} else
									known_count.put(strs, 1);
							}
						}
					}
					double intersect_similarity_val = IntersectSimilarity
							.getComparisonscore(known_count.keySet(),
									unknown_count.keySet());

					CosineSimilarity cs = new CosineSimilarity(known_count,
							unknown_count);
					double cosine_similarity_val = cs.computeSimilarity();
					boolean klass = ClassSelection.selectClass(
							intersect_similarity_val, cosine_similarity_val);
					Klass_list.put(path.getName(), klass);
				}
			}
			ClassSelection.writeResults(new File(target_dir), Klass_list);
			System.out.format("\n\nGenerated results at %s/%s", target_dir, "Author-Results.txt");
		} else {
			System.err.println("Unexpected number of commandline arguments.\n"
					+ "Usage: java -jar IdentifyAuthor.jar {Directo"
					+ "ry where subdirectories of known and unknown " +
					"files are available} {destination folder}\n");
		}
	}
}
