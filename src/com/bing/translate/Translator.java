package com.bing.translate;

import java.nio.charset.Charset;

import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;

public class Translator {
	private final Charset UTF8_CHARSET = Charset.forName("UTF-8");
	public String decodeUTF8(byte[] bytes) {
		return new String(bytes, UTF8_CHARSET);
	}

	public byte[] encodeUTF8(String string) {
		return string.getBytes(UTF8_CHARSET);
	}

	public static String translate(String textToTranslate, Language srcLanguage) throws Exception {
		// Set your Windows Azure Marketplace client info - See http://msdn.microsoft.com/en-us/library/hh454950.aspx
		Translate.setClientId("arunjayapal");
		Translate.setClientSecret("ZUfM6BTFC2Hq/nfsu7qNWMwGccNqS2kTPm5+Ie6xeL4=");

		String translatedText = Translate.execute(textToTranslate, srcLanguage, Language.ENGLISH);
		System.out.println(translatedText);
		return translatedText;
	}

	public static String[] translate(String[] sourceText, Language srcLanguage) throws Exception {
		// Set the Client ID / Client Secret once per JVM. It is set statically and applies to all services
		Translate.setClientId("arunjayapal");
		Translate.setClientSecret("ZUfM6BTFC2Hq/nfsu7qNWMwGccNqS2kTPm5+Ie6xeL4=");

		//Create your array of texts to be translated
		// NOTE: The source language of all texts must be the same. For instance, I cannot translate
		// a Spanish string, French string, and English string into German during a single call
//		String[] sourceTexts = 
//			{
//				"»Zum psychischen Mechanismus der Vergesslichkeit« einen kleinen Aufsatz veröffentlicht, dessen"
//			};


		// Call the translate.execute method, passing an array of source texts
		String[] translatedTexts = Translate.execute(sourceText, Language.GERMAN, Language.ENGLISH);

		//Print the results!
//		for(String text : translatedTexts) {
//			System.out.println(text);
//		}
		return translatedTexts;
	}

	public static void main(String sp[]) throws Exception{

		//translate("﻿Wie soll ich das verstehen", DetectLanguage.identify("﻿Frau von Kannawurf erhob sich"));
		String[] newS = new String[1];
		newS[0] = "﻿Zum psychischen Mechanismus der Vergesslichkeit« einen kleinen Aufsatz veröffentlicht, dessen";
		translate(newS, DetectLanguage.identify("﻿Zum psychischen Mechanismus der Vergesslichkeit« einen kleinen Aufsatz veröffentlicht, dessen"));
	}
}