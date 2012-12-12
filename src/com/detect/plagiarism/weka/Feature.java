package com.detect.plagiarism.weka;

public class Feature {
	public final int susp_unigram_count;
	public final int src_unigram_count;
	public final int intersect_unigram_val;
	public final int no_susp_ngrams;
	public final int no_uniq_susp_ngrams;
	public final int no_src_ngrams;
	public final int no_uniq_src_ngrams;
	public final double similarity_score;
	public final int intersection_score;
	public final int great100;
	public final int less100great75;
	public final int less75great50;
	public final int less50great25;
	public final int less25great20;
	public final int less20great15;
	public final int less15great10;
	public final int less10great5;
	public final int less5;
	public final String klass;
	public Feature(int susp_unigram_count, int src_unigram_count,
			int intersect_unigram_val, int no_susp_ngrams, int no_src_ngrams,
			int no_uniq_susp_ngrams, int no_uniq_src_ngrams,
			double similarity_score, int intersection_score, int great100, int less100great75, int less75great50, int less50great25, int less25great20,
			int less20great15, int less15great10, int less10great5, int less5, String klass) {
		this.susp_unigram_count = susp_unigram_count;
		this.src_unigram_count = src_unigram_count;
		this.intersect_unigram_val = intersect_unigram_val;
		this.no_susp_ngrams = no_susp_ngrams;
		this.no_src_ngrams = no_src_ngrams;
		this.no_uniq_susp_ngrams = no_uniq_susp_ngrams;
		this.no_uniq_src_ngrams = no_uniq_src_ngrams;
		this.similarity_score = similarity_score;
		this.intersection_score = intersection_score;
		this.great100 = great100;
		this.less100great75 = less100great75;
		this.less75great50 = less75great50;
		this.less50great25 = less50great25;
		this.less25great20 = less25great20;
		this.less20great15 = less20great15;
		this.less15great10 = less15great10;
		this.less10great5 = less10great5;
		this.less5 = less5;
		this.klass = klass;
	}
}
