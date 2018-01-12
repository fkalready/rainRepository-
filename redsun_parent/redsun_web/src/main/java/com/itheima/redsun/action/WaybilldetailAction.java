package com.itheima.redsun.action;
import com.itheima.redsun.biz.IWaybilldetailBiz;
import com.itheima.redsun.entity.Waybilldetail;

/**
 * Action 
 * @author Administrator
 *
 */
public class WaybilldetailAction extends BaseAction<Waybilldetail> {

	private IWaybilldetailBiz waybilldetailBiz;

	public void setWaybilldetailBiz(IWaybilldetailBiz waybilldetailBiz) {
		this.waybilldetailBiz = waybilldetailBiz;
		super.setBaseBiz(this.waybilldetailBiz);
	}

}
