package com.candidate.retrieval.analysis;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import com.tools.util.FileUtil;

public class FilterResults {

	public FilterResults() {
	}

	protected static ArrayList<RemovableLists> filterResults(
			ArrayList<RemovableLists> map) {
		int result_size = map.size(), max_cap = 20;
		Double max_val, normalized_score;
		Double threshold1 = 0.09, threshold2 = 0.009;
		max_val = map.get(0).score;
		if (max_val < threshold2)
			max_cap = 5;
		if (result_size < max_cap)
			max_cap = result_size;

		for (int i = 0; i < max_cap; i++) {
			Double score = map.get(i).score;
			normalized_score = score / max_val;
			if (normalized_score < threshold1){
				map.get(i).score = null;
				max_cap = i;
				break;
			}
			map.get(i).score = normalized_score;
		}
		
		if (max_cap < result_size) {
			int j = map.size() - 1;
			while (j != max_cap - 1) {
				map.remove(j);
				j--;
			}
		}
		return map;
	}

	public void sortedResults(String src_dir, String dest_dir) throws IOException {
		ArrayList<File> filelist = FileUtil.getFilelist(src_dir, "res");
		for (File file : filelist) {
			ArrayList<RemovableLists> result_map = new ArrayList<RemovableLists>();
			Scanner scan = new Scanner(file);
			while (scan.hasNextLine()) {
				RemovableLists rl = null;
				String[] line = scan.nextLine().split("\\s");
				Double value;
				try {
					value = Double.parseDouble(line[0]);
				} catch (NumberFormatException e) {
					continue;
				}
				rl = this.new RemovableLists(line[1], value);
				result_map.add(rl);
			}
			FileWriter fw = new FileWriter(new File(file.toString()));
			Double factor = 1e5;
			for (RemovableLists itr : FilterResults.filterResults(result_map)) {
				StringBuilder sb = new StringBuilder();

				String towrite = sb
						.append((Math.round(itr.score * factor) / factor))
						.append(" ").append(itr.id.toString()).append("\n")
						.toString();
				fw.write(towrite);
			}
			fw.close();
		}
	}

	public class RemovableLists {
		public final String id;
		public Double score;

		public RemovableLists(String id, Double score) {
			this.id = id;
			this.score = score;
		}
	}

	public static void main(String args[]) throws IOException {
		if (args.length == 2) {
			FilterResults fr = new FilterResults();
			fr.sortedResults(args[0], args[1]);
		}else{
			System.err
			.println("Unexpected number of commandline arguments.\n");
		}
		
	}
}
