package com.detect.plagiarism.external;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import com.tools.nlputil.gst.GreedyStringTiling;
import com.tools.nlputil.gst.MatchVals;
import com.tools.nlputil.gst.PlagResult;
import com.tools.util.CharOffset;

/**
 * Class to test the String offset data
 * 
 * @author arunjayapal
 */
public class getStringOffsetData {

	public static void main(String s[]) throws StringIndexOutOfBoundsException, InvocationTargetException{
		String susText = "with Hash table entries Hash table entries " +
				"has Arun name is here, Arun name is here with Hash table entries Arun how is arun";
		String plagText = "Hash table entries has Arun name is here, " +
				"Arun name is here with Hash table entries Arun how is arun Arun name is here " +
				"with Hash table entries"; 
		PlagResult result = GreedyStringTiling.run(susText,plagText,2,(float)0.5);
		ArrayList<MatchVals> matches = result.tiles;
		//System.out.println(getStringData(plagText, matches, true));
		String text;
		for(MatchVals tiles:matches){
			System.out.println(tiles.patternPostion+":"+tiles.textPosition+":"+tiles.length);
			text = CharOffset.getOffsetStringValue(susText, tiles.patternPostion, tiles.length, true);
			int offset = CharOffset.getWordCharLength(susText, tiles.patternPostion, tiles.length);
			System.out.println(text+":"+offset);
			text = CharOffset.getOffsetStringValue(plagText, tiles.textPosition, tiles.length, true);
			System.out.println(text);
		}
	}
}
