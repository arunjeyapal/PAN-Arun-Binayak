package com.tools.util;

import java.text.Normalizer;

public class NormalizeWords
{
    public static String removeDiacritics(String input)
    {
        String nrml = Normalizer.normalize(input, Normalizer.Form.NFD);
        StringBuilder stripped = new StringBuilder();
        for (int i=0;i<nrml.length();++i)
        {
            if (Character.getType(nrml.charAt(i)) != Character.NON_SPACING_MARK)
            {
                stripped.append(nrml.charAt(i));
            }
        }
        return stripped.toString();
    }
}
