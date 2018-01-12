package cn.itcast.erp.dao;

import cn.itcast.erp.entity.Menu;

import java.util.List;

/**
 * 菜单数据访问接口
 * @author Administrator
 *
 */
public interface IMenuDao extends IBaseDao<Menu>{

    List<Menu> getMunesByEmpuuid(Long uuid);

}