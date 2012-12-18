package com.tools.nlputil;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

public class SentenceDetector {
	public static String[] splitSentence(String modelPath, String Passage) throws FileNotFoundException{
		InputStream modelIn = new FileInputStream(modelPath);
		String sentences[] = new String[1000];

		try {
			SentenceModel model = new SentenceModel(modelIn);
			SentenceDetectorME sentenceDetector = new SentenceDetectorME(model);
			sentences = sentenceDetector.sentDetect(Passage);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if (modelIn != null) {
				try {
					modelIn.close();
				}
				catch (IOException e) {
				}
			}
		}
		return sentences;
	}
}
