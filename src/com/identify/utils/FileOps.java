package com.identify.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 * Class to create and delete file directories
 * 
 * @author arunjayapal
 * 
 */
public class FileOps {

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
	 * Method to create a Directory
	 * 
	 * @param file
	 * @return Status of the CreateDirectory
	 */
	public static boolean createDirectory(File file) {
		// Delete Directory if alreday exists
		if (file.exists()) {
			System.out
					.print("\nDirectory already exists \n \t"
							+"Do you want to delete the diretory (y/n):");
			BufferedReader in = new BufferedReader(new InputStreamReader(
					System.in));
			try {
				String val = in.readLine();
				if (val.toLowerCase().equals("y")
						|| val.toLowerCase().equals("yes")) {
					deleteDirectory(file);
				} else {
					return true;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		boolean status = file.mkdirs();
		if (status)
			System.out.println(" Successfull creating Directory "
					+ file.getPath());
		return status;
	}

	/**
	 * Method to delete a directory
	 * 
	 * @param dir
	 * @return Status of the DeleteDirectory
	 */
	public static boolean deleteDirectory(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				File delFile = new File(dir, children[i]);
				if (!delFile.exists()) {
					System.out.println("Cannot find directory to delete"
							+ delFile.getPath());
					return false;
				}
				boolean success = deleteDirectory(delFile);
				System.out.println(delFile + ": success? " + success);
				if (!success) {
					System.out.println("failure during delete directory"
							+ delFile.getPath());
					return false;
				}
			}
			// The directory is now empty so now it can be smoked
		}
		return dir.delete();
	}
}
