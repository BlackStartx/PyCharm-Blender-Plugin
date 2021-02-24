package util.core;

public class MyStringUtils {
    /**
     * Add a given text before and after a string.
     *
     * @param myString the string you want to surround.
     * @param surround the text you want to surround the string.
     * @return the source string surrounded by the surround string.
     */
    public static String surrounded(String myString, String surround) {
        return surround + myString + surround;
    }
}
