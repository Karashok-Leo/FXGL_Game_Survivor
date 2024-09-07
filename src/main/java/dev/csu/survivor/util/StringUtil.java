package dev.csu.survivor.util;

import java.util.Locale;

public class StringUtil
{
    public static String lowercase(String str)
    {
        return str.toLowerCase(Locale.ROOT);
    }

    public static String uppercaseFirstLetter(String str)
    {
        return str.substring(0, 1).toUpperCase(Locale.ROOT)
               + str.substring(1).toLowerCase(Locale.ROOT);
    }
}
