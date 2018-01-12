package cn.itcast.erp.biz;
import cn.itcast.erp.entity.Emp;
import cn.itcast.erp.entity.Tree;

import java.util.List;

/**
 * 员工业务逻辑层接口
 * @author Administrator
 *
 */
public interface IEmpBiz extends IBaseBiz<Emp>{
    Emp findByUsernameAndPwd(String username, String pwd);

    void updatePwd(String pwd, String newPwd, Emp uuid);

    /**
     * 重置密码
     */
    void resetPwd(String pwd,Long uuid);

    List<Tree> getRole(Long id);

    void updateEmpRoles(Long id, String ids);
}

