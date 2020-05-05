package com.bcat.Service;

import com.bcat.utils.MyStringUtil;
import okhttp3.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ZabbixService {

    private static final String ZABBIX_OF_SERVER_HOME_URL = "http://119.81.140.244/index.php";

    private String cookie;

    public Map login(){
        this.cookie = getCookie();
        OkHttpClient okHttpClient = new OkHttpClient();
        String head = "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3\n" +
                "Accept-Encoding: gzip, deflate\n" +
                "Accept-Language: zh-CN,zh;q=0.9\n" +
                "Cache-Control: max-age=0\n" +
                "Connection: keep-alive\n" +
                "Content-Type: application/x-www-form-urlencoded\n" +
                "Cookie: " + cookie + "\n" +
                "Host: 119.81.140.244\n" +
                "Origin: http://119.81.140.244\n" +
                "Referer: http://119.81.140.244/index.php\n" +
                "Upgrade-Insecure-Requests: 1\n" +
                "User-Agent: Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.103 Safari/537.36";

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
            String body = response.body().string();
            String cookie = l.get(3) + l.get(0);
            cookie = cookie.replace("path=/","");
            String sessionId = MyStringUtil.getSubString(cookie,"zbx_sessionid=",";");
            String sid = sessionId.substring(16);

            map.put("code", response.code());
            map.put("cookie", cookie);
            map.put("sid", sid);
            map.put("okHttpClient", okHttpClient);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    private String getCookie(){
        OkHttpClient okHttpClient = new OkHttpClient();
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

        } catch (IOException e) {
            e.printStackTrace();
        }
        return cookie;
    }

    public List<String> getData(String cookie, String sid, OkHttpClient okHttpClient){

        String data_url = "http://119.81.140.244/zabbix.php?action=widget.issues.view&sid=" + sid +"&upd_counter=1&pmasterid=dashboard";

        String head = "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3\n" +
                "Accept-Encoding: gzip, deflate\n" +
                "Accept-Language: zh-CN,zh;q=0.9\n" +
                "Cache-Control: max-age=0\n" +
                "Connection: keep-alive\n" +
                "Cookie: " + cookie + "\n" +
                "Host: 119.81.140.244\n" +
                "Upgrade-Insecure-Requests: 1\n" +
                "User-Agent: Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.103 Safari/537.36";

        String[] heads = head.split(": |\n");
        Request request = new Request.Builder()
                .url(data_url)
                .headers(Headers.of(heads))
                .get()
                .build();

        Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            String body = response.body().string();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}

