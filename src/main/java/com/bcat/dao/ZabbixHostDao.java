package com.bcat.dao;

import com.bcat.model.MailModel;
import com.bcat.utils.CommonUtil;
import com.bcat.utils.EmailUtil;
import com.bcat.utils.MyStringUtil;
import com.bcat.view.MainView;
import okhttp3.*;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

public class ZabbixHostDao extends ZabbixDao {

    private static final String ZABBIX_OF_SERVER_HOME_URL = "http://119.81.140.244/index.php";
    private static final String HEADER_ = "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3\n" + "Accept-Encoding: gzip, deflate\n" + "Accept-Language: zh-CN,zh;q=0.9\n" + "Cache-Control: max-age=0\n" + "Host: 119.81.140.244\n" + "Upgrade-Insecure-Requests: 1\n" + "User-Agent: Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.103 Safari/537.36\n";
    private static final int MAX_RETRY_COUNT = 3;
    private static final OkHttpClient okHttpClient;
    private static final RequestBody zabbixRequestBody;
    private static Map<String,String> cookieMap;
    private static final String ZABBIX_USERNAME = "monitor";
    private static final String ZABBIX_PASSWORD = "monitor";

    static {
        okHttpClient = new OkHttpClient()
                .newBuilder()
                .followRedirects(false)
                .followSslRedirects(false)
                .build();
        zabbixRequestBody = new FormBody.Builder()
                .add("name", ZABBIX_USERNAME)
                .add("password",ZABBIX_PASSWORD)
                .add("autologin","1")
                .add("enter","Sign in")
                .build();
        cookieMap = login(zabbixRequestBody);
    }

    private static Map<String,String> login(RequestBody zabbixRequestBody){
        // try to get cookie，success break；
        String cookie = "";
        for (int i = 0; i < MAX_RETRY_COUNT; i++){
            cookie = getCookie();
            if (cookie != null && !cookie.equals("")) break;
        }
        String head = HEADER_ +
                "Content-Type: application/x-www-form-urlencoded\n" +
                "Cookie: " + cookie + "\n" + "Origin: http://119.81.140.244\n" +
                "Referer: http://119.81.140.244/index.php\n";
        String[] heads = head.split(": |\n");

        Request request = new Request.Builder()
                .url(ZABBIX_OF_SERVER_HOME_URL)
                .headers(Headers.of(heads))
                .post(zabbixRequestBody)
                .build();
        Call call = okHttpClient.newCall(request);

        try (Response response = call.execute()){

            Map<String,String> map = new HashMap<>();
            List<String> l = response.headers().values("Set-Cookie");
            if(l.size() == 0) throw new NullPointerException();

            String cookie_ = cookie.split(";")[0] + "; "+ l.get(0).split(";")[0] + ";";
            String sessionId = MyStringUtil.getSubString(cookie,"zbx_sessionid=",";");
            String sid = sessionId.substring(16);

            map.put("cookie", cookie_);
            map.put("sid", sid);

            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String getCookie(){
        Request request = new Request.Builder()
                .get()
                .url(ZABBIX_OF_SERVER_HOME_URL)
                .build();
        Call call = okHttpClient.newCall(request);
        try (Response response = call.execute()){
            Headers headers = response.headers();
            List<String> l = headers.values("Set-Cookie");
            String cookie = l.get(3) + l.get(0);
            cookie = cookie.replace("path=/","");
            return cookie;
        } catch (IndexOutOfBoundsException | IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    public List<Map<String, String>> getData(){
        if (cookieMap == null) {
            EmailUtil.sendEmail(new MailModel(MainView.emailText.getText(),
                    "zabbix网络异常",
                    "累计十次获取数据失败，请查检网络或更新cookie"));
            System.out.println("[error] -> ZabbixHostDao 已发送 “ 网络错误或cookie失效 ” 邮件");
            throw new RuntimeException() ;
        }
        List<Map<String,String>> mapList = new ArrayList<>();
        String cookie = cookieMap.get("cookie");
        String sid = cookieMap.get("sid");

        String data_url = "http://119.81.140.244/zabbix.php?action=widget.issues.view&sid=" + sid +"&upd_counter=1&pmasterid=dashboard";

        String head = HEADER_ + "Cookie: " + cookie ;
        String[] heads = head.split(": |\n");
        Request request = new Request.Builder()
                .url(data_url)
                .headers(Headers.of(heads))
                .get()
                .build();

        Call call = okHttpClient.newCall(request);

        try (Response response = call.execute();
             ResponseBody responseBody = response.body()){
            if (response.code() != 200) return null;
            Objects.requireNonNull(responseBody);

            JSONObject jsonObject = new JSONObject(responseBody.string());
            String body = (String) jsonObject.get("body");
//            Pattern pattern = Pattern.compile("<span class=\"link-action\" data-menu-popup=(.*?)\">(.*?)</span></td><td class=\"(.*?)\">(.*?)</td><td><a href=\"(.*?)\">(.*?)</a></td><td>");
//            Matcher matcher = pattern.matcher(body);
//            System.out.println("host="+matcher.group(1)+"level="+matcher.group(2)+"question="+matcher.group(3)+"time="+matcher.group(5));
            List<String> hostStrings = MyStringUtil.getSubStrings(body, "<span class=\"link-action\" data-menu-popup=","</span></td>");
            List<String> questionStrings = MyStringUtil.getSubStrings(body, "</span></td><td cl", "<thead><tr><th>时间</th><th>状");
            List<String> sTimeStrings = MyStringUtil.getSubStrings(body,"确认</th></tr></thead><tbody><tr><td>", "</td>");
            int count = CommonUtil.getMinByInts(hostStrings.size(),questionStrings.size(),sTimeStrings.size());
            for (int i = 0; i < count; i++) {
                Map<String,String> stringMap = new HashMap<>();
                String host = MyStringUtil.getSubString(hostStrings.get(i), "}\">","");
                stringMap.put("host", host);
                String level = MyStringUtil.getSubString(questionStrings.get(i), "ass=\"","\">");
                stringMap.put("level", level);
                String question = MyStringUtil.getSubString(questionStrings.get(i), "n\">","<s");
                stringMap.put("question", question);
                String time = MyStringUtil.getSubString(sTimeStrings.get(i),"确认</th></tr></thead><tbody><tr><td>","</td>");
                stringMap.put("time", time);
                mapList.add(stringMap);
            }
            return mapList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
