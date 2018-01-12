package cn.itcast.erp.biz.impl;
import cn.itcast.erp.biz.IEmpBiz;
import cn.itcast.erp.biz.utils.ErpException;
import cn.itcast.erp.dao.IEmpDao;
import cn.itcast.erp.dao.IRoleDao;
import cn.itcast.erp.entity.Emp;
import cn.itcast.erp.entity.Role;
import cn.itcast.erp.entity.Tree;
import org.apache.shiro.crypto.hash.Md5Hash;

import java.util.ArrayList;
import java.util.List;

/**
 * 员工业务逻辑类
 * @author Administrator
 *
 */
public class EmpBiz extends BaseBiz<Emp> implements IEmpBiz {

	private IEmpDao empDao;
	private IRoleDao roleDao;

	public void setRoleDao(IRoleDao roleDao) {
		this.roleDao = roleDao;
	}

	public void setEmpDao(IEmpDao empDao) {
		this.empDao = empDao;
		super.setBaseDao(this.empDao);
	}

	private String encrypt(String source,String salt){
		Md5Hash md5Hash = new Md5Hash(source, salt,2);
		return md5Hash.toString();
	}

	@Override
	public Emp findByUsernameAndPwd(String username, String pwd) {
		String md5Pwd = encrypt(pwd, username);
		return empDao.findByUsernameAndPwd(username, md5Pwd);
	}

	@Override
	public void updatePwd(String newPwd, String oldPwd, Emp user) {
		String md5Pwd = encrypt(newPwd, user.getUsername());
		String md5Old = encrypt(oldPwd,user.getUsername());
		if(user.getPwd().equals(md5Old)) {
			empDao.updatePwd(md5Pwd, user.getUuid());
		}else{
			throw new ErpException("旧密码错误");
		}
	}

	@Override
	public void add(Emp emp) {

		String source = emp.getUsername();
		String md5Pwd = encrypt(source, emp.getUsername());
		emp.setPwd(md5Pwd);

		super.add(emp);
	}

	@Override
	public void update(Emp emp) {
		String source = emp.getPwd();
		if (source != null) {
			String md5Pwd = encrypt(source, emp.getUsername());
			emp.setPwd(md5Pwd);
		}
		super.update(emp);
	}

	@Override
	public void resetPwd(String pwd,Long uuid){
		Emp emp = empDao.get(uuid);
		String md5Pwd = encrypt(pwd, emp.getUsername());
		empDao.updatePwd(md5Pwd,uuid);
	}

	@Override
	public List<Tree> getRole(Long id) {
		List<Tree> result = new ArrayList<>();
		Emp emp = empDao.get(id);
		List<Role> list = roleDao.getList(null, null, null);
		Tree tree = null;
		if (list != null) {
			for (Role role : list) {
				tree = new Tree();
				tree.setText(role.getName());
				tree.setId(role.getUuid()+"");

				if (emp.getRoles().contains(role)) {
					tree.setChecked(true);
				}
				result.add(tree);
			}
		}
		return result;
	}

	@Override
	public void updateEmpRoles(Long id, String ids) {

		Emp emp = get(id);
		emp.setRoles(new ArrayList<Role>());
		String[] strings = ids.split(",");
		for (String sid : strings) {
			emp.getRoles().add(roleDao.get(Long.parseLong(sid)));
		}
	}
}
