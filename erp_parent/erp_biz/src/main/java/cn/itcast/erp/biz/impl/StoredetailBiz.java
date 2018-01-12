package cn.itcast.erp.biz.impl;
import cn.itcast.erp.biz.IStoredetailBiz;
import cn.itcast.erp.biz.utils.ErpException;
import cn.itcast.erp.biz.utils.MailUtil;
import cn.itcast.erp.dao.*;
import cn.itcast.erp.dao.impl.StoreDao;
import cn.itcast.erp.entity.*;

import javax.mail.MessagingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 仓库库存业务逻辑类
 * @author Administrator
 *
 */
public class StoredetailBiz extends BaseBiz<Storedetail> implements IStoredetailBiz {

	private IStoredetailDao storedetailDao;
	private IStoreDao storeDao;
	private IGoodsDao goodsDao;
	private MailUtil mailUtil;

	private String to;
	private String subject;
	private String text;

	public void setTo(String to) {
		this.to = to;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setMailUtil(MailUtil mailUtil) {
		this.mailUtil = mailUtil;
	}

	public void setStoreDao(IStoreDao storeDao) {
		this.storeDao = storeDao;
	}

	public void setGoodsDao(IGoodsDao goodsDao) {
		this.goodsDao = goodsDao;
	}

	public void setStoredetailDao(IStoredetailDao storedetailDao) {
		this.storedetailDao = storedetailDao;
		super.setBaseDao(this.storedetailDao);
	}

	@Override
	public List<Storedetail> getListByPage(Storedetail t1, Storedetail t2, Object param, int firstResult, int maxResults) {
		List<Storedetail> list = storedetailDao.getListByPage(t1, t2, param, firstResult, maxResults);
		if (list != null){
			for (Storedetail storedetail : list) {
				Store store = storeDao.get(storedetail.getStoreuuid());
				storedetail.setStoreName(store.getName());

				Goods goods = goodsDao.get(storedetail.getGoodsuuid());
				storedetail.setGoodsName(goods.getName());
			}
		}
		return list;
	}

	@Override
	public List<Storealert> getStorealertList() {
		return storedetailDao.getStorealertList();
	}

	@Override
	public void sendStorealertMail() throws MessagingException {

		List<Storealert> list = storedetailDao.getStorealertList();
		SimpleDateFormat slf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		if (list != null && list.size() > 0) {
			String reTime = subject.replace("[time]",slf.format(new Date()));
			String reCount = text.replace("[count]",  "<font color='red'>"+list.size()+"</font>").replace("[a]","");
			reCount += text.substring(text.indexOf("[a]")).replace("[a]", "<a href='http://localhost:8080/erp/login.html'>ERP系统查看详情</a>").replace("[count]","");
			mailUtil.sendMail(to,reTime,reCount);

		}else {
			throw new ErpException("目前库存情况良好!");
		}
	}
}
