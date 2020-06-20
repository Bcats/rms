package com.bcat.controller;

import com.bcat.service.ZabbixHostService;

import java.util.List;
import java.util.Map;

public class ZabbixHostController extends ZabbixController{

    public List<Map<String, String>> getAlarmData(){
        List<Map<String,String>> sendDataArrays = new ZabbixHostService().checkSend();

        return sendDataArrays;
    }

}
