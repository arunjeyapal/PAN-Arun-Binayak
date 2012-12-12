package com.detect.plagiarism.external;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

import weka.classifiers.Classifier;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

import com.detect.plagiarism.weka.Feature;
import com.detect.plagiarism.weka.GenerateARFF;
import com.tools.nlputil.gst.GreedyStringTiling;
import com.tools.nlputil.gst.PlagResult;

public class IdentifyPlagType {
	public final String model_path;
	public ObjectInputStream ois;
	public IdentifyPlagType(String model_path){
		this.model_path = model_path;
		try {
			this.ois = new ObjectInputStream(
					new FileInputStream(model_path));
		} catch (FileNotFoundException e) {
			System.err.println("File not found at "+model_path);
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Instances getARFF(String sus_file, String src_file, PlagResult results,
			String klass) throws IOException {
		FastVector atts;
		Instances trainingSet;

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
		//atts.addElement(new Attribute("Great100"));
		//atts.addElement(new Attribute("Less100great75"));
		atts.addElement(new Attribute("Less75great50"));
		atts.addElement(new Attribute("Less50great25"));
		atts.addElement(new Attribute("Less25great20"));
		atts.addElement(new Attribute("Less20great15"));
		atts.addElement(new Attribute("Less15great10"));
		atts.addElement(new Attribute("Less10great5"));
		atts.addElement(new Attribute("Less5"));
		 FastVector fvClassVal = new FastVector(6);
		 fvClassVal.addElement("no-plagiarism");
		 fvClassVal.addElement("no-obfuscation");
		 fvClassVal.addElement("artificial-low");
		 fvClassVal.addElement("artificial-high");
		 fvClassVal.addElement("translation");
		 fvClassVal.addElement("simulated-paraphrase");
		atts.addElement(new Attribute("Class",fvClassVal));
		
		trainingSet = new Instances("Plagiarism", atts, 17);
		trainingSet.setClassIndex(16);
		Instance iExample = new Instance(17);

		Feature feature = GenerateARFF.generateFeatures(sus_file, src_file,
				klass);

		iExample.setValue((Attribute)atts.elementAt(0), feature.susp_unigram_count);
		iExample.setValue((Attribute)atts.elementAt(1), feature.src_unigram_count);
		iExample.setValue((Attribute)atts.elementAt(2), feature.intersect_unigram_val);
		iExample.setValue((Attribute)atts.elementAt(3), feature.no_susp_ngrams);
		iExample.setValue((Attribute)atts.elementAt(4), feature.no_src_ngrams);
		iExample.setValue((Attribute)atts.elementAt(5), feature.no_uniq_susp_ngrams);
		iExample.setValue((Attribute)atts.elementAt(6), feature.no_uniq_src_ngrams);
		iExample.setValue((Attribute)atts.elementAt(7), feature.similarity_score);
		iExample.setValue((Attribute)atts.elementAt(8), feature.intersection_score);
		//iExample.setValue((Attribute)atts.elementAt(9), feature.great100);
		//iExample.setValue((Attribute)atts.elementAt(10), feature.less100great75);
		iExample.setValue((Attribute)atts.elementAt(9), feature.less75great50);
		iExample.setValue((Attribute)atts.elementAt(10), feature.less50great25);
		iExample.setValue((Attribute)atts.elementAt(11), feature.less25great20);
		iExample.setValue((Attribute)atts.elementAt(12), feature.less20great15);
		iExample.setValue((Attribute)atts.elementAt(13), feature.less15great10);
		iExample.setValue((Attribute)atts.elementAt(14), feature.less10great5);
		iExample.setValue((Attribute)atts.elementAt(15), feature.less5);
		iExample.setValue((Attribute)atts.elementAt(16), "no-obfuscation");
		
		trainingSet.add(iExample);

		return trainingSet;
	}

	public String setClassifier(String s_file, String r_file, PlagResult results) throws Exception {
		Classifier randomsubspace_classifier = (Classifier) ois.readObject();
		Instances unlabeled = getARFF(s_file, r_file, results, "unknown");
		unlabeled.setClassIndex(unlabeled.numAttributes() - 1);
		// create copy
		Instances labeled_instances = new Instances(unlabeled);

		// label instances
		for (int i = 0; i < unlabeled.numInstances(); i++) {
			double clsLabel = randomsubspace_classifier
					.classifyInstance(unlabeled.instance(i));
			labeled_instances.instance(i).setClassValue(clsLabel);
		}
		
		//return the label of the given instance
		return labeled_instances.instance(0).stringValue(16);
	}

	public static void main(String args[]) throws Exception {
		IdentifyPlagType idp = new IdentifyPlagType("./model/meta-Randomsubspace-10fold-moreAttr.model");
		String s_file = "/home/arunjayapal/Public/PANCompetition/docs/pan12-detailed-comparison-training-corpus/susp/suspicious-document00000.txt";
		String r_file = "/home/arunjayapal/Public/PANCompetition/docs/pan12-detailed-comparison-training-corpus/src/source-document00000.txt";
		String sus_text = DetectPlagiarism.readFileAsString(new File(s_file));
		String src_text = DetectPlagiarism.readFileAsString(new File(r_file));
		GreedyStringTiling gst = new GreedyStringTiling();
		@SuppressWarnings("static-access")
		PlagResult results = gst.run(sus_text, src_text,
				3, (float) 0.3);
		//Instance klass = setClassifier(s_file, r_file, model_path).instance(0);
		System.out.println(idp.setClassifier(s_file, r_file, results));
	}
}
