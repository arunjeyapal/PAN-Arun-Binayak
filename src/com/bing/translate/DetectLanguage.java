package com.bing.translate;

import com.memetix.mst.detect.Detect;
import com.memetix.mst.language.Language;

/**
 * DetectLanguageExample
 * 
 * Calls Microsoft to determine origin language of provided text.
 * 
 * Shows how to turn the two character response code into a language and how to localize the Language name
 *
 * @author griggs.jonathan
 * @date Jun 1, 2011
 * @since 0.3 June 1, 2011
 */
public class DetectLanguage {
    public static Language identify(String strval) throws Exception
    {
        // Set the Client ID / Client Secret once per JVM. It is set statically and applies to all services
        Detect.setClientId("arunjayapal");
        Detect.setClientSecret("ZUfM6BTFC2Hq/nfsu7qNWMwGccNqS2kTPm5+Ie6xeL4=");
        //Detect returns a Language Enum representing the language code
        Language detectedLanguage = Detect.execute(strval);
        
        // Prints out the language code
        System.out.println(detectedLanguage.getName(Language.ENGLISH));
        return detectedLanguage;
        //return detectedLanguage.getName(Language.ENGLISH);
    }
    
    public static void main(String s[]) throws Exception{
    	identify("﻿Frau von Kannawurf erhob sich");
    }
}
