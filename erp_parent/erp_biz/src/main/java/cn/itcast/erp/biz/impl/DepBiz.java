package cn.itcast.erp.biz.impl;
import cn.itcast.erp.biz.IBaseBiz;
import cn.itcast.erp.biz.IDepBiz;
import cn.itcast.erp.biz.IEmpBiz;
import cn.itcast.erp.biz.utils.ErpException;
import cn.itcast.erp.dao.IDepDao;
import cn.itcast.erp.dao.IEmpDao;
import cn.itcast.erp.dao.impl.EmpDao;
import cn.itcast.erp.dao.impl.SupplierDao;
import cn.itcast.erp.entity.Dep;
import cn.itcast.erp.entity.Emp;
import cn.itcast.erp.entity.Supplier;

import java.io.OutputStream;

/**
 * 部门业务逻辑类
 * @author Administrator
 *
 */
public class DepBiz extends BaseBiz<Dep> implements IDepBiz {

	private IDepDao depDao;
    private IEmpDao empDao;

	public void setDepDao(IDepDao depDao) {
		this.depDao = depDao;
		super.setBaseDao(this.depDao);
	}

    public void setEmpDao(IEmpDao empDao) {
        this.empDao = empDao;
    }

    public void delete(Long uuid){
        Emp emp = new Emp();
        emp.setDep(new Dep());
        emp.getDep().setUuid(uuid);

        long count = empDao.getCount(emp, null, null);
        if (count > 0) {
            throw new ErpException("该部门下面有员工,不能删除!");
        }
        depDao.delete(uuid);
	}
}
