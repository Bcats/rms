package com.bcat.task;

import com.bcat.controller.RmsServerController;

import java.util.List;
import java.util.Map;

public class RmsServerTask extends RmsTask implements Runnable {

    public RmsServerTask(String cookie, int timeOut) {
        super(cookie, timeOut);
    }

    @Override
    public void run() {
        for (;;){
            RmsServerController rmsServerController = new RmsServerController();
            List<Map<String, String>> sendAlarmData = rmsServerController.getAlarmData(cookie);

            try {
                Thread.sleep(timeOut);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
