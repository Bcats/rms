package com.bcat.model;

import com.bcat.utils.TimeUtil;
import lombok.Data;

import java.util.Map;

@Data
public class MailModel {

    // 收件人
    private String receiver;
    // 发件人
    private String sender;
    // smtp服务器地址
    private String host;
    // 发件邮箱账号
    private String mailUser;
    // 发件邮箱授权码
    private String mailPwd;
    // 邮件标题
    private String title;
    // 邮件内容
    private String content;

    private MailModel(){};

    public MailModel(String receiver){
        this.receiver = receiver;
        this.sender = "1905985427@qq.com";
        this.host = "smtp.qq.com";
        this.mailUser = "1905985427@qq.com";
        this.mailPwd = "hfjkkoptanfmgfej";
    }

    public MailModel(String receiver, String title, String content){
        this(receiver);
        this.title = title;
        this.content = content;

    }

    public MailModel setRmsBusinessMail(Map<String,String> map){

        this.title = "[" + TimeUtil.getHourByDate(map.get("alarmTime")) + "点][rms业务][" + map.get("level") + "]" + map.get("projectName");
        this.content = "id：" + map.get("id") + "\n" +
                    "项目名称：" + map.get("projectName") + "\n" +
                    "监控点名称：" + map.get("alarmPointName") + "\n" +
                    "错误说明：" + map.get("errorInfo") + "\n" +
                    "级别：" + map.get("level") + "\n" +
                    "告警时间：" + map.get("alarmTime") + "\n"+
                    "告警链接：" + map.get("alarmDetailUrl");
        return this;
    }

    public MailModel setRmsServerMail(Map<String,String> map){

        this.title = "[" + TimeUtil.getHourByDate(map.get("alarmTime")) + "点][rms服务器][" + map.get("level") + "]" + map.get("businessName");
        this.content = "id：" + map.get("id") + "\n" +
                "业务名称：" + map.get("businessName") + "\n" +
                "Metric：" + map.get("metric") + "\n" +
                "Tag：" + map.get("tag") + "\n" +
                "级别：" + map.get("level") + "\n" +
                "告警时间：" + map.get("alarmTime") + "\n"+
                "告警链接：" + map.get("alarmDetailUrl");
        return this;
    }


    public MailModel setZabbixMailMail(Map<String,String> map){
       this.title = "[" + map.get("time") + "]zabbix有一条红色告警超10分钟未消除";
       this.content = "主机：" + map.get("host") + "\n" +
               "问题：" + map.get("question") + "\n" +
               "等级：深红(严重)\n" +
               "发生时间：" + map.get("time");

        return this;
    }

//    public static String rmsBusinessMailTitle(Map<String,String> map) {
//       return "[" + TimeUtil.getHourByDate(map.get("alarmTime")) + "点][rms业务][" + map.get("level") + "]" + map.get("projectName");
//    }
//
//    public static String rmsBusinessMailContent(Map<String,String> map){
//        return  "id：" + map.get("id") + "\n" +
//                "项目名称：" + map.get("projectName") + "\n" +
//                "监控点名称：" + map.get("alarmPointName") + "\n" +
//                "错误说明：" + map.get("errorInfo") + "\n" +
//                "级别：" + map.get("level") + "\n" +
//                "告警时间：" + map.get("alarmTime") + "\n"+
//                "告警链接：" + map.get("alarmDetailUrl");
//    }

//    public static String rmsServerMailContent(Map<String,String> map){
//        return  "id：" + map.get("id") + "\n" +
//                "业务名称：" + map.get("businessName") + "\n" +
//                "Metric：" + map.get("metric") + "\n" +
//                "Tag：" + map.get("tag") + "\n" +
//                "级别：" + map.get("level") + "\n" +
//                "告警时间：" + map.get("alarmTime") + "\n"+
//                "告警链接：" + map.get("alarmDetailUrl");
//    }

}
