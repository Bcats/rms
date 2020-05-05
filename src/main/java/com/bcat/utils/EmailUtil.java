package com.bcat.utils;

import com.bcat.model.Mail;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class EmailUtil {

    public static boolean sendEmail(final Mail mail){

        Properties properties = System.getProperties();

        properties.setProperty("mail.smtp.auth", "true");

        properties.setProperty("mail.smtp.host", mail.getHost());

        Session session = Session.getDefaultInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(mail.getMailUser(), mail.getMailPwd());
            }
        });

        try {

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(mail.getSender()));
            message.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(mail.getReceiver()));
            message.setSubject(mail.getTitle());
            message.setText(mail.getContent());

            Transport.send(message);
            return true;

        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<Boolean> sendEmails(List<Mail> mailList)  {
        List<Boolean> booleanList = new ArrayList<>();

        for (Mail mail : mailList){
            if (sendEmail(mail)?booleanList.add(true):booleanList.add(false));

        }
        return booleanList;
    }
}
