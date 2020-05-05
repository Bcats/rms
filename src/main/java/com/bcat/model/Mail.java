package com.bcat.model;

import com.oracle.webservices.internal.api.databinding.DatabindingMode;
import lombok.Data;

@Data
public class Mail {

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

    public Mail(String receiver, String title, String content){

        this.receiver = receiver;
        this.sender = "1905985427@qq.com";
        this.host = "smtp.qq.com";
        this.mailUser = "1905985427@qq.com";
        this.mailPwd = "hfjkkoptanfmgfej";
        this.title = title;
        this.content = content;

    }

}
