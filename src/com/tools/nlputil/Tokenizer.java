package com.tools.nlputil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.InvalidFormatException;

public class Tokenizer {
	TokenizerME token;
	
	public Tokenizer() throws InvalidFormatException, FileNotFoundException, IOException{
		token = new TokenizerME(new TokenizerModel(new FileInputStream(new File("./model/en-token.bin"))));
	}
	
	public String[] tokenize(String text){
		String[] tokens = token.tokenize(text);
		return tokens;
	}
}
