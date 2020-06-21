package com.bcat.service;

import com.bcat.constant.CommonConstant;
import com.bcat.dao.RmsBusinessDao;
import com.bcat.dao.RmsDao;
import com.bcat.dao.RmsServerDao;
import com.bcat.model.MailModel;
import com.bcat.utils.EmailUtil;
import com.bcat.utils.TimeUtil;
import com.bcat.view.MainView;

import java.util.*;

public class RmsBusinessService extends RmsService {

    public List<Map<String,String>> checkSend(String cookie)  {
        List<Map<String,String>> sendDataArrays = new ArrayList<>();
        List<Map<String,String>> dataArrays = new RmsBusinessDao().getBusinessAlarms(cookie);

        if (dataArrays == null || dataArrays.size() < 1)
            return sendDataArrays;

        for (Map<String,String> dataMap : dataArrays) {
            String alarmTime = dataMap.get("alarmTime");
            String status = dataMap.get("status");
            String isAlarm = dataMap.get("isAlarm");

            if (isContains(status, new String[]{"待处理"}) &&
                    isContains(isAlarm, new String[]{"是"}) &&
                    TimeUtil.isInOneHour(alarmTime) &&
                    !readSendSign(dataMap.toString(), CommonConstant.RMS_SEND_SIGN_FILENAME))
            {
                sendDataArrays.add(dataMap);
                // 触发告警条件执行操作
                execute(dataMap);
            }

        }
        return sendDataArrays;
    }

    private void execute( Map<String,String> dataMap){
        // 更新logText
        MainView.logUtil.info("  [" + dataMap.get("level") + "] " +  dataMap.get("projectName")  + " " + dataMap.get("errorInfo"));
        // 写入文件
        writeSendSign(dataMap.toString(), CommonConstant.RMS_SEND_SIGN_FILENAME);
        // 发送邮件
        receiver = MainView.emailText.getText();
        MailModel mailModel = new MailModel(receiver)
                .setRmsBusinessMail(dataMap);
        EmailUtil.sendEmail(mailModel);
        // 更新表格数据
        String[] row = RmsDao.mapToStrings(dataMap);
        MainView.myTableModel.list.add(0, row);
        MainView.alarmTable.updateUI();
    }

}
