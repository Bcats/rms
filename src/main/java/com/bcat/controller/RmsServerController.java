package com.bcat.controller;

import com.bcat.service.RmsServerService;

import java.util.List;
import java.util.Map;

public class RmsServerController extends RmsController{

    private static final String[] SERVER_ALARM_KEYS = { "alive", "net.port.listen" };

    public List<Map<String,String>> getAlarmData(String cookie){
        List<Map<String,String>> sendDataArrays = new RmsServerService().checkSend(cookie);

        return sendDataArrays;
    }

}
