package com.identify.author;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

import com.identify.utils.FileOps;

/**
 * Class selection based on intersect and cosine similarity vals between the set
 * of documents and the unknown document
 * 
 * @author arunjayapal
 * 
 */
public class ClassSelection {
	
	/**
	 * Rules to get the right author based on the similarity values
	 * 
	 * @param intersect_similarity_val
	 * @param cosine_similarity_val
	 * @return boolean value
	 */
	public static boolean selectClass(double intersect_similarity_val,
			double cosine_similarity_val) {
		if (intersect_similarity_val > 0.5 && cosine_similarity_val > 0.0009)
			return true;
		else if (intersect_similarity_val > 0.8)
			return true;
		else if (cosine_similarity_val > 0.09)
			return true;
		return false;
	}
	
	/**
	 * Write results to file in the target directory
	 */
	public static void writeResults(File file, Map<String, Boolean> results){
		FileOps.createDirectory(file);
		Set<String> keys = results.keySet(); 
		File file_to_write;
		if (file.toString().endsWith("/")){
			file_to_write = new File(file.toString()+"Author-Results.txt");
		} else {
			file_to_write = new File(file.toString()+"/Author-Results.txt");
		}
		
		try {
			FileWriter fw = new FileWriter(file_to_write);
			for(String key: keys){
				int val = 0;
				if (results.get(key) == true)
					val = 1;
				fw.write(key+" "+val+"\n");
			}
			fw.close();
		} catch (IOException e) {
			System.err.format("Data cannot be written to the file: %s", file_to_write);
		}
	}
}
