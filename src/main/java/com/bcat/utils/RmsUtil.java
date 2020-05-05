package com.bcat.utils;

import okhttp3.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class RmsUtil {

    public static String login(String cookie){
        try {
            OkHttpClient client = new OkHttpClient();
            String head = "Host: www.rms110.com\n" +
                    "Connection: keep-alive\n" +
                    "Upgrade-Insecure-Requests: 1\n" +
                    "User-Agent: Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.103 Safari/537.36\n" +
                    "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3\n" +
                    "Accept-Language: zh-CN,zh;q=0.9\n" + cookie;
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
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getAlarmUrlById(String id, String cookie){
        OkHttpClient client = new OkHttpClient();
        String head = "Host: www.rms110.com:" +
                "Connection: keep-alive:" +
                "Upgrade-Insecure-Requests: 1:" +
                "User-Agent: Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.103 Safari/537.36:" +
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3:" +
                "Accept-Language: zh-CN,zh;q=0.9:" + cookie;
        String[] heads = head.split(":");
        Headers headers = Headers.of(heads);

        Request request = new Request.Builder()
                .get()
                .url("http://www.rms110.com/high-level-alarm/edit?taskSendId=" + id)
                .headers(headers)
                .build();
        String url = null;
        try {
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
