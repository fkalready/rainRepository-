package cn.itcast.erp.dao.impl;

import cn.itcast.erp.dao.IReportDao;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ReportDao extends HibernateDaoSupport implements IReportDao {

    public List orderReport(Date startDate, Date endDate) {
        List<Date> listParam = new ArrayList<>();

        String hql = "select new map(gs.name as name,sum(od.money) as y) " +
                "from Orders o,Orderdetail od,Goodstype gs,Goods g " +
                "where od.orders=o and o.type=2 and g.goodstype=gs and od.goodsuuid=g.uuid ";

        if (startDate != null) {
            hql += "and o.createtime >= ? ";
            listParam.add(startDate);
        }
        if (endDate != null) {
            hql += "and o.createtime <= ? ";
            listParam.add(endDate);
        }

        hql += "group by gs.name";

        return getHibernateTemplate().find(hql, listParam.toArray());
    }


    public List trendReport(int year, int month) {

        String hql = "select new map(month(o.createtime) || '月' as name,sum(od.money) as y) " +
                "from Orders o,Orderdetail od " +
                "where o=od.orders and o.type='2' and year(o.createtime) = ? and month(o.createtime) = ? " +
                "group by extract(month from o.createtime)";

        return getHibernateTemplate().find(hql, year, month);
    }
}
