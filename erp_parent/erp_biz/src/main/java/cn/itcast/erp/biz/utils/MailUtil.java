package cn.itcast.erp.biz.utils;


import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class MailUtil {

    private JavaMailSender mailSender;
    private Properties p;

    public void setP(Properties p) {
        this.p = p;
    }

    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendMail(String to,String subject,String text) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        mimeMessage.addRecipients(MimeMessage.RecipientType.CC, InternetAddress.parse(p.getProperty("userName")));
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true,"utf-8");

        helper.setFrom("wangrui_sn@163.com");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text,true);

        mailSender.send(mimeMessage);
    }
}
