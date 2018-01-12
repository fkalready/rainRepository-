package cn.itcast.erp.biz.impl;

import cn.itcast.erp.biz.IOrdersBiz;
import cn.itcast.erp.biz.utils.ErpException;
import cn.itcast.erp.dao.*;
import cn.itcast.erp.entity.*;
import com.redsun.bos.ws.Waybilldetail;
import com.redsun.bos.ws.impl.IWaybillWs;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

/**
 * 订单业务逻辑类
 *
 * @author Administrator
 */
public class OrdersBiz extends BaseBiz<Orders> implements IOrdersBiz {

    private IOrdersDao ordersDao;
    private IEmpDao empDao;
    private ISupplierDao supplierDao;

    private IStoredetailDao storedetailDao;
    private IOrderdetailDao orderdetailDao;
    private IStoreoperDao storeoperDao;

    private IWaybillWs waybillWS;

    public void setWaybillWS(IWaybillWs waybillWS) {
        this.waybillWS = waybillWS;
    }

    public void setOrdersDao(IOrdersDao ordersDao) {
        this.ordersDao = ordersDao;
        super.setBaseDao(this.ordersDao);
    }

    public void setOrderdetailDao(IOrderdetailDao orderdetailDao) {
        this.orderdetailDao = orderdetailDao;
    }

    public void setStoreoperDao(IStoreoperDao storeoperDao) {
        this.storeoperDao = storeoperDao;
    }

    public void setStoredetailDao(IStoredetailDao storedetailDao) {
        this.storedetailDao = storedetailDao;
    }

    public void setEmpDao(IEmpDao empDao) {
        this.empDao = empDao;
    }

    public void setSupplierDao(ISupplierDao supplierDao) {
        this.supplierDao = supplierDao;
    }

    @Override
    public void add(Orders orders) {
        orders.setCreatetime(new Date());

        Double totalMoney = 0.0;
        for (Orderdetail orderdetail : orders.getOrderdetails()) {
            totalMoney += orderdetail.getMoney();
            orderdetail.setState(orderdetail.STATE_NOT_IN);
            orderdetail.setOrders(orders);
        }
        orders.setTotalmoney(totalMoney);

//        orders.setType(orders.TYPE_IN);

        orders.setState(orders.STATE_CREATE);

        ordersDao.add(orders);
    }

    @Override
    public List<Orders> getListByPage(Orders t1, Orders t2, Object param, int firstResult, int maxResults) {
        List<Orders> list = super.getListByPage(t1, t2, param, firstResult, maxResults);

        for (Orders orders : list) {
            orders.setCreaterName(getEmpName(orders.getCreater()));
            orders.setCheckerName(getEmpName(orders.getChecker()));
            orders.setStarterName(getEmpName(orders.getStarter()));
            orders.setEnderName(getEmpName(orders.getEnder()));

            orders.setSupplierName(getSupplierName(orders.getSupplieruuid()));
        }

        return list;
    }

    private String getEmpName(Long uuid) {

        if (uuid == null) {
            return null;
        }
        return empDao.get(uuid).getName();
    }

    private String getSupplierName(Long uuid) {

        if (uuid == null) {
            return null;
        }
        return supplierDao.get(uuid).getName();
    }

    public void doCheck(Long uuid, Long loginEmpId){

        Orders orders = ordersDao.get(uuid);

        if (!orders.STATE_CREATE.equals(orders.getState())) {
            throw new ErpException("该订单已经被审核,请勿重复审核!");
        }
        orders.setState(Orders.STATE_CHECK);

        orders.setChecker(loginEmpId);

        orders.setChecktime(new Date());
        ordersDao.update(orders);


    }

    @Override
    public void doStart(long id, Long uuid) {

        Orders orders = ordersDao.get(id);

        if (!Orders.STATE_CHECK.equals(orders.getState())) {
            throw new ErpException("该订单已经被确认,请勿重复确认!");
        }
        orders.setState(Orders.STATE_START);

        orders.setStarter(uuid);

        orders.setStarttime(new Date());

        ordersDao.update(orders);
    }


