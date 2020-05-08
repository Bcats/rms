package com.bcat.Handler;

import okhttp3.OkHttpClient;
import org.junit.jupiter.api.Test;

import java.util.Map;


class ZabbixServiceTest {

    @Test
    public void test(){
        ZabbixHandler zabbixService = new ZabbixHandler();
        zabbixService.getData();



    }

}