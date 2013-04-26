package com.identify.analyzeresults;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Compute Precision and recall for Author Idenfication
 * 
 * @author arunjayapal
 * 
 */
public class PrecisionRecall {

	public File goldstdfile;
	public File resultsfile;
	public int TP, FP, TN, FN, miss;
	public double precision, recall, fmeasure;
	public HashMap<String, String> goldstd;

	public PrecisionRecall(String goldstdfile, String resultsfile) {
		this.TP = 0;
		this.FP = 0;
		this.TN = 0;
		this.FN = 0;
		this.miss = 0;
		this.precision = 0.0;
		this.recall = 0.0;
		this.fmeasure = 0.0;
		this.goldstdfile = new File(goldstdfile);
		this.resultsfile = new File(resultsfile);
		this.goldstd = new HashMap<String, String>();
		Scanner sc = null;
		try {
			sc = new Scanner(this.goldstdfile);
		} catch (FileNotFoundException e) {
			System.err.format("File not found at: %s", goldstdfile);
		}
		while (sc.hasNextLine()) {
			String s[] = sc.nextLine().split(" ");
			String fn = s[0].trim(), res = s[1].trim();
			this.goldstd.put(fn, res);
		}
	}

	/**
	 * Get the positive and negative values
	 */
	public void computePosNeg() {
		Scanner sc = null;
		try {
			sc = new Scanner(this.resultsfile);
		} catch (FileNotFoundException e) {
			System.err.format("File not found at: %s",
					this.resultsfile.toString());
		}
		while (sc.hasNextLine()) {
			String s[] = sc.nextLine().split(" ");
			String fn = s[0], res = s[1];
			if (this.goldstd.containsKey(fn)) {
				if (res.trim().equals("Y")
						&& this.goldstd.get(fn).trim().equals("Y"))
					++TP;
				else if (res.trim().equals("N")
						&& this.goldstd.get(fn).trim().equals("N"))
					++TN;
				else if (res.trim().equals("Y")
						&& this.goldstd.get(fn).trim().equals("N"))
					++FP;
				else if (res.trim().equals("N")
						&& this.goldstd.get(fn).trim().equals("Y"))
					++FN;
			} else {
				miss++;
			}
		}
	}

	public void computePrecision() {
		precision = TP * 1.0 / (TP + FP);
	}

	public void computeRecall() {
		recall = TP * 1.0 / (TP + FN);
	}

	public void computeFmeasure() {
		fmeasure = 2.0 * (precision * recall) / (precision + recall);
	}

	public double getPrecision() {
		return precision;
	}

	public double getRecall() {
		return recall;
	}

	public double getFmeasure() {
		return fmeasure;
	}

	public static void main(String args[]) {
		if (args.length == 2) {
			PrecisionRecall pr = new PrecisionRecall(args[0], args[1]);
			pr.computePosNeg();
			pr.computePrecision();
			pr.computeRecall();
			pr.computeFmeasure();
			System.out.format("Precision: %.4f\nRecall: %.4f\nFmeasure: %.4f",
					pr.getPrecision(), pr.getRecall(), pr.getFmeasure());
			System.out.println("\nTP:"+ pr.TP);
			System.out.println("TN:"+ pr.TN);
			System.out.println("FP:"+ pr.FP);
			System.out.println("FN:"+ pr.FN);
		}
	}
}