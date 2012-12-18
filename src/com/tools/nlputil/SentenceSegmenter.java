package com.tools.nlputil;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.util.InvalidFormatException;

public class SentenceSegmenter {
	SentenceDetectorME sentence;
	
	public SentenceSegmenter() throws InvalidFormatException, FileNotFoundException, IOException{
		InputStream modelIn = new FileInputStream("./model/en-sent.bin");
		SentenceModel model = null;
		try {
			  model = new SentenceModel(modelIn);
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
		sentence = new SentenceDetectorME(model);
	}
	
	public String[] segmenter(String text){
		String[] tokens = sentence.sentDetect(text);
		return tokens;
	}
}
