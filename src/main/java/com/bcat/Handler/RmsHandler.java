package com.bcat.Handler;

import okhttp3.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RmsHandler {

    private final static String HEADER =
                    "Host: www.rms110.com\n" +
                    "Connection: keep-alive\n" +
                    "Upgrade-Insecure-Requests: 1\n" +
                    "User-Agent: Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.103 Safari/537.36\n" +
                    "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3\n" +
                    "Accept-Language: zh-CN,zh;q=0.9\n";

    public List<List<String>> getData(String cookie){
        try {
            OkHttpClient client = new OkHttpClient();
            String head = HEADER + "Cookie :" + cookie;
            String[] heads = head.split(":|\n");
            Headers headers = Headers.of(heads);

            Request request = new Request.Builder()
                    .get()
                    .url("http://www.rms110.com/high-level-alarm/high-level-alarm")
                    .headers(headers)
                    .build();
            Call call = client.newCall(request);

            Response response = call.execute();
            String result = response.body().string();

            return parseStr(result);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private List<List<String>> parseStr(String str){
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


    public String getAlarmUrlById(String id, String cookie){
        OkHttpClient client = new OkHttpClient();
        String head = HEADER + "Cookie :" +  cookie;
        String url = null;

        try {
            String[] heads = head.split(":|\n");
            Headers headers = Headers.of(heads);

            Request request = new Request.Builder()
                    .get()
                    .url("http://www.rms110.com/high-level-alarm/edit?taskSendId=" + id)
                    .headers(headers)
                    .build();
            Call call = client.newCall(request);
            Response response = call.execute();
            String result = response.body().string();
            Document document = Jsoup.parse(result);
            Elements elements = document.getElementsByAttribute("target");
            Element element = elements.get(0);
            url = element.attr("href");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return url;
    }


}
