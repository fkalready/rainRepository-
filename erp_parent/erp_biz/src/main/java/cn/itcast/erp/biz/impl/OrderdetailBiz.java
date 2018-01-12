package cn.itcast.erp.biz.impl;
import cn.itcast.erp.biz.IOrderdetailBiz;
import cn.itcast.erp.dao.IOrderdetailDao;
import cn.itcast.erp.entity.Orderdetail;
/**
 * 订单明细业务逻辑类
 * @author Administrator
 *
 */
public class OrderdetailBiz extends BaseBiz<Orderdetail> implements IOrderdetailBiz {

	private IOrderdetailDao orderdetailDao;
	
	public void setOrderdetailDao(IOrderdetailDao orderdetailDao) {
		this.orderdetailDao = orderdetailDao;
		super.setBaseDao(this.orderdetailDao);
	}
	
}
