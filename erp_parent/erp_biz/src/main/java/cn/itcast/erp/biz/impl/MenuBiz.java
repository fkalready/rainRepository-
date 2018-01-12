package cn.itcast.erp.biz.impl;
import cn.itcast.erp.biz.IMenuBiz;
import cn.itcast.erp.dao.IMenuDao;
import cn.itcast.erp.entity.Menu;

import java.util.ArrayList;
import java.util.List;

/**
 * 菜单业务逻辑类
 * @author Administrator
 *
 */
public class MenuBiz extends BaseBiz<Menu> implements IMenuBiz {

	private IMenuDao menuDao;
	
	public void setMenuDao(IMenuDao menuDao) {
		this.menuDao = menuDao;
		super.setBaseDao(this.menuDao);
	}

	@Override
	public Menu getMenuTree(String menuid) {
		return menuDao.get(menuid);
	}

	@Override
	public Menu getMunesByEmpuuid(Long uuid) {

		Menu menu = null;

		List<Menu> list = menuDao.getMunesByEmpuuid(uuid);
		Menu allMenu = menuDao.get("0");

		menu = copyMenu(allMenu);
		menu.setMenus(new ArrayList<Menu>());

		Menu m1 = null;
		Menu m2 = null;

		for (Menu menu1 : allMenu.getMenus()) {
			m1 = copyMenu(menu1);//第一级菜单
			m1.setMenus(new ArrayList<Menu>());
			for (Menu menu2 : menu1.getMenus()) {
				if (list.contains(menu2)) {
					m2 = copyMenu(menu2);
					m1.getMenus().add(m2);
				}
			}
			if (m1.getMenus().size() > 0) {
				menu.getMenus().add(m1);
			}
		}

		return menu;
	}

	private Menu copyMenu(Menu menu) {

		Menu remenu = new Menu();

		remenu.setMenuid(menu.getMenuid());
		remenu.setIcon(menu.getIcon());
		remenu.setMenuname(menu.getMenuname());
		remenu.setUrl(menu.getUrl());

		return remenu;
	}
}
