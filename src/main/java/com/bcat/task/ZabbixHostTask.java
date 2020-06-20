package com.bcat.task;

import com.bcat.controller.ZabbixHostController;
import com.bcat.utils.TimeUtil;
import com.bcat.view.MainView;

import java.util.List;
import java.util.Map;

public class ZabbixHostTask implements Runnable {

    private int timeOut;

    private int runCount = 0;

    private ZabbixHostTask(){}

    public ZabbixHostTask(int timeOut){
        this.timeOut = timeOut;
    }

    @Override
    public void run() {
        for (;;){
            ZabbixHostController zabbixHostController = new ZabbixHostController();
            List<Map<String, String>> sendAlarmData = zabbixHostController.getAlarmData();

            // 调用状态label更新
            MainView.statusLabel.setText("状态：第 " + (++runCount) + " 次调用,时间："+ TimeUtil.getDayTime());

            try {
                Thread.sleep(timeOut);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
