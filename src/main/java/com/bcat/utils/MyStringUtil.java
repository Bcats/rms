package com.bcat.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class MyStringUtil {

    public static String getSubString(String text, String left, String right) {
        String result = "";
        int zLen;
        if (left == null || left.isEmpty()) {
            zLen = 0;
        } else {
            zLen = text.indexOf(left);
            if (zLen > -1) {
                zLen += left.length();
            } else {
                zLen = 0;
            }
        }
        int yLen = text.indexOf(right, zLen);
        if (yLen < 0 || right == null || right.isEmpty()) {
            yLen = text.length();
        }
        result = text.substring(zLen, yLen);
        return result;
    }

    public static List<String> getSubStrings(String text, String left, String right) {
        int xLen = 0, yLen = 0;
        List<String> strings = new ArrayList<>();
        boolean flag = true;
        while(flag){
            xLen = text.indexOf(left, yLen);
            if (xLen > -1) {
                xLen += left.length();
            } else {
                flag = false;
                continue;
            }

            yLen = text.indexOf(right, xLen);
            if (yLen <= -1) {
                flag = false;
                continue;
            }
            String result = text.substring(xLen, yLen);
            strings.add(result);
        }

        return strings;
    }

}
