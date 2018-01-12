package cn.itcast.erp.job;

import cn.itcast.erp.biz.utils.MailUtil;
import cn.itcast.erp.dao.impl.StoredetailDao;
import cn.itcast.erp.entity.Storealert;

import javax.mail.MessagingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MailJob {
    private StoredetailDao storedetailDao;
    private MailUtil mailUtil;

    private String to;
    private String subject;
    private String text;

    public void setStoredetailDao(StoredetailDao storedetailDao) {
        this.storedetailDao = storedetailDao;
    }

    public void setMailUtil(MailUtil mailUtil) {
        this.mailUtil = mailUtil;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void doJob(){

        List<Storealert> list = storedetailDao.getStorealertList();
        SimpleDateFormat slf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        if (list != null && list.size() > 0) {

            try {
                String reTime = subject.replace("[time]",slf.format(new Date()));
                String reCount = text.replace("[count]",  "<font color='red'>"+list.size()+"</font>").replace("[a]","");
                reCount += text.substring(text.indexOf("[a]")).replace("[a]", "<a href='http://localhost:8080/erp/login.html'>ERP系统查看详情</a>").replace("[count]","");
                mailUtil.sendMail(to,reTime,reCount);
                System.out.println("发送成功");
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
    }
}
