package com.detect.plagiarism.external;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * XML functions class to write the features to the file
 * 
 * @author arunjayapal
 */
public class XMLfunctions{
	public StringBuilder sb = new StringBuilder("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
	public File xml;
	public String[] sus_src;
	
	public XMLfunctions(String xmlFile, String[] sus_src){
		this.sb = new StringBuilder();
		this.xml = new File(xmlFile);
		this.sus_src = sus_src;
	}
	
	public void beginXML(){
		sb.append("<document reference=\"" + sus_src[0] + "\">\n");
	}
	
	public void genFeaturesXML(int sus_offset, int src_offset, int sus_length, int src_length){
		
		sb.append("\t\t<feature name=\"detected-plagiarism\" source_length=\"" + src_length +
				"\" source_offset=\"" + src_offset + 
				"\" source_reference=\"" + sus_src[1] + 
				"\" this_length=\"" + sus_length + "\" this_offset=\"" + sus_offset + "\"/>\n");
	}
	
	public void endXML() throws IOException{
		sb.append("</document>");
		FileWriter fw = new FileWriter(xml);
		fw.write(sb.toString());
		fw.close();
	}
}