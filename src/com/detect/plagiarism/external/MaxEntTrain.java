package com.detect.plagiarism.external;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collections;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.NameSample;
import opennlp.tools.namefind.NameSampleDataStream;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;

public class MaxEntTrain {
	@SuppressWarnings("deprecation")
	public static void main(String args[]) throws IOException{
		Charset charset = Charset.forName("UTF-8");
		ObjectStream<String> lineStream =
				new PlainTextByLineStream(new FileInputStream("filelist.train"), charset);
		ObjectStream<NameSample> sampleStream = new NameSampleDataStream(lineStream);

		TokenNameFinderModel model;

		try {
			model = NameFinderME.train("en", "plag_text", sampleStream,
					Collections.<String, Object>emptyMap(), 100, 5);
		}
		finally {
			sampleStream.close();
		}

		BufferedOutputStream modelOut = null;
		try {
			modelOut = new BufferedOutputStream(new FileOutputStream(""));
			model.serialize(modelOut);
		} finally {
			if (modelOut != null) 
				modelOut.close();      
		}
	}
}
