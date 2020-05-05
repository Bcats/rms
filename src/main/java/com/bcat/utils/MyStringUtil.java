package com.bcat.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class MyStringUtil {

    public static List<List<String>> parseStr(String str){
        Document document = Jsoup.parse(str);
        Elements mytbElements = document.getElementsByClass("myTableBox");
        Element mytbElement = mytbElements.get(0);

        Elements tbElements = mytbElement.getElementsByTag("tbody");
        Element tbElement = tbElements.get(0);

        Elements trElements = tbElement.getElementsByTag("tr");
        List<List<String>> dataArrays = new ArrayList<List<String>>();
        for (Element trElement : trElements){
            Elements tdElements = trElement.getElementsByTag("td");
            List<String> dataArray = new ArrayList<String>();
            for (Element tdElement : tdElements){
                dataArray.add(tdElement.text());
            }
            dataArrays.add(dataArray);
        }

//        Map<String,String> map = new HashMap<String, String>();

        return dataArrays;
    }

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

}
