package com.tools.util;

import java.io.File;
import java.util.ArrayList;

/**
 * File utility class provides getFileList method to 
 * retrieve the list of files from a given directory 
 * of any required format
 * 
 * @author arunjayapal
 *
 */
public class FileUtil {

	/**
	 * This static method is used to retrieve filelist 
	 * from a given directory of the desired file format
	 * 
	 * @param myDirectoryPath
	 * @param fileformat e.g., html or txt or json
	 * @return filelist in ArrayList<File> format
	 */
	public static ArrayList<File> getFilelist(String myDirectoryPath, String fileformat){ 
		File dir = new File(myDirectoryPath);
		ArrayList<File> filelist= new ArrayList<File>();
		for (File child : dir.listFiles()) {
			if(child.toString().contains("."+fileformat.toLowerCase()))
				filelist.add(child);
		}
		return filelist;
	}
	
	public static void main(String args[]){
		getFilelist("../PANCompetition/docs/pan12-plagiarism-candidate-retrieval-training-corpus-2012-03-31/","html");
	}
}