    public void doInStore(Long storeuuid, Long id, Long loginEmpId){

        //storeuuid 入库编号
        //id        订单明细编号
        //loginEmpId 登录用户明细

        Orderdetail orderdetail = orderdetailDao.get(id);

        if (Orderdetail.STATE_IN.equals(orderdetail.getState())){
            throw new ErpException("该商品已入库,请勿重复入库!");
        }
        //更新订单明细状态
        orderdetail.setState(Orderdetail.STATE_IN);
        orderdetail.setEnder(loginEmpId);
        orderdetail.setEndtime(new Date());
        orderdetail.setStoreuuid(storeuuid);

        Storedetail storedetail = new Storedetail();
        storedetail.setStoreuuid(storeuuid);
        storedetail.setGoodsuuid(orderdetail.getGoodsuuid());

        List<Storedetail> list = storedetailDao.getList(storedetail, null, null);

        if (list != null && list.size() > 0) {
            list.get(0).setNum(list.get(0).getNum() + orderdetail.getNum());
//			storedetailDao.update(storedetail);持久态对象动态改变数据库
        } else {
            storedetail.setNum(orderdetail.getNum());
            storedetailDao.add(storedetail);
        }

        //更新仓库明细
        Storeoper storeoper = new Storeoper();

        storeoper.setEmpuuid(loginEmpId);
        storeoper.setGoodsuuid(orderdetail.getGoodsuuid());
        storeoper.setNum(orderdetail.getNum());
        storeoper.setOpertime(orderdetail.getEndtime());
        storeoper.setStoreuuid(storeuuid);
        storeoper.setType(Storeoper.TYPE_IN);

        storeoperDao.add(storeoper);

        //更新订单状态 当且仅当所有该订单下的明细商品都入库 才更新订单状态
        Orderdetail od = new Orderdetail();//创建一个状态为未入库 并且是该订单下的订单明细对象作为查询条件
        od.setState(Orderdetail.STATE_NOT_IN);
        od.setOrders(orderdetail.getOrders());

        long count = orderdetailDao.getCount(od, null, null);
        if (count == 0) {
            Orders orders = orderdetail.getOrders();//持久化类 动态改变数据库 无需操作数据库

            orders.setState(Orders.STATE_END);
            orders.setEndtime(orderdetail.getEndtime());
            orders.setEnder(loginEmpId);
        }
    }

    @Override
    public void doOutStore(Long storeuuid, long id, Long loginEmpId) {
        Orderdetail orderdetail = orderdetailDao.get(id);

        if (Orderdetail.STATE_OUT.equals(orderdetail.getState())) {
            throw new ErpException("该商品已经出库,请勿重复出库!");
        }

        Storedetail storedetail = new Storedetail();
        storedetail.setStoreuuid(storeuuid);
        storedetail.setGoodsuuid(orderdetail.getGoodsuuid());

        //获取需要出库的仓库中该商品的数量 如果数量大于需要出库的数量则表示可以出库
        List<Storedetail> list = storedetailDao.getList(storedetail, null, null);
        if (list != null && list.size() > 0) {
            if (list.get(0).getNum() - orderdetail.getNum() < 0) {
                throw new ErpException("该商品库存不足,出库失败!");
            }
            //更新库存
            list.get(0).setNum(list.get(0).getNum()-orderdetail.getNum());
        }
        //更新订单明细
        orderdetail.setState(Orderdetail.STATE_OUT);
        orderdetail.setStoreuuid(storeuuid);
        orderdetail.setEndtime(new Date());
        orderdetail.setEnder(loginEmpId);

       //更新仓库记录
        Storeoper storeoper = new Storeoper();
        storeoper.setType(Storeoper.TYPE_OUT);
        storeoper.setStoreuuid(storeuuid);
        storeoper.setOpertime(orderdetail.getEndtime());
        storeoper.setNum(orderdetail.getNum());
        storeoper.setGoodsuuid(orderdetail.getGoodsuuid());
        storeoper.setEmpuuid(loginEmpId);
        storeoperDao.add(storeoper);

        //更新订单
        Orderdetail queryPara = new Orderdetail();
        queryPara.setOrders(orderdetail.getOrders());
        queryPara.setState(Orderdetail.STATE_NOT_IN);
        long count = orderdetailDao.getCount(queryPara,null,null);
        if (count == 0) {
            Orders orders = orderdetail.getOrders();
            orders.setState(Orders.STATE_OUT);
            orders.setEnder(loginEmpId);
            orders.setEndtime(orderdetail.getEndtime());

            Supplier supplier = supplierDao.get(orders.getSupplieruuid());
            Long Waybillsn = waybillWS.addWaybill(loginEmpId, supplier.getAddress(), supplier.getName(), supplier.getTele(), "-");

            orders.setWaybillsn(Waybillsn);
        }
    }

