package com.bcat.utils;

import com.bcat.model.MailModel;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public final class EmailUtil {

    public static boolean sendEmail(final MailModel mailModel){

        Properties properties = System.getProperties();

        properties.setProperty("mail.smtp.auth", "true");

        properties.setProperty("mail.smtp.host", mailModel.getHost());

        Session session = Session.getDefaultInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(mailModel.getMailUser(), mailModel.getMailPwd());
            }
        });

        try {

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(mailModel.getSender()));
            message.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(mailModel.getReceiver()));
            message.setSubject(mailModel.getTitle());
            message.setText(mailModel.getContent());

            Transport.send(message);
            return true;

        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<Boolean> sendEmails(List<MailModel> mailModelList)  {
        List<Boolean> booleanList = new ArrayList<>();

        for (MailModel mailModel : mailModelList){
            if (sendEmail(mailModel) ? booleanList.add(true) : booleanList.add(false));
        }
        return booleanList;
    }
}
