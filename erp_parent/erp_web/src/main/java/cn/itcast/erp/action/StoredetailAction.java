package cn.itcast.erp.action;
import cn.itcast.erp.biz.IStoredetailBiz;
import cn.itcast.erp.biz.utils.ErpException;
import cn.itcast.erp.entity.Storealert;
import cn.itcast.erp.entity.Storedetail;
import cn.itcast.erp.utils.ActionUtil;
import com.alibaba.fastjson.JSON;

import javax.swing.*;
import java.util.List;

/**
 * 仓库库存Action 
 * @author Administrator
 *
 */
public class StoredetailAction extends BaseAction<Storedetail> {

	private IStoredetailBiz storedetailBiz;

	public void setStoredetailBiz(IStoredetailBiz storedetailBiz) {
		this.storedetailBiz = storedetailBiz;
		super.setBaseBiz(this.storedetailBiz);
	}

	public void getStorealertList(){
		try {
			List<Storealert> list = storedetailBiz.getStorealertList();
			ActionUtil.write(JSON.toJSONString(list));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendStorealert(){
		try {
			storedetailBiz.sendStorealertMail();
			ActionUtil.ajaxReturn(true,"发送邮件成功!");
		}catch (ErpException e){
			ActionUtil.ajaxReturn(false,e.getMessage());
		}
		catch (Exception e) {
			e.printStackTrace();
			ActionUtil.ajaxReturn(false,"发送邮件异常!");
		}
	}
}
