package cn.itcast.erp.biz.impl;
import cn.itcast.erp.biz.IStoreoperBiz;
import cn.itcast.erp.dao.IEmpDao;
import cn.itcast.erp.dao.IGoodsDao;
import cn.itcast.erp.dao.IStoreDao;
import cn.itcast.erp.dao.IStoreoperDao;
import cn.itcast.erp.dao.impl.EmpDao;
import cn.itcast.erp.dao.impl.GoodsDao;
import cn.itcast.erp.dao.impl.StoreDao;
import cn.itcast.erp.entity.Emp;
import cn.itcast.erp.entity.Storeoper;

import java.util.List;

/**
 * 仓库操作记录业务逻辑类
 *
 * @author Administrator
 */
public class StoreoperBiz extends BaseBiz<Storeoper> implements IStoreoperBiz {

	private IStoreoperDao storeoperDao;
	private IEmpDao empDao;
	private IGoodsDao goodsDao;
	private IStoreDao storeDao;

	public void setStoreoperDao(IStoreoperDao storeoperDao) {
		this.storeoperDao = storeoperDao;
		super.setBaseDao(this.storeoperDao);
	}

	public void setEmpDao(IEmpDao empDao) {
		this.empDao = empDao;
	}

	public void setGoodsDao(IGoodsDao goodsDao) {
		this.goodsDao = goodsDao;
	}

	public void setStoreDao(IStoreDao storeDao) {
		this.storeDao = storeDao;
	}

	@Override
	public List<Storeoper> getListByPage(Storeoper t1, Storeoper t2, Object param, int firstResult, int maxResults) {
		List<Storeoper> list = storeoperDao.getListByPage(t1, t2, param, firstResult, maxResults);
		if (list != null) {
			for (Storeoper storeoper : list) {
				storeoper.setEmpName(empDao.get(storeoper.getEmpuuid()).getName());
				storeoper.setGoodsName(goodsDao.get(storeoper.getGoodsuuid()).getName());
				storeoper.setStoreName(storeDao.get(storeoper.getStoreuuid()).getName());
			}
		}
		return list;
	}
}
