package cn.itcast.erp.action;
import cn.itcast.erp.biz.IOrderdetailBiz;
import cn.itcast.erp.entity.Orderdetail;

/**
 * 订单明细Action 
 * @author Administrator
 *
 */
public class OrderdetailAction extends BaseAction<Orderdetail> {

	private IOrderdetailBiz orderdetailBiz;

	public void setOrderdetailBiz(IOrderdetailBiz orderdetailBiz) {
		this.orderdetailBiz = orderdetailBiz;
		super.setBaseBiz(this.orderdetailBiz);
	}

}
