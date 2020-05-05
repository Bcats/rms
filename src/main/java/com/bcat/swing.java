package com.bcat;

import com.bcat.model.Mail;
import com.bcat.utils.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
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
            // 监控定时器
            final Timer timer = new Timer(delay, (e) -> {
                // 首次测试登录
                String cookie = cookieText.getText();
                String result = RmsUtil.login(cookie);
                String emailReceiver = emailText.getText();
                if (result == null){
                    logUtil.error("  网络异常或Cookie失效");
                    return;
                }
                // 当前监控时间段报时
                if (bufferHour.get() != Calendar.getInstance().get(Calendar.HOUR_OF_DAY)){
                    bufferHour.set(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
                    logUtil.info("  -------------  "+ bufferHour.get() + "点  -------------");
                }
                // 开始获取需要发送的数据
                List<List<String>> dataArrays = MyStringUtil.parseStr(result);
                List<List<String>> sendDataArrays = JudgeUtil.checkSend(dataArrays);
                /*if (sendDataArrays.size() != 0){
                    logUtil.info("  共获取 " + dataArrays.size() + " 条数据, " +
                            "需发送 " + sendDataArrays.size() + " 条数据");
                }*/

                // 开始发送告警邮件
                List<Mail> mailList = new ArrayList<>();
                for (List<String> sdArray : sendDataArrays){
                    String id = sdArray.get(0);
                    String dataTime = sdArray.get(5);
                    String level = sdArray.get(4);
                    sdArray.set(12, RmsUtil.getAlarmUrlById(id, cookie));
                    String title = "[" + TimeUtil.getHourByDate(dataTime) + "点][rms][" + sdArray.get(4) + "]" + sdArray.get(1);
                    String content = sdArray.toString();
                    mailList.add(new Mail(emailReceiver, title, content));
                    logUtil.info("  发送一条 [ " + level + " ] 告警至邮箱 ");
                }
                List<Boolean> booleanList = EmailUtil.sendEmails(mailList);

                // 调用状态label更新
                statusLabel.setText("状态：第 " + count.incrementAndGet() + " 次调用,时间："+ TimeUtil.getDayTime());
            });

            // 开始按钮事件
            runButton.addActionListener((e) -> {

                if (runButton.getText().equals("开始")){
                    // 开始首先检测Cookie
                    String cookie = cookieText.getText();
                    String result = RmsUtil.login(cookie);
                    if (result == null){
                        logUtil.error("  网络异常或Cookie失效");
                        return;
                    }
                    logUtil.info("  模拟登录成功,正在启动监控");

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
