package com.tools.nlputil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.util.InvalidFormatException;

public class SpanishPOStagger {
	POSTaggerME posTagger;
	
	/**
	 * Constructor to load the model from the default location
	 * 
	 * @throws InvalidFormatException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public SpanishPOStagger() throws InvalidFormatException, FileNotFoundException, IOException{
		posTagger = new POSTaggerME(new POSModel(new FileInputStream(new File("./model/opennlp-es-pos-maxent-pos-es.model"))));
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
	
	public static void main(String s[]) throws InvalidFormatException, FileNotFoundException, IOException{
		SpanishPOStagger spt = new SpanishPOStagger();
		Tokenizer tk = new Tokenizer();
		String[] tags = spt.tag(tk.tokenize("Pregúntale a María que se compra todas las revistas del corazón."));
		String[] words = tk.tokenize("Pregúntale a María que se compra todas las revistas del corazón.");
		for (int i=0; i<words.length; i++){
			System.out.println(words[i]+"/"+tags[i]);
		}
	}
}