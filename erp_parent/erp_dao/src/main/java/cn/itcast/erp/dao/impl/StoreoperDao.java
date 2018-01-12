package cn.itcast.erp.dao.impl;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import cn.itcast.erp.dao.IStoreoperDao;
import cn.itcast.erp.entity.Storeoper;

import java.util.Calendar;

/**
 * 仓库操作记录数据访问类
 * @author Administrator
 *
 */
public class StoreoperDao extends BaseDao<Storeoper> implements IStoreoperDao {

	/**
	 * 构建查询条件
	 * @param dep1
	 * @param dep2
	 * @param param
	 * @return
	 */
	public DetachedCriteria getDetachedCriteria(Storeoper storeoper1,Storeoper storeoper2,Object param){
		DetachedCriteria dc=DetachedCriteria.forClass(Storeoper.class);
		if(storeoper1!=null){
			if(null != storeoper1.getType() && storeoper1.getType().trim().length()>0){
				dc.add(Restrictions.eq("type", storeoper1.getType()));
			}
			if(null != storeoper1.getEmpuuid()){
				dc.add(Restrictions.eq("empuuid", storeoper1.getEmpuuid()));
			}
			if(null != storeoper1.getStoreuuid()){
				dc.add(Restrictions.eq("storeuuid", storeoper1.getStoreuuid()));
			}
			if (null != storeoper1.getGoodsuuid()) {
				dc.add(Restrictions.eq("goodsuuid", storeoper1.getGoodsuuid()));
			}
			if (storeoper1.getOpertime() != null) {
				dc.add(Restrictions.ge("opertime", storeoper1.getOpertime()));
			}
		}
		if (storeoper2 != null) {
			if (storeoper2.getOpertime() != null) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(storeoper2.getOpertime());
				cal.set(Calendar.HOUR, 23);
				cal.set(Calendar.MINUTE, 59);
				cal.set(Calendar.SECOND, 59);
				cal.set(Calendar.MILLISECOND, 999);
//				cal.add(Calendar.DATE,1);
				dc.add(Restrictions.le("opertime", cal.getTime()));
			}
		}
		return dc;
	}

}
