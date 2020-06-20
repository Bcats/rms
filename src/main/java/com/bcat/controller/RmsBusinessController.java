package com.bcat.controller;

import com.bcat.service.RmsBusinessService;

import java.util.List;
import java.util.Map;

public class RmsBusinessController extends RmsController{

    public List<Map<String,String>> getAlarmData(String cookie){
        List<Map<String,String>> sendDataArrays = new RmsBusinessService().checkSend(cookie);

        return sendDataArrays;
    }

}
