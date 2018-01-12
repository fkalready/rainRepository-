package cn.itcast.erp.action;
import cn.itcast.erp.biz.IEmpBiz;
import cn.itcast.erp.biz.impl.EmpBiz;
import cn.itcast.erp.biz.utils.ErpException;
import cn.itcast.erp.entity.Emp;
import cn.itcast.erp.entity.Tree;
import cn.itcast.erp.utils.ActionUtil;
import com.alibaba.fastjson.JSON;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.web.util.WebUtils;
import org.apache.struts2.ServletActionContext;
import sun.security.util.Password;

import java.util.List;
import java.util.UUID;

/**
 * 员工Action 
 * @author Administrator
 *
 */
public class EmpAction extends BaseAction<Emp> {

	private IEmpBiz empBiz;

	private String oldPwd;
	private String newPwd;
	private Long uuid;

	public void setNewPwd(String newPwd) {
		this.newPwd = newPwd;
	}

	public void setOldPwd(String oldPwd) {
		this.oldPwd = oldPwd;
	}

	public void setUuid(Long uuid) {
		this.uuid = uuid;
	}

	public void setEmpBiz(IEmpBiz empBiz) {
		this.empBiz = empBiz;
		super.setBaseBiz(this.empBiz);
	}

	public void updatePwd(){
		Emp user = (Emp) ServletActionContext.getRequest().getSession().getAttribute("user");
		if (user != null){
				try {
					empBiz.updatePwd(newPwd,oldPwd, user);
					ActionUtil.ajaxReturn(true,newPwd);
				}catch(ErpException e){
					e.printStackTrace();
					ActionUtil.ajaxReturn(false,e.getMessage());
			}
				catch (Exception e) {
					e.printStackTrace();
					ActionUtil.ajaxReturn(false,"修改失败!");
				}
		}
	}

	public void resetPwd(){

		try {
			empBiz.resetPwd(newPwd,uuid);
			ActionUtil.ajaxReturn(true,"修改成功!");
		} catch (Exception e) {
			e.printStackTrace();
			ActionUtil.ajaxReturn(false,"修改失败!");
		}
	}

	public void getRole(){

		List<Tree> trees = empBiz.getRole(getId());
		ActionUtil.write(JSON.toJSONString(trees));
	}


	private String ids;

	public void setIds(String ids) {
		this.ids = ids;
	}

	public void updateEmpRoles(){
		try {
			empBiz.updateEmpRoles(getId(),ids);
			ActionUtil.ajaxReturn(true, "更新成功!");
		} catch (Exception e) {
			e.printStackTrace();
			ActionUtil.ajaxReturn(true, "更新失败!");
		}
	}
}
