package com.detect.plagiarism.weka;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;

import com.candidate.retrieval.analysis.Comparefiles;
import com.detect.plagiarism.external.DetectPlagiarism;
import com.tools.nlputil.NGramSplitter;
import com.tools.nlputil.gst.GreedyStringTiling;
import com.tools.nlputil.gst.MatchVals;
import com.tools.nlputil.gst.PlagResult;

public class GenerateARFF {

	public static Feature generateFeatures(String susp_file, String src_file,
			String klass) throws IOException {
		String susp_data = DetectPlagiarism.readFileAsString(
				new File(susp_file)).replaceAll("\\W", " ");
		String src_data = DetectPlagiarism.readFileAsString(new File(src_file))
				.replaceAll("\\W", " ");

		List<String> susp_unigrams = Arrays.asList(susp_data
				.split("[\\s+|\\W+]"));
		List<String> src_unigrams = Arrays
				.asList(src_data.split("[\\s+|\\W+]"));

		int intersect_unigram_val = Comparefiles.intersection(
				Comparefiles.convertSet(susp_unigrams),
				Comparefiles.convertSet(src_unigrams)).size();

		List<String> susp_ngrams = NGramSplitter.ngrams(6, susp_data);
		List<String> src_ngrams = NGramSplitter.ngrams(6, src_data);

		HashSet<String> susp_set = Comparefiles.convertSet(susp_ngrams);
		HashSet<String> src_set = Comparefiles.convertSet(src_ngrams);

		double sim = Comparefiles.computeScore(
				Comparefiles.intersection(susp_set, src_set).size(),
				susp_set.size(), src_set.size());
		GreedyStringTiling gst = new GreedyStringTiling();
		@SuppressWarnings("static-access")
		PlagResult results = gst.run( DetectPlagiarism.readFileAsString(
				new File(susp_file)), DetectPlagiarism.readFileAsString(new File(src_file)), 3,
				(float) 0.3);
		int great100 = 0, less100great75 = 0, less75great50 = 0, less50great25 = 0, less25great20 = 0, less20great15 = 0, less15great10 = 0, less10great5 = 0, less5 = 0;
		System.out.println(results.getTiles().size());
		for (MatchVals tiles : results.getTiles()) {
			if (tiles.length >= 100)
				great100++;
			else if (tiles.length < 100 && tiles.length > 75)
				less100great75++;
			else if (tiles.length <= 75 && tiles.length > 50)
				less75great50++;
			else if (tiles.length <= 50 && tiles.length > 25)
				less50great25++;
			else if (tiles.length <= 25 && tiles.length > 20)
				less25great20++;
			else if (tiles.length <= 20 && tiles.length > 15)
				less20great15++;
			else if (tiles.length <= 15 && tiles.length > 10)
				less15great10++;
			else if (tiles.length <= 10 && tiles.length > 5)
				less10great5++;
			else
				less5++;
		}

		return (new Feature(susp_unigrams.size(), src_unigrams.size(),
				intersect_unigram_val, susp_ngrams.size(), src_ngrams.size(),
				susp_set.size(), src_set.size(), sim, Comparefiles
						.intersection(susp_set, src_set).size(), great100,
				less100great75, less75great50, less50great25, less25great20,
				less20great15, less15great10, less10great5, less5, klass));
	}

	public static void generateARFF(String pairs, String klass)
			throws IOException {
		File pair = new File(pairs);
		Scanner scan = new Scanner(pair);

		FastVector atts;
		FastVector attVals;
		Instances data;
		int i;

		atts = new FastVector();
		atts.addElement(new Attribute("Susp_Unigram_count"));
		atts.addElement(new Attribute("Src_Unigram_count"));
		atts.addElement(new Attribute("Intersect_Unigram_count"));
		atts.addElement(new Attribute("Susp_Ngram_size"));
		atts.addElement(new Attribute("Source_Ngram_size"));
		atts.addElement(new Attribute("Unique_Susp_ngrams"));
		atts.addElement(new Attribute("Unique_Src_ngrams"));
		atts.addElement(new Attribute("Similarity_value"));
		atts.addElement(new Attribute("Intersection_number"));
		atts.addElement(new Attribute("Great100"));
		atts.addElement(new Attribute("Less100great75"));
		atts.addElement(new Attribute("Less75great50"));
		atts.addElement(new Attribute("Less50great25"));
		atts.addElement(new Attribute("Less25great20"));
		atts.addElement(new Attribute("Less20great15"));
		atts.addElement(new Attribute("Less15great10"));
		atts.addElement(new Attribute("Less10great5"));
		atts.addElement(new Attribute("Less5"));
		attVals = new FastVector();
		for (i = 1; i <= 6; i++) {
			attVals.addElement("plag" + (i));
		}
		atts.addElement(new Attribute("class", attVals));

		data = new Instances("Plagiarism", atts, 0);
		while (scan.hasNext()) {
			double[] vals = new double[18];

			String[] sus_src = scan.nextLine().split("\\s+");
			String sus_file = pair.getParent() + "/../susp/" + sus_src[0];
			String src_file = pair.getParent() + "/../src/" + sus_src[1];
			Feature feature = generateFeatures(sus_file, src_file, klass);

			/**
			 * Weka feature generation starts here
			 */
			vals[0] = feature.susp_unigram_count;
			vals[1] = feature.src_unigram_count;
			vals[2] = feature.intersect_unigram_val;
			vals[3] = feature.no_susp_ngrams;
			vals[4] = feature.no_src_ngrams;
			vals[5] = feature.no_uniq_susp_ngrams;
			vals[6] = feature.no_uniq_src_ngrams;
			vals[7] = feature.similarity_score;
			vals[8] = feature.intersection_score;
			vals[9] = feature.great100;
			vals[10] = feature.less100great75;
			vals[11] = feature.less75great50;
			vals[12] = feature.less50great25;
			vals[13] = feature.less25great20;
			vals[14] = feature.less20great15;
			vals[15] = feature.less15great10;
			vals[16] = feature.less10great5;
			vals[17] = feature.less5;
			data.add(new Instance(1.0, vals));
			System.out.println(data);
		}
		ArffSaver saver = new ArffSaver();
		saver.setInstances(data);
		saver.setFile(new File("../test.arff"));
		saver.setDestination(new File("../test.arff"));
		saver.writeBatch();
		System.out.println("Data written..");
	}

	public static void main(String args[]) throws IOException {
		generateARFF(
				"/home/arunjayapal/Public/PANCompetition/docs/pan12-detailed-comparison-training-corpus/06_simulated_paraphrase/pairs",
				"no_plagiarism");
	}
}