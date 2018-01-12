package cn.itcast.erp.dao;

import java.util.Date;
import java.util.List;

public interface IReportDao {
    List orderReport(Date startDate, Date endDate);

    List trendReport(int year, int month);
}
