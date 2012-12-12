package com.tools.util;

import java.util.List;

/**
 * Class to store the document name and its respective
 * n-grams
 * 
 * @author arunjayapal
 *
 */
public class DocGram {
	public final String docName;
	public final List<String> nGrams;
	public DocGram(String documentName, List<String> ngrams){
		this.docName = documentName;
		this.nGrams = ngrams;
	}
}
