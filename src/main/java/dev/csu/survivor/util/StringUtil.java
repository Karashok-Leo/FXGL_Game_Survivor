package dev.csu.survivor.util;

import java.util.Locale;

public class StringUtil
{
    /**
     * 将字符串全部转换为小写
     */
    public static String lowercase(String str)
    {
        return str.toLowerCase(Locale.ROOT);
    }

    /**
     * 将字符串首字母大写，其余字母全部转换为小写
     */
    public static String uppercaseFirstLetter(String str)
    {
        return str.substring(0, 1).toUpperCase(Locale.ROOT)
               + str.substring(1).toLowerCase(Locale.ROOT);
    }
}
