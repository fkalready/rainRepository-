package cn.itcast.erp.dao.impl;
import org.hibernate.criterion.DetachedCriteria;
import cn.itcast.erp.dao.IMenuDao;
import cn.itcast.erp.entity.Menu;

import java.util.ArrayList;
import java.util.List;

/**
 * 菜单数据访问类
 * @author Administrator
 *
 */
public class MenuDao extends BaseDao<Menu> implements IMenuDao {

	/**
	 * 构建查询条件
	 * @param dep1
	 * @param dep2
	 * @param param
	 * @return
	 */
	public DetachedCriteria getDetachedCriteria(Menu menu1,Menu menu2,Object param){

		DetachedCriteria dc=DetachedCriteria.forClass(Menu.class);
		return dc;
	}

	public List<Menu> getMunesByEmpuuid(Long uuid) {

		String hql = "select m from Emp e join e.roles r join r.menus m where e.uuid=?";

		return (List<Menu>) getHibernateTemplate().find(hql, uuid);
	}

}
