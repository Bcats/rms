package com.bcat.Service;

import com.bcat.utils.TimeUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ZabbixService {

    public static List<Map<String, String>> checkSend(List<Map<String, String>> mapList){
        List<Map<String, String>> sendDataList = new ArrayList<>();
        for (Map<String, String> map : mapList){
            String time = map.get("time");
            String level = map.get("level");
            Date date = TimeUtil.toDate(time,"yyyy-MM-dd HH:ss:mm");
            // 告警出现十分钟且为红色告警。
            if (TimeUtil.isOverTenMinutes(date) && level.equals("disaster-bg")){
                sendDataList.add(map);
            }
        }
        return sendDataList;
    }

}
