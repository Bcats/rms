package com.bcat;

import com.bcat.controller.RmsServerController;
import com.bcat.controller.ZabbixHostController;
import com.bcat.task.RmsBusinessTask;
import com.bcat.task.RmsServerTask;
import com.bcat.task.ZabbixHostTask;
import com.bcat.utils.TimeUtil;
import com.bcat.view.MainView;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class App {

    private final static int timeOut = 30000;

    private static ExecutorService executor;

    public static void main(String[] args) {

        // 渲染使用界面
        MainView mainView = new MainView();
//        mainView.init();
        // 开始按钮触发事件
        MainView.runButton.addActionListener((e)->{
            if (MainView.runButton.getText().equals("开始"))
                onStart();
            else
                onStop();
        });
    }

    public static void onStart(){
        if (!MainView.isTextNotNull()){
            MainView.logUtil.error("  cookie或email地址不能为空 ");
            return;
        }

        MainView.saveAccount();
        MainView.cookieText.setEnabled(false);
        MainView.emailText.setEnabled(false);
        MainView.runButton.setText("正在运行");
        MainView.logUtil.info("  监控运行中, 当前每 " + timeOut / 1000 + "s 监控一次");
        MainView.statusLabel.setText("状态：定时器已启动,等待触发");

        String cookie = MainView.cookieText.getText();
        if (executor == null){
            // 创建线程池
            executor = Executors.newFixedThreadPool(3);
            // 开始执行任务线程
            executor.execute(new RmsBusinessTask(cookie, timeOut));
            executor.execute(new RmsServerTask(cookie, timeOut));
            executor.execute(new ZabbixHostTask(timeOut));
        }

    }

    public static void onStop(){
        MainView.runButton.setText("开始");
        MainView.cookieText.setEnabled(true);
        MainView.emailText.setEnabled(true);
        // 关闭任务线程池
//        if (executor != null) executor.shutdown();
        MainView.logUtil.info("  监控暂停");
        MainView.statusLabel.setText("状态：无");
    }

}
