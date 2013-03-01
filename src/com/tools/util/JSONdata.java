package com.tools.util;

import java.util.ArrayList;
import java.util.List;
/**
 * To store the retrieved JSON data from the JSON files
 * 
 * @author arunjayapal
 *
 */
public class JSONdata {
	public final List<String> WarcTargetURI;
	public final List<String> BaseURL;
	public final List<String> HTMLTitle;
	public final List<Double> Pagerank;
	public final List<Long> Spamrank;
	public final List<Long> NumSentences;
	public final List<Double> Weight;
	public final List<Double> NormalizedPagerank;
	public final List<Long> NumWords;
	public final List<Double> Readability;
	public final List<Double> Proximity;
	public final List<String> WarcTrecID;
	public final List<String> LongID;
	public final List<Long> NumSyllables;
	public final List<Long> NumCharacters;
	public final ArrayList<List<String>> candidatenNgrams;
	public JSONdata(List<String> WarcTargetURI, List<String> BaseURL, List<String> HTMLTitle, 
			List<Double> Pagerank, List<Long> Spamrank, List<Long> NumSentences, List<Double> Weight,
			List<Double> NormalizedPagerank, List<Long> NumWords, List<Double> Readability, 
			List<Double> Proximity, List<String> WarcTrecID, List<String> LongID, 
			List<Long> NumSyllables, List<Long> NumCharacters, 
			ArrayList<List<String>> candidatenNgrams){
		
		this.BaseURL = BaseURL;
		this.candidatenNgrams = candidatenNgrams;
		this.HTMLTitle = HTMLTitle;
		this.LongID = LongID;
		this.NormalizedPagerank = NormalizedPagerank;
		this.NumCharacters = NumCharacters;
		this.NumSentences = NumSentences;
		this.NumSyllables = NumSyllables;
		this.NumWords = NumWords;
		this.Pagerank = Pagerank;
		this.Proximity = Proximity;
		this.Readability = Readability;
		this.Spamrank = Spamrank;
		this.WarcTargetURI =WarcTargetURI;
		this.WarcTrecID = WarcTrecID;
		this.Weight = Weight;
	}
}
