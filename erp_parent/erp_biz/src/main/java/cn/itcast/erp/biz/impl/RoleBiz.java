package cn.itcast.erp.biz.impl;
import cn.itcast.erp.biz.IRoleBiz;
import cn.itcast.erp.dao.IMenuDao;
import cn.itcast.erp.dao.IRoleDao;
import cn.itcast.erp.entity.Menu;
import cn.itcast.erp.entity.Role;
import cn.itcast.erp.entity.Tree;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色业务逻辑类
 * @author Administrator
 *
 */
public class RoleBiz extends BaseBiz<Role> implements IRoleBiz {

	private IRoleDao roleDao;
	private IMenuDao menuDao;

	public void setMenuDao(IMenuDao menuDao) {
		this.menuDao = menuDao;
	}

	public void setRoleDao(IRoleDao roleDao) {
		this.roleDao = roleDao;
		super.setBaseDao(this.roleDao);
	}

	public List<Tree> getTree(long id){
//		此时的参数id是角色的id

		Tree tree = null;

		List<Tree> result = new ArrayList<>();

		Menu menu = menuDao.get("0");
		Role role = roleDao.get(id);

		List<Menu> list = menu.getMenus();//表示该菜单下面还有菜单
		if (list.size() > 0) {
			for (Menu menu1 : list) {

				tree = new Tree();
				tree.setId(menu1.getMenuid());
				tree.setText(menu1.getMenuname());
				tree.setChildren(new ArrayList<Tree>());

				if (menu1.getMenus() != null) {

					for (Menu menu2 : menu1.getMenus()) {
						Tree tree1 = new Tree();
						tree1.setId(menu2.getMenuid());
						tree1.setText(menu2.getMenuname());

						if (role.getMenus().contains(menu2)) {
							tree1.setChecked(true);
						}

						tree.getChildren().add(tree1);
					}
				}
				result.add(tree);
			}
		}
		return result;
	}

	@Override
	public void updateRoleMenus(long id, String ids) {
		Role role = roleDao.get(id);
		role.setMenus(new ArrayList<Menu>());

		String[] strings = ids.split(",");
		for (String sid : strings) {
			Menu menu = menuDao.get(sid);
			role.getMenus().add(menu);
		}
	}
}
