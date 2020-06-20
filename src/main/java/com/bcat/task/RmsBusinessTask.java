package com.bcat.task;

import com.bcat.controller.RmsBusinessController;
import com.bcat.view.MainView;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class RmsBusinessTask extends RmsTask implements Runnable {

    private int bufferHour;

    public RmsBusinessTask(String cookie, int timeOut){
        super(cookie, timeOut);
    }

    @Override
    public void run() {
        for (;;){
            // 监控时间段报时
            if (bufferHour != Calendar.getInstance().get(Calendar.HOUR_OF_DAY)){
                bufferHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
                MainView.logUtil.info("  -------------  "+ bufferHour + "点  -------------");
            }
            // 获取数据
            RmsBusinessController rmsBusinessController = new RmsBusinessController();
            List<Map<String, String>> sendAlarmData = rmsBusinessController.getAlarmData(cookie);

            try {
                Thread.sleep(timeOut);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
