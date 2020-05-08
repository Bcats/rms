package com.bcat.Handler;

import com.bcat.utils.MyStringUtil;
import org.json.JSONObject;
import okhttp3.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ZabbixHandler {

    private static final String ZABBIX_OF_SERVER_HOME_URL = "http://119.81.140.244/index.php";

    private static final String HEADER_ =
            "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3\n" +
            "Accept-Encoding: gzip, deflate\n" +
            "Accept-Language: zh-CN,zh;q=0.9\n" +
            "Cache-Control: max-age=0\n" +
            "Host: 119.81.140.244\n" +
            "Upgrade-Insecure-Requests: 1\n" +
            "User-Agent: Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.103 Safari/537.36\n";

    private String cookie;

    private final OkHttpClient okHttpClient = new OkHttpClient().newBuilder().followRedirects(false).followSslRedirects(false).build();

    private Map login(){
        this.cookie = getCookie();
        //OkHttpClient okHttpClient = new OkHttpClient();
        String head = HEADER_ +
                "Content-Type: application/x-www-form-urlencoded\n" +
                "Cookie: " + cookie + "\n" +
                "Origin: http://119.81.140.244\n" +
                "Referer: http://119.81.140.244/index.php\n";

        String[] heads = head.split(": |\n");

        RequestBody requestBody = new FormBody.Builder()
                .add("name","monitor")
                .add("password","monitor")
                .add("autologin","1")
                .add("enter","Sign in")
                .build();
        Request request = new Request.Builder()
                .url(ZABBIX_OF_SERVER_HOME_URL)
                .headers(Headers.of(heads))
                .post(requestBody)
                .build();

        Call call = okHttpClient.newCall(request);
        Map map = new HashMap<>();
        try {
            Response response = call.execute();
            List<String> l = response.headers().values("Set-Cookie");
            ResponseBody responseBody = response.body();
            String body = responseBody.string();
            if(l.size() == 0){
                throw new NullPointerException();
            }
            String cookie_ = cookie.split(";")[0] + "; "+ l.get(0).split(";")[0] + ";";
            String sessionId = MyStringUtil.getSubString(cookie,"zbx_sessionid=",";");
            String sid = sessionId.substring(16);

            map.put("code", response.code());
            map.put("cookie", cookie_);
            map.put("sid", sid);
            map.put("okHttpClient", okHttpClient);

            responseBody.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    private String getCookie(){
        //OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .get()
                .url(ZABBIX_OF_SERVER_HOME_URL)
                .build();
        Call call = okHttpClient.newCall(request);
        String cookie = null;
        try {
            Response response = call.execute();
            Headers headers = response.headers();
            
            List<String> l = headers.values("Set-Cookie");
            
            cookie = l.get(3) + l.get(0);
            cookie = cookie.replace("path=/","");
            response.body().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cookie;
    }

    public List<Map<String, String>> getData(){
        Map map = login();
        String cookie = (String) map.get("cookie");
        String sid = (String) map.get("sid");

        String data_url = "http://119.81.140.244/zabbix.php?action=widget.issues.view&sid=" + sid +"&upd_counter=1&pmasterid=dashboard";

        String head = HEADER_ + "Cookie: " + cookie ;

        String[] heads = head.split(": |\n");
        Request request = new Request.Builder()
                .url(data_url)
                .headers(Headers.of(heads))
                .get()
                .build();

        Call call = okHttpClient.newCall(request);
        List<Map<String, String>> mapList = new ArrayList<>();
        try {
            Response response = call.execute();
            ResponseBody responseBody = response.body();
            JSONObject jsonObject = new JSONObject(responseBody.string());
            String body = (String) jsonObject.get("body");
            List<String> hostStrings = MyStringUtil.getSubStrings(body, "<span class=\"link-action\" data-menu-popup=","</span></td>");
            List<String> questionStrings = MyStringUtil.getSubStrings(body, "</span></td><td cl", "<thead><tr><th>时间</th><th>状");
            List<String> sTimeStrings = MyStringUtil.getSubStrings(body,"确认</th></tr></thead><tbody><tr><td>", "</td>");
            for (int i = 0; i < hostStrings.size(); i++) {
                Map<String, String> stringMap = new HashMap<>();
                String host = MyStringUtil.getSubString(hostStrings.get(i), "}\">","");
                stringMap.put("host", host);
                String level = MyStringUtil.getSubString(questionStrings.get(i), "ass=","\">");
                stringMap.put("level", level);
                String question = MyStringUtil.getSubString(questionStrings.get(i), "n\">","<s");
                stringMap.put("question", question);
                String time = MyStringUtil.getSubString(sTimeStrings.get(i),"确认</th></tr></thead><tbody><tr><td>","</td>");
                stringMap.put("time", time);
                mapList.add(stringMap);
            }
            responseBody.close();
            return mapList;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }
}

