package com.bcat.dao;

import com.bcat.model.MailModel;
import com.bcat.utils.EmailUtil;
import com.bcat.view.MainView;
import okhttp3.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class RmsServerDao extends RmsDao{

    protected final static String SERVER_ALARM_URL = "http://www.rms110.com/high-level-alarm-open-falcon/high-level-alarm?page=";

    protected final static String SERVER_ALARM_DETAIL_URL_PREFIX = "http://www.rms110.com/high-level-alarm-open-falcon/edit?taskId=";

    protected final static String[] SERVER_TITLE_ARRAY = {"id", "businessName","metric", "tag", "level", "alarmTime", "status","operator", "isAlarm", "notifyUserName", "notifyTime", "notifyResult", "alarmDetailUrl"};

    private int errorCount = 0;

    /**
     * 获取RMS服务器告警数据。
     * @param cookie rms网站cookie，例 “Cookie : xxxx”
     * @return <code> true </code> return server data
     *         <code> false </code> return null;
     */
    public List<Map<String,String>> getServerAlarms(String cookie){
        int page = 1;
//        OkHttpClient client = new OkHttpClient();
        String head = DEFAULT_HEADER + cookie;
        String[] heads = head.split(":|\n");
        try {
            Headers headers = Headers.of(heads);
            Request request = new Request.Builder()
                    .get()
                    .url(SERVER_ALARM_URL + page)
                    .headers(headers)
                    .build();
            Call call = client.newCall(request);
            Response response = call.execute();
            ResponseBody responseBody = response.body();
            Objects.requireNonNull(responseBody);
            return parseStr(false, responseBody.string(), cookie);
        } catch (Exception e) {
            e.printStackTrace();
            MainView.logUtil.error("  rms网络异常或Cookie失效");
            errorCount++;
            if (errorCount >= 10) {
                EmailUtil.sendEmail(new MailModel(MainView.emailText.getText(),
                        "rms网络异常或Cookie失效",
                        "累计十次获取数据失败，请查检网络或更新cookie"));
                errorCount = 0;
            }
            System.out.println("[error] -> RmsServerDao 已发送 “ 网络错误或cookie失效 ” 邮件");
            return null;
        }
    }
}