    public void exportById(OutputStream os, Long uuid) {

        Orders orders = ordersDao.get(uuid);
        List<Orderdetail> list = orders.getOrderdetails();

        HSSFWorkbook workbook = new HSSFWorkbook();

        HSSFSheet sheet = workbook.createSheet("订单明细");
        //创建全局样式
        HSSFCellStyle style_content = workbook.createCellStyle();
        //设置全局样式居中方式
        style_content.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style_content.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

        //设置字体样式
        HSSFFont font_content = workbook.createFont();
        font_content.setFontName("黑体");
        font_content.setFontHeightInPoints((short) 11);

        //设置标题字体样式
        HSSFFont title_font = workbook.createFont();
        title_font.setFontName("黑体");
        title_font.setBold(true);
        title_font.setFontHeightInPoints((short) 18);

        //给全局样式加入字体样式
        style_content.setFont(font_content);

        //设置标题样式
        HSSFCellStyle titleStyle = workbook.createCellStyle();
        titleStyle.cloneStyleFrom(style_content);
        titleStyle.setFont(title_font);

        //设置全局样式边框
        style_content.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style_content.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style_content.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style_content.setBorderTop(HSSFCellStyle.BORDER_THIN);

        int rowNum = list.size() + 9;
        for (int i = 2;i <= rowNum; i++) {
            HSSFRow row = sheet.createRow(i);
            for (int j = 0; j < 4; j++) {
                HSSFCell cell = row.createCell(j);
                cell.setCellStyle(style_content);
            }
        }

        sheet.addMergedRegion(new CellRangeAddress(2, 2, 1, 3));
        sheet.addMergedRegion(new CellRangeAddress(7, 7, 0, 3));
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));
        sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 0, 2));

        sheet.createRow(0).createCell(0).setCellValue("采购单");
        sheet.getRow(0).getCell(0).setCellStyle(titleStyle);
        sheet.getRow(0).setHeight((short) 1000);

        //供应商
        sheet.getRow(2).getCell(0).setCellValue("供应商");
        sheet.getRow(2).getCell(1).setCellValue(getSupplierName(orders.getSupplieruuid()));

        //日期
        sheet.getRow(3).getCell(0).setCellValue("下单日期");
        sheet.getRow(4).getCell(0).setCellValue("审核日期");
        sheet.getRow(5).getCell(0).setCellValue("采购日期");
        sheet.getRow(6).getCell(0).setCellValue("入库日期");

        //创建日期样式
        CellStyle style_date = workbook.createCellStyle();
        style_date.cloneStyleFrom(style_content);
        //格式化
        DataFormat dataFormat = workbook.createDataFormat();
        style_date.setDataFormat(dataFormat.getFormat("yyyy-MM-dd"));
        //设置日期格式
        for (int i = 3; i < 7; i++) {
            sheet.getRow(i).getCell(1).setCellStyle(style_date);
        }

        sheet.getRow(3).getCell(1).setCellValue(orders.getCreatetime());//下单日期
        setDateValue(sheet.getRow(4).getCell(1),orders.getChecktime());//审核日期
        setDateValue(sheet.getRow(5).getCell(1),orders.getStarttime());//确认日期
        setDateValue(sheet.getRow(6).getCell(1),orders.getEndtime());//入库日期

        sheet.getRow(3).getCell(2).setCellValue("经办人");
        sheet.getRow(4).getCell(2).setCellValue("经办人");
        sheet.getRow(5).getCell(2).setCellValue("经办人");
        sheet.getRow(6).getCell(2).setCellValue("经办人");

        sheet.getRow(3).getCell(3).setCellValue(getEmpName(orders.getCreater()));//下单人
        sheet.getRow(4).getCell(3).setCellValue(getEmpName(orders.getChecker()));//审核人
        sheet.getRow(5).getCell(3).setCellValue(getEmpName(orders.getStarter()));//确认人
        sheet.getRow(6).getCell(3).setCellValue(getEmpName(orders.getEnder()));//库管员

        sheet.getRow(7).getCell(0).setCellValue("订单明细");
        sheet.getRow(8).getCell(0).setCellValue("商品名称");
        sheet.getRow(8).getCell(1).setCellValue("数量");
        sheet.getRow(8).getCell(2).setCellValue("价格");
        sheet.getRow(8).getCell(3).setCellValue("金额");

        int count = 9;
        for (Orderdetail orderdetail : list) {
            Row row = sheet.getRow(count);
            row.getCell(0).setCellValue(orderdetail.getGoodsname());
            row.getCell(1).setCellValue(orderdetail.getNum());
            row.getCell(2).setCellValue(orderdetail.getPrice());
            row.getCell(3).setCellValue(orderdetail.getMoney());
            count++;
        }
        //加入合计
        HSSFCellStyle total_style = workbook.createCellStyle();
        total_style.cloneStyleFrom(style_content);
        HSSFFont total_font = workbook.createFont();
        total_font.setFontName("黑体");
        total_font.setBold(true);
        total_font.setFontHeightInPoints((short) 11);
        total_style.setFont(total_font);
        sheet.getRow(count).getCell(0).setCellValue("合计");
        sheet.getRow(count).getCell(0).setCellStyle(total_style);
        sheet.getRow(count).getCell(3).setCellValue(orders.getTotalmoney());
        sheet.getRow(count).getCell(3).setCellStyle(total_style);

        for (int i = 2;i <= rowNum; i++) {
            sheet.getRow(i).setHeight((short) 500);
        }
        for (int j = 0; j < 4; j++) {
            sheet.setColumnWidth(j, 5000);
        }

        try {
            workbook.write(os);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void setDateValue(Cell cell, Date date){
        if (date != null) {
            cell.setCellValue(date);
        }
    }
}
