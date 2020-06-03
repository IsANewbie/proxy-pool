package com.vtech.newscrawler.util;

/**
 *
 */
public class URlPrase {
    public static String getEncodeURI(String url){
        url = url.replaceAll("#","%23");
        return url;
    }
}
