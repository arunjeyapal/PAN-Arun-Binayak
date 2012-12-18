package com.candidate.retrieval.analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import com.tools.util.DocGram;
import com.tools.util.JSONdata;

/**
 * Class compares given two documents and computes the score.
 * 
 * Similarity(A,B) = intersect(S(A,n), S(B,n)) / min(S(A,n) S(B,n))
 * 
 * This is used for the PAN 2012 Plagiarism detection conference
 * to rank the suspicious documents based on the similarity metric
 * 
 * Reference: Retrieving Candidate Plagiarised Documents using Query Expansion
 * 		by Rao Muhammad Adeel Nawab , Mark Stevenson , and Paul Clough
 * 
 * @author arunjayapal
 *
 */
public class Comparefiles {

	public static HashSet<String> convertSet(List<String> data){
		return (new HashSet<String>(data));
	}

	public static HashSet<String> intersection(HashSet<String> setA, HashSet<String> setB) {
		HashSet<String> tmp = new HashSet<String>();
		//setA.retainAll(setB);
		for (String x : setA)
			if (setB.contains(x))
				tmp.add(x);
		return tmp;
	}

	public static double computeScore(int intersection, int sus_set, int cand_set){
		int tmp;
		if(sus_set < cand_set)
			tmp = sus_set;
		else
			tmp = cand_set;
		return (double)intersection/tmp;		
	}

	/**
	 * Returns the comparison score between the suspicious documents and the set of 
	 * candidate documents which are considered to be plagiarized. The documents are 
	 * input after pre-processing. Please refer to the other classes for more details.
	 * 
	 * @param susp_docs
	 * @param cand_docs
	 * @return scores of each candidate document with respect to the suspicious document
	 */
	public static HashMap<String,HashMap<String,Double>> getComparisonScore(ArrayList<DocGram> susp_docs, HashMap<String, ArrayList<JSONdata>> cand_docs){
		HashMap<String,HashMap<String,Double>> all_scores = new HashMap<String,HashMap<String,Double>>();
		for(DocGram docs:susp_docs){
			HashMap<String,Double> doc_scores = new HashMap<String,Double> ();
			String susp_doc_name = docs.docName;
			List<String> susp_nGrams = docs.nGrams;
			if(cand_docs.containsKey(susp_doc_name)){
				for(JSONdata items:cand_docs.get(susp_doc_name)){
					List<String> cand_longid = items.LongID;
					ArrayList<List<String>> cand_ngrams = items.candidatenNgrams;
					for(int i=0; i<cand_longid.size() && i<cand_ngrams.size(); i++){
						//System.out.println(cand_longid.get(i)+" : "+cand_ngrams.get(i));
						HashSet<String> sus_set = convertSet(susp_nGrams);
						HashSet<String> cand_set = convertSet(cand_ngrams.get(i));
						doc_scores.put(cand_longid.get(i), computeScore(intersection(sus_set, cand_set).size(),sus_set.size(),cand_set.size()));
						//System.out.println(cand_longid.get(i)+":"+computeScore(intersection(sus_set, cand_set).size(),sus_set.size(),cand_set.size()));
					}
				}
			}
			all_scores.put(susp_doc_name, doc_scores);
		}
		//System.out.println(all_scores);
		return all_scores;
	}
}
