package cn.itcast.erp.action;
import cn.itcast.erp.biz.IRoleBiz;
import cn.itcast.erp.entity.Role;
import cn.itcast.erp.entity.Tree;
import cn.itcast.erp.utils.ActionUtil;
import com.alibaba.fastjson.JSON;

import java.util.List;

/**
 * 角色Action 
 * @author Administrator
 *
 */
public class RoleAction extends BaseAction<Role> {

	private IRoleBiz roleBiz;

	public void setRoleBiz(IRoleBiz roleBiz) {
		this.roleBiz = roleBiz;
		super.setBaseBiz(this.roleBiz);
	}

	public void getTree(){

		List<Tree> trees = roleBiz.getTree(getId());
		ActionUtil.write(JSON.toJSONString(trees));

	}

	private String ids;

	public void setIds(String ids) {
		this.ids = ids;
	}

	public void updateRoleMenus(){

		try {
			roleBiz.updateRoleMenus(getId(),ids);
			ActionUtil.ajaxReturn(true, "更新成功!");
		} catch (Exception e) {
			e.printStackTrace();
			ActionUtil.ajaxReturn(false, "更新失败!");
		}
	}
}
