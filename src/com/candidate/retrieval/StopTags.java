package com.candidate.retrieval;

import java.util.HashSet;

public class StopTags {
	
	protected HashSet<String> stoptags = new HashSet<String>();
	
	public StopTags(){
		stoptags.add("NNP");
		stoptags.add("NN");
		stoptags.add("NNS");
//		stoptags.add("VB");
//		stoptags.add("VBD");
//		stoptags.add("VBG");
//		stoptags.add("VBN");
//		stoptags.add("JJ");
	}
	
	public boolean containTags(String tag){
		if(stoptags.contains(tag))
			return true;
		else
			return false;
	}

}
