/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gbc.gateway.common;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import javax.mail.BodyPart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

/**
 *
 * @author tamvh
 */
public class GoogleEmail {
    public static int sendEmail(String emailReceive, String emailCC, String subj, String mess) {
        int errCode = 0;
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", AppConst.GMAIL_HOST);
        props.put("mail.smtp.port", AppConst.GMAIL_PORT);

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(AppConst.GMAIL_USER, AppConst.GMAIL_PWD);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            MimeMultipart multipart = new MimeMultipart("related");

            message.setFrom(new InternetAddress(AppConst.GMAIL_USER));
            message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(emailReceive));            
            message.setRecipients(Message.RecipientType.CC,
					InternetAddress.parse(emailCC));            
            message.setSubject(subj);
            message.setContent(mess, "text/html; charset=utf-8");
            Transport.send(message);
        } catch (MessagingException e) {
            errCode = 1;
        }
        
        return errCode;
    }
}
