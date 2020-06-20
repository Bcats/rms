package com.bcat.utils;

import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyStringUtil {
    /**
     * 取文本中间内容
     * @return 返回中间内容
     **/

    public static String getSubString(String text, String left, String right) {
        int zLen;
        if (left.isEmpty()) {
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
        if (yLen < 0 || right.isEmpty())  yLen = text.length();

        return text.substring(zLen, yLen);
    }

    public static List<String> getSubStrings(String text, String left, String right) {
        int xLen, yLen = 0;
        List<String> strings = new ArrayList<>();

        while(true){
            xLen = text.indexOf(left, yLen);
            if (xLen > -1) xLen += left.length();
            else break;

            yLen = text.indexOf(right, xLen);
            if (yLen <= -1) break;

            String result = text.substring(xLen, yLen);
            strings.add(result);
        }
        return strings;
    }

    public static Map<String,String> mapStringToMap(String str){
        str = str.substring(1, str.length()-1);
        String[] strs = str.split(",");
        Map<String,String> map = new HashMap<String, String>();
        for (String string : strs) {
            if (string.split("=").length == 1){
                String key = string.split("=")[0].trim();
                map.put(key, "");
                continue;
            }
            String key = string.split("=",2)[0].trim();
            String value = string.split("=",2)[1].trim();
            map.put(key, value);
        }
        return map;
    }

}
