package com.itheima.redsun.biz.impl;
import com.itheima.redsun.biz.IWaybillBiz;
import com.itheima.redsun.dao.IWaybillDao;
import com.itheima.redsun.entity.Waybill;
/**
 * 业务逻辑类
 * @author Administrator
 *
 */
public class WaybillBiz extends BaseBiz<Waybill> implements IWaybillBiz {

	private IWaybillDao waybillDao;
	
	public void setWaybillDao(IWaybillDao waybillDao) {
		this.waybillDao = waybillDao;
		super.setBaseDao(this.waybillDao);
	}
	
}
