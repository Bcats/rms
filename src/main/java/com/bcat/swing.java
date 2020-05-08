package com.bcat;

import com.bcat.Handler.RmsHandler;
import com.bcat.Handler.ZabbixHandler;
import com.bcat.Service.RmsService;
import com.bcat.Service.ZabbixService;
import com.bcat.model.Mail;
import com.bcat.utils.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class swing {

    private static JLabel cookieLabel;
    
    private static JTextField cookieText;
    
    private static JLabel emailLabel;
    
    private static JTextField emailText;

    private static JButton runButton;

    private static JTextArea logText;

    private static JLabel statusLabel;


    public static void main(String[] args) {
            // 创建 JFrame 实例
            JFrame frame = new JFrame("rms110");
            // Setting the width and height of frame
            frame.setSize(400, 430);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            /* 创建面板，这个类似于 HTML 的 div 标签
             * 我们可以创建多个面板并在 JFrame 中指定位置
             * 面板中我们可以添加文本字段，按钮及其他组件。
             */
            JPanel panel = new JPanel();
            // 添加面板
            frame.add(panel);
            /*
             * 调用用户定义的方法并添加组件到面板
             */
            placeComponents(panel);

            // 设置界面可见
            frame.setVisible(true);
        }

        private static void placeComponents(JPanel panel) {

            /* 布局部分我们这边不多做介绍
             * 这边设置布局为 null
             */
            panel.setLayout(null);

            cookieLabel = new JLabel("Cookie:");
            cookieLabel.setBounds(10,20,60,25);
            panel.add(cookieLabel);

            cookieText = new JTextField(20);
            cookieText.setBounds(80,20,290,25);
            panel.add(cookieText);

            emailLabel = new JLabel("Email:");
            emailLabel.setBounds(10,60,60,25);
            panel.add(emailLabel);

            emailText = new JTextField(20);
            emailText.setBounds(80,60,180,25);
            panel.add(emailText);

            // 创建登录按钮
            runButton = new JButton("开始");
            runButton.setBounds(270, 60, 100, 25);
            panel.add(runButton);

            logText = new JTextArea("    请输入打开RMS获取Cookie");
            JScrollPane scrollPane = new JScrollPane(logText);
            logText.setLineWrap(true);
            logText.setEditable(false);
            logText.setBounds(10,100,360,260);
            scrollPane.setBounds(10, 100, 360, 260);
            panel.add(scrollPane);

            statusLabel = new JLabel("状态：无");
            statusLabel.setBounds(10,365,360,25);
            panel.add(statusLabel);

            final LogUtil logUtil = new LogUtil(logText);
            final int delay = 30000;
            AtomicInteger count = new AtomicInteger();
            AtomicInteger bufferHour = new AtomicInteger();
            ZabbixHandler zabbixHandler = new ZabbixHandler();
            RmsHandler rmsHandler = new RmsHandler();
            // 监控定时器
            final Timer timer = new Timer(delay, (e) -> {

                String cookie = cookieText.getText();
                String emailReceiver = emailText.getText();
                // 获取数据
                List<List<String>> rmsDataArrays = rmsHandler.getData(cookie);
                List<Map<String,String>> zabbixDataArrays = zabbixHandler.getData();

                if (rmsDataArrays == null){
                    logUtil.error("  rms网络异常或Cookie失效");
                    return;
                }
                if (zabbixDataArrays == null){
                    logUtil.error("  zabbix网络异常或Cookie失效");
                    return;
                }

                // 当前监控时间段报时
                if (bufferHour.get() != Calendar.getInstance().get(Calendar.HOUR_OF_DAY)){
                    bufferHour.set(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
                    logUtil.info("  -------------  "+ bufferHour.get() + "点  -------------");
                }
                // 获取需要发送的数据
                List<List<String>> rmsSendDataArrays = RmsService.checkSend(rmsDataArrays);
                List<Map<String,String>> zabbixSendDataArrays = ZabbixService.checkSend(zabbixDataArrays);

                /*if (sendDataArrays.size() != 0){
                    logUtil.info("  共获取 " + dataArrays.size() + " 条数据, " +
                            "需发送 " + sendDataArrays.size() + " 条数据");
                }*/

                // 开始发送rms告警邮件
                List<Mail> rmsMailList = new ArrayList<>();
                for (List<String> rsdArray : rmsSendDataArrays){
                    String id = rsdArray.get(0);
                    String dataTime = rsdArray.get(5);
                    String level = rsdArray.get(4);
                    rsdArray.set(12, rmsHandler.getAlarmUrlById(id, cookie));
                    String title = "[" + TimeUtil.getHourByDate(dataTime) + "点][rms][" + rsdArray.get(4) + "]" + rsdArray.get(1);
                    String content = rsdArray.toString();
                    rmsMailList.add(new Mail(emailReceiver, title, content));
                    logUtil.info("  发送一条 [ " + level + " ] 告警至邮箱 ");
                }
                List<Boolean> rmsBooleanList = EmailUtil.sendEmails(rmsMailList);

                //  开始发送zabbix告警邮件
                List<Mail> zabbixMailList = new ArrayList<>();
                for (Map<String,String> zsdArray : zabbixSendDataArrays){
                    String time = zsdArray.get("time");
                    String title = "[" + time + "]zabbix有一条红色告警超10分钟未消除";
                    String content = zsdArray.toString();
                    zabbixMailList.add(new Mail(emailReceiver, title, content));
                    logUtil.info("  发送一条 [ zbbix ] 告警至邮箱 ");
                }
                List<Boolean> zabbixBooleanList = EmailUtil.sendEmails(zabbixMailList);

                // 调用状态label更新
                statusLabel.setText("状态：第 " + count.incrementAndGet() + " 次调用,时间："+ TimeUtil.getDayTime());
            });

            // 开始按钮事件
            runButton.addActionListener((e) -> {

                if (runButton.getText().equals("开始")){
                    // 开始首先检测Cookie
                    String cookie = cookieText.getText();
                    List<List<String>> rmsDataArrays = rmsHandler.getData(cookie);
                    List<Map<String,String>> zabbixDataArrays = zabbixHandler.getData();

                    if (rmsDataArrays == null){
                        logUtil.error("  rms网络异常或Cookie失效");
                        return;
                    }
                    if (zabbixDataArrays == null){
                        logUtil.error("  zabbix网络异常或Cookie失效");
                        return;
                    }
                    logUtil.info("  测试获取数据成功,正在启动监控");

                    timer.start();
                    logUtil.info("  监控运行中, 当前每 " + delay/1000 + "s 监控一次");
                    runButton.setText("正在运行");
                    statusLabel.setText("状态：定时器已启动,等待触发");
                }else {
                    timer.stop();
                    logUtil.info("  监控暂停");
                    runButton.setText("开始");
                    statusLabel.setText("状态：无");
                }

            });




        }
}
