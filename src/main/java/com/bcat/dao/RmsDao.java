package com.bcat.dao;

import com.bcat.constant.CommonConstant;
import com.bcat.utils.CommonUtil;
import com.bcat.utils.MyStringUtil;
import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;

public class RmsDao extends Dao{

    protected final static String DEFAULT_HEADER = "Connection: keep-alive\n" + "Upgrade-Insecure-Requests: 1\n" + "User-Agent: Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.103 Safari/537.36\n" + "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3\n" + "Accept-Language: zh-CN,zh;q=0.9\n";

    private List<String[]> list = new ArrayList<>();

    protected List<Map<String,String>> parseStr(boolean isBusiness, String str, String cookie) {
        List<Map<String, String>> dataArrays = new ArrayList<>();
        try {
            String[] headTitleArray = isBusiness ? RmsBusinessDao.BUSINESS_TITLE_ARRAY : RmsServerDao.SERVER_TITLE_ARRAY;
            Document document = Jsoup.parse(str);
            Elements mytbElements = document.getElementsByClass("myTableBox");
            Element mytbElement = mytbElements.get(0);

            Elements tbElements = mytbElement.getElementsByTag("tbody");
            Element tbElement = tbElements.get(0);

            Elements trElements = tbElement.getElementsByTag("tr");

            for (Element trElement : trElements) {
                Elements tdElements = trElement.getElementsByTag("td");
                Map<String, String> dataMap = new HashMap<>();
                for (int i = 0; i < tdElements.size(); i++) {
                    if (i == 12) {
                        dataMap.put(headTitleArray[12], getAlarmUrlById(isBusiness, tdElements.get(0).text(), cookie));
                    } else {
                        dataMap.put(headTitleArray[i], tdElements.get(i).text());
                    }


                }
                dataArrays.add(dataMap);
            }
            return dataArrays;
        } catch (Exception e) {
            System.out.println("[error] " + str);
            e.printStackTrace();
            return null;
        }
    }

    public String getAlarmUrlById(boolean isBusiness ,String id, String cookie){
//        OkHttpClient client = new OkHttpClient();
        String head = DEFAULT_HEADER + cookie;
        String alarmUrl = null;

        String urlPrefix = isBusiness ? RmsBusinessDao.BUSINESS_ALARM_DETAIL_URL_PREFIX : RmsServerDao.SERVER_ALARM_DETAIL_URL_PREFIX;
        try {
            String[] heads = head.split(":|\n");
            Headers headers = Headers.of(heads);

            Request request = new Request.Builder()
                    .get()
                    .url(urlPrefix + id)
                    .headers(headers)
                    .build();
            Call call = client.newCall(request);
            Response response = call.execute();
            String result = Objects.requireNonNull(response.body()).string();
            Document document = Jsoup.parse(result);
            Elements elements = document.getElementsByAttribute("target");
            Element element = elements.get(0);
            alarmUrl = element.attr("href");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return alarmUrl;
    }

    public List<String[]> getTotal(){
        String rmsDataStr = CommonUtil.readStr(CommonConstant.RMS_SEND_SIGN_FILENAME);
        if (rmsDataStr == null){
            return list;
        }
        String[] rmsDataStrings = rmsDataStr.split("\n");
        for (String rmsDataRow : rmsDataStrings) {
            Map<String, String> map = MyStringUtil.mapStringToMap(rmsDataRow);
            String[] tableStrings = new String[8];
            tableStrings[0] = map.get("id");
            tableStrings[1] = map.containsKey("projectName") ? map.get("projectName") : map.get("businessName");
            tableStrings[2] = map.containsKey("alarmPointName") ? map.get("alarmPointName") : map.get("metric");
            tableStrings[3] = map.containsKey("errorInfo") ? map.get("errorInfo") : map.get("tag");
            tableStrings[4] = map.get("level");
            tableStrings[5] = map.get("alarmTime");
            tableStrings[6] = map.get("status");
            tableStrings[7] = map.get("alarmDetailUrl");
            list.add(tableStrings);
        }
        return list;
    }

    public static String[] mapToStrings(Map<String,String> map){
        String[] strings = new String[8];
        try {
            strings[0] = map.get("id");
            strings[1] = map.containsKey("projectName") ? map.get("projectName") : map.get("businessName");
            strings[2] = map.containsKey("alarmPointName") ? map.get("alarmPointName") : map.get("metric");
            strings[3] = map.containsKey("errorInfo") ? map.get("errorInfo") : map.get("tag");
            strings[4] = map.get("level");
            strings[5] = map.get("alarmTime");
            strings[6] = map.get("status");
            strings[7] = map.get("alarmDetailUrl");
        }catch (NullPointerException e){
            e.printStackTrace();
            return strings;
        }
        return strings;
    }

}
