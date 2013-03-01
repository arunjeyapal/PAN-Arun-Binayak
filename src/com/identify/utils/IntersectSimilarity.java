package com.identify.utils;

import java.util.HashSet;
import java.util.Set;

/**
 * 
 * Intersect Similarity class gets the similarity value based on the unique
 * number of ngrams identified from known set and unknown set
 * 
 * @author arunjayapal
 * 
 */
public class IntersectSimilarity {

	public static Set<String> intersection(Set<String> setA, Set<String> setB) {
		Set<String> tmp = new HashSet<String>();
		for (String x : setA)
			if (setB.contains(x))
				tmp.add(x);
		return tmp;
	}

	public static double computeScore(int intersection, int known_set,
			int unknown_set) {
		int tmp;
		if (known_set < unknown_set)
			tmp = known_set;
		else
			tmp = unknown_set;
		return (double) intersection / tmp;
	}

	/**
	 * Method to get the comparison score between the known set of ngrams and
	 * unknown set of ngrams
	 * 
	 * @param known_set
	 * @param unknown_set
	 * @return
	 */
	public static double getComparisonscore(Set<String> known_set,
			Set<String> unknown_set) {
		return computeScore(intersection(known_set, unknown_set).size(),
				known_set.size(), unknown_set.size());
	}
}