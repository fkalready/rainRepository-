package cn.itcast.erp.biz;
import cn.itcast.erp.entity.Role;
import cn.itcast.erp.entity.Tree;

import java.util.List;

/**
 * 角色业务逻辑层接口
 * @author Administrator
 *
 */
public interface IRoleBiz extends IBaseBiz<Role>{

    List<Tree> getTree(long id);

    void updateRoleMenus(long id, String ids);
}

