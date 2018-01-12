package com.itheima.redsun.biz.impl;
import com.itheima.redsun.biz.IWaybilldetailBiz;
import com.itheima.redsun.dao.IWaybilldetailDao;
import com.itheima.redsun.entity.Waybilldetail;
/**
 * 业务逻辑类
 * @author Administrator
 *
 */
public class WaybilldetailBiz extends BaseBiz<Waybilldetail> implements IWaybilldetailBiz {

	private IWaybilldetailDao waybilldetailDao;
	
	public void setWaybilldetailDao(IWaybilldetailDao waybilldetailDao) {
		this.waybilldetailDao = waybilldetailDao;
		super.setBaseDao(this.waybilldetailDao);
	}
	
}
