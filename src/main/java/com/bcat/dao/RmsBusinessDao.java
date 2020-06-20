package com.bcat.dao;

import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.Response;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class RmsBusinessDao extends RmsDao {

    protected final static String BUSINESS_ALARM_URL = "http://www.rms110.com/high-level-alarm/high-level-alarm?page=1";

    protected final static String BUSINESS_ALARM_DETAIL_URL_PREFIX = "http://www.rms110.com/high-level-alarm/edit?taskSendId=";

    protected final static String[] BUSINESS_TITLE_ARRAY = {"id", "projectName","alarmPointName", "errorInfo", "level", "alarmTime", "status","operator", "isAlarm", "notifyUserName", "notifyTime", "notifyResult", "alarmDetailUrl"};

    /**
     * 获取RMS业务告警数据。
     * @param cookie rms网站cookie，例 “Cookie : xxxx”
     * @return <code> true </code> return business data
     *         <code> false </code> return null;
     */
    public List<Map<String,String>> getBusinessAlarms(String cookie){
        try {
//            OkHttpClient client = new OkHttpClient();
            String head = DEFAULT_HEADER + cookie;
            String[] heads = head.split(":|\n");
            Headers headers = Headers.of(heads);

            Request request = new Request.Builder()
                    .get()
                    .url(BUSINESS_ALARM_URL)
                    .headers(headers)
                    .build();
            Call call = client.newCall(request);

            Response response = call.execute();
            String result = Objects.requireNonNull(response.body()).string();
            return parseStr(true, result, cookie);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("[error] -> getBusinessAlarms");
            return null;
        }
    }

}
