package cn.itcast.erp.action;

import cn.itcast.erp.biz.IReportBiz;
import cn.itcast.erp.utils.ActionUtil;
import com.alibaba.fastjson.JSON;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class ReportAction {

    private IReportBiz reportBiz;

    private Date startDate;
    private Date endDate;

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setReportBiz(IReportBiz reportBiz) {
        this.reportBiz = reportBiz;
    }

    public void orderReport() {

        try {
            List list = reportBiz.orderReport(startDate,endDate);

            ActionUtil.write(JSON.toJSONString(list));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int year;

    public void setYear(int year) {
        this.year = year;
    }

    public void trendReport() {

        try {

            List<Map<String, Object>> list = reportBiz.trendReport(year);
            ActionUtil.write(JSON.toJSONString(list));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
