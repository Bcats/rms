package com.bcat.Service;

import okhttp3.OkHttpClient;
import org.junit.jupiter.api.Test;

import java.util.Map;


class ZabbixServiceTest {

    @Test
    public void test(){
        ZabbixService zabbixService = new ZabbixService();
        Map map = zabbixService.login();
        if (map.get("code").equals(200)){
            String cookie = (String)map.get("cookie");
            String sid = (String)map.get("sid");
            OkHttpClient okHttpClient = (OkHttpClient)map.get("okHttpClient");
            zabbixService.getData(cookie , sid, okHttpClient);
        }


    }

}