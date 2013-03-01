package com.identify.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Computes cosine similarity between two documents
 * 
 * @author arunjayapal
 * 
 */
public class CosineSimilarity {

	public HashMap<String, Integer> doc_tf;
	public HashMap<String, Integer> query_tf;
	public Set<String> doc_set;
	public Set<String> query_set;
	public HashSet<String> all_tokens = new HashSet<String>();

	public CosineSimilarity(HashMap<String, Integer> map1,
			HashMap<String, Integer> map2) {
		doc_tf = map1;
		query_tf = map2;
		doc_set = map1.keySet();
		query_set = map2.keySet();
		for (String tokens : doc_set)
			all_tokens.add(tokens);
		for (String tokens : query_set) {
			if (all_tokens.contains(tokens))
				continue;
			else
				all_tokens.add(tokens);
		}
	}

	public Double computeSum(ArrayList<Double> list) {
		Double sum = 0.0;
		for (Double elements : list)
			sum = sum + elements;
		return sum;
	}

	public double computeSimilarity() {
		ArrayList<Double> numerator = new ArrayList<Double>();
		ArrayList<Double> denominator_query = new ArrayList<Double>();
		ArrayList<Double> denominator_doc = new ArrayList<Double>();
		for (String tokens : all_tokens) {
			int tf, df, total_docs = 2;
			double idf, tf_idf_doc, tf_idf_query;
			if (doc_set.contains(tokens) && query_set.contains(tokens)) {
				df = 2;
				tf = doc_tf.get(tokens);
				idf = Math.log10(total_docs / df + 1);
				tf_idf_doc = tf * idf;
				tf = query_tf.get(tokens);
				idf = Math.log10(total_docs / df + 1);
				tf_idf_query = tf * idf;
			} else if (doc_set.contains(tokens)) {
				df = 1;
				tf = doc_tf.get(tokens);
				idf = Math.log10(total_docs / df);
				tf_idf_doc = tf * idf;
				tf_idf_query = 0.0;
			} else {
				df = 1;
				tf = query_tf.get(tokens);
				idf = Math.log10(total_docs / df);
				tf_idf_doc = 0.0;
				tf_idf_query = tf * idf;
			}
			numerator.add(tf_idf_doc * tf_idf_query);
			denominator_doc.add(tf_idf_doc * tf_idf_doc);
			denominator_query.add(tf_idf_query * tf_idf_query);
		}
		double Similarity_value = computeSum(numerator)
				/ (computeSum(denominator_doc) * computeSum(denominator_query));
		return Similarity_value * (10 ^ 5);
	}
}
