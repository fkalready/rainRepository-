package cn.itcast.erp.action;
import cn.itcast.erp.biz.IOrdersBiz;
import cn.itcast.erp.biz.utils.ErpException;
import cn.itcast.erp.entity.Emp;
import cn.itcast.erp.entity.Orderdetail;
import cn.itcast.erp.entity.Orders;
import cn.itcast.erp.utils.ActionUtil;
import com.alibaba.fastjson.JSON;
import com.redsun.bos.ws.Waybilldetail;
import com.redsun.bos.ws.impl.IWaybillWs;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import java.io.IOException;
import java.util.List;

/**
 * 订单Action 
 * @author Administrator
 *
 */
public class OrdersAction extends BaseAction<Orders> {

	private IOrdersBiz ordersBiz;

	public void setOrdersBiz(IOrdersBiz ordersBiz) {
		this.ordersBiz = ordersBiz;
		super.setBaseBiz(this.ordersBiz);
	}

	private String jsonString;

	public void setJsonString(String jsonString) {
		this.jsonString = jsonString;
	}

	@Override
	public void add() {
		//[{"price":"1.11","num":"100","money":"111.00","goodsuuid":"2","goodsname":"大鸭梨"}]

		Emp user = (Emp) ServletActionContext.getRequest().getSession().getAttribute("user");

		Orders orders = getT();

		if (user == null) {
			ActionUtil.ajaxReturn(false,"亲,您还没登录哟!");
			return;
		}
		try {
			List<Orderdetail> orderdetails = JSON.parseArray(jsonString, Orderdetail.class);
			orders.setOrderdetails(orderdetails);

			orders.setCreater(user.getUuid());


			ordersBiz.add(orders);
			ActionUtil.ajaxReturn(true,"新增成功!");
		} catch (Exception e) {
			e.printStackTrace();
			ActionUtil.ajaxReturn(false,"新增失败!");
		}
	}

	public void doCheck(){
		Emp loginEmp = ActionUtil.getLoginEmp();
		if (loginEmp == null) {
			ActionUtil.ajaxReturn(false,"亲,您还没登录!");
			return;
		}
		try {
			ordersBiz.doCheck(getId(), loginEmp.getUuid());
			ActionUtil.ajaxReturn(true, "审核完成!");
		} catch (ErpException e) {
			ActionUtil.ajaxReturn(false,e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			ActionUtil.ajaxReturn(false, "审核失败!");
		}
	}

	public void doStart(){
		Emp loginEmp = ActionUtil.getLoginEmp();
		if (loginEmp == null) {
			ActionUtil.ajaxReturn(false,"亲,您还没登录!");
			return;
		}
		try {
			ordersBiz.doStart(getId(), loginEmp.getUuid());
			ActionUtil.ajaxReturn(true, "确认订单成功!");
		} catch (ErpException e) {
			ActionUtil.ajaxReturn(false,e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			ActionUtil.ajaxReturn(false, "确认订单失败!");
		}
	}

	private Long storeuuid;

	public void setStoreuuid(Long storeuuid) {
		this.storeuuid = storeuuid;
	}

	public void doInStore(){

		Emp loginEmp = ActionUtil.getLoginEmp();
		if (loginEmp == null) {
			ActionUtil.ajaxReturn(false,"亲,您还没登录!");
			return;
		}
		try {
			ordersBiz.doInStore(storeuuid, getId(), loginEmp.getUuid());
			ActionUtil.ajaxReturn(true, "入库成功!");
		} catch (ErpException e) {
			ActionUtil.ajaxReturn(false, e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			ActionUtil.ajaxReturn(false, "入库失败!");
		}
	}

	public void doOutStore(){

		Emp loginEmp = ActionUtil.getLoginEmp();
		if (loginEmp == null) {
			ActionUtil.ajaxReturn(false,"亲,您还没登录!");
			return;
		}
		try {
			ordersBiz.doOutStore(storeuuid, getId(), loginEmp.getUuid());
			ActionUtil.ajaxReturn(true, "出库成功!");
		} catch (ErpException e) {
			ActionUtil.ajaxReturn(false, e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			ActionUtil.ajaxReturn(false, "出库失败!");
		}
	}

	public void myListByPage() {
		Emp loginEmp = ActionUtil.getLoginEmp();
		if (getT1() == null){
			this.setT1(new Orders());
		}
		getT1().setCreater(loginEmp.getUuid());
		super.listByPage();
	}

	public void exportById() {

		HttpServletResponse response = ServletActionContext.getResponse();

		try {

			String file = new String("号订单明细".getBytes(), "iso-8859-1");

			response.setHeader("Content-Disposition","attachment;filename="+getId()+file+".xls");
			ordersBiz.exportById(response.getOutputStream(), getId());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Long sn;

	public void setSn(Long sn) {
		this.sn = sn;
	}

	private IWaybillWs waybillWS;

	public void setWaybillWS(IWaybillWs waybillWS) {
		this.waybillWS = waybillWS;
	}

	public void waybilldetailList(){

		List<Waybilldetail> list = waybillWS.waybilldetailList(sn);

		ActionUtil.write(JSON.toJSONString(list));

	}
}
