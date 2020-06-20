package com.bcat.service;

import com.bcat.constant.CommonConstant;
import com.bcat.dao.ZabbixHostDao;
import com.bcat.model.MailModel;
import com.bcat.utils.EmailUtil;
import com.bcat.utils.TimeUtil;
import com.bcat.view.MainView;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ZabbixHostService extends ZabbixService{

    public List<Map<String, String>> checkSend(){
        List<Map<String, String>> mapList = new ZabbixHostDao().getData();
        List<Map<String, String>> sendDataList = new ArrayList<>();

        if (mapList == null || mapList.size() < 1)
            return sendDataList;

        for (Map<String, String> map : mapList){
            String host = map.get("host");
            String question = map.get("question");
            String time = map.get("time");
            String level = map.get("level");
            Date date = TimeUtil.toDate(time, TimeUtil.DATE_PATTERN_DEFAULT);
            // 告警出现20分钟至9小时内且为红色告警。
            if (level.equals("disaster-bg") &&
                    !isContains(new String[]{host, question}, "忽略") &&
                    TimeUtil.isOverMinutes(date,15) &&
                    !readSendSign(map.toString(), CommonConstant.ZABBIX_SEND_SIGN_FILENAME)){
                sendDataList.add(map);
                // 触发告警条件执行操作
                execute(map);
//                writeSendSign(map.toString(), CommonConstant.ZABBIX_SEND_SIGN_FILENAME);
            }
        }
        return sendDataList;
    }

    private void execute( Map<String,String> dataMap){
        // 更新logText
        MainView.logUtil.info("  zabbixHost 有一条超过十分钟的红色告警");
        // 写入文件
        writeSendSign(dataMap.toString(), CommonConstant.RMS_SEND_SIGN_FILENAME);
        // 发送邮件
        receiver = MainView.emailText.getText();
        MailModel mailModel = new MailModel(receiver)
                .setZabbixMailMail(dataMap);
        EmailUtil.sendEmail(mailModel);
        // 更新表格数据
        String[] row = new String[]{"", "", "", "", "", "", "", ""};
        row[1] = dataMap.get("host");
        row[2] = dataMap.get("question");
        row[5] = dataMap.get("time");
        MainView.myTableModel.list.add(row);
        MainView.alarmTable.updateUI();
    }
}
