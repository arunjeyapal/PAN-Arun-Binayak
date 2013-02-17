package com.tools.nlputil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.util.InvalidFormatException;

/**
 * This piece of code uses the OpenNLP tool for loading the 
 * Part Of Speech tagging model trained on the OpenNLP's corpus 
 * and tag the tokens
 * 
 * @author arunjayapal
 */

public class POStagger {
	
	POSTaggerME posTagger;
	
	/**
	 * Constructor to load the model from the default location
	 * 
	 * @throws InvalidFormatException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public POStagger() throws InvalidFormatException, FileNotFoundException, IOException{
		posTagger = new POSTaggerME(new POSModel(new FileInputStream(new File("./model/en-pos-maxent.bin"))));
	}
	
	/**
	 * Method to tag the tokens based on the model
	 * 
	 * @param tokens
	 * @return
	 */
	
	public String[] tag(String[] tokens){
		String tags[] = posTagger.tag(tokens);
		return tags;
	}
}
