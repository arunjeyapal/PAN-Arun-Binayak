package com.candidate.retrieval;

import java.io.*;
import java.net.*;

/**
 * This small application takes an URL as argument, and
 * outputs the HTTP response
 * @author Arun Jayapal
 */
public class GetResponse  
{
	HttpURLConnection con;
	int reCode;
	String reMessage;
	String result = null;
	public GetResponse( URL theURL )
	{
		try
		{
			con = (HttpURLConnection)theURL.openConnection();
			reCode = con.getResponseCode();
			reMessage = con.getResponseMessage();
			System.out.println("HTTP response and message: " + reCode  + " - " + reMessage );
		}
		catch( IOException e )
		{
			System.err.println( "GetResponse.GetResponse - error opening or reading URL: " + e );
		}
	}

	public String returnResponse() throws IOException{
		// Get the response
		BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream()));
		StringBuffer sb = new StringBuffer();
		String line;
		while ((line = rd.readLine()) != null)
		{
			//System.out.println(line);
			sb.append("\n"+line);
		}
		rd.close();
		result = sb.toString();
		//System.out.println(result);
		return result;
	}

	public static void main( String args[] ) throws IOException
	{
		try
		{
			GetResponse gr = new GetResponse(new URL("http://webis15.medien.uni-weimar.de/chatnoir/clueweb?id=100022317882&token=525885a76c1a47d56987b1c87edbb2f9004"));
			System.out.println(gr.returnResponse());
			//Open file from disk instead
			//File f = new File( args[0] );
			//new GetResponse( f.toURL() );
		}
		catch (MalformedURLException e)
		{
			System.out.println("GetResponse.main - wrong url: " +e );
		}

	}
}
