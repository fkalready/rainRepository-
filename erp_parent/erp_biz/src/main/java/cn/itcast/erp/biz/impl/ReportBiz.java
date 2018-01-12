package cn.itcast.erp.biz.impl;

import cn.itcast.erp.biz.IReportBiz;
import cn.itcast.erp.dao.IReportDao;

import java.util.*;

public class ReportBiz implements IReportBiz {
    private IReportDao reportDao;

    public void setReportDao(IReportDao reportDao) {
        this.reportDao = reportDao;
    }

    public List orderReport(Date startDate, Date endDate) {
        return reportDao.orderReport(startDate, endDate);
    }

    @Override
    public List<Map<String, Object>> trendReport(int year) {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        for (int i = 1; i <= 12; i++) {
            List<Map<String, Object>> list = reportDao.trendReport(year, i);

            if (list != null && list.size() > 0) {
                result.add(list.get(0));
            }else {
                Map<String, Object> map = new HashMap<>();
                map.put("name", i+"月");
                map.put("y", 0);
                result.add(map);
            }
        }
        return result;
    }
}
