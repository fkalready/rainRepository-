package cn.itcast.erp.action;
import cn.itcast.erp.biz.IMenuBiz;
import cn.itcast.erp.entity.Emp;
import cn.itcast.erp.entity.Menu;
import cn.itcast.erp.utils.ActionUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.List;

/**
 * 菜单Action 
 * @author Administrator
 *
 */
public class MenuAction extends BaseAction<Menu> {

	private IMenuBiz menuBiz;

	public void setMenuBiz(IMenuBiz menuBiz) {
		this.menuBiz = menuBiz;
		super.setBaseBiz(this.menuBiz);
	}

	public void getMenuTree(){

		Menu menuTree = menuBiz.getMenuTree("0");

		ActionUtil.write(JSON.toJSONString(menuTree));
	}

	public void getMunesByEmpuuid() {

		Emp loginEmp = ActionUtil.getLoginEmp();

		Menu munes = menuBiz.getMunesByEmpuuid(loginEmp.getUuid());
		ActionUtil.write(JSON.toJSONString(munes));

	}
}
