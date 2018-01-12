package cn.itcast.erp.biz;

import java.util.Date;
import java.util.List;

public interface IReportBiz {
    List orderReport(Date startDate, Date endDate);

    List trendReport(int year);
}
