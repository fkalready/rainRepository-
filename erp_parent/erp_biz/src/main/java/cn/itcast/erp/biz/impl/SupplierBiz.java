package cn.itcast.erp.biz.impl;
import cn.itcast.erp.biz.ISupplierBiz;
import cn.itcast.erp.biz.utils.ErpException;
import cn.itcast.erp.dao.ISupplierDao;
import cn.itcast.erp.entity.Supplier;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * 供应商业务逻辑类
 * @author Administrator
 *
 */
public class SupplierBiz extends BaseBiz<Supplier> implements ISupplierBiz {

	private ISupplierDao supplierDao;
	
	public void setSupplierDao(ISupplierDao supplierDao) {
		this.supplierDao = supplierDao;
		super.setBaseDao(this.supplierDao);
	}

	public void export(OutputStream os, Supplier supplier) {
		List<Supplier> list = super.getList(supplier, null, null);

		HSSFWorkbook workbook = new HSSFWorkbook();

		HSSFSheet sheet = null;
		String type = supplier.getType();
		if ("1".equals(type)) {
			sheet = workbook.createSheet("供应商");
		}
		if ("2".equals(type)) {
			sheet = workbook.createSheet("客户");
		}

		HSSFRow row = sheet.createRow(0);
		String[] cellName = {"编号","名称","地址","联系人","电话","Email"};
		int[] cellWidth = {2000,4000,8000,2000,3000,8000};

		for (int i = 0; i < cellName.length; i++) {
			row.createCell(i).setCellValue(cellName[i]);
			sheet.setColumnWidth(i, cellWidth[i]);
		}

		int i = 1;
		for (Supplier s : list) {
			HSSFRow rows = sheet.createRow(i);

			rows.createCell(0).setCellValue(s.getUuid());
			rows.createCell(1).setCellValue(s.getName());
			rows.createCell(2).setCellValue(s.getAddress());
			rows.createCell(3).setCellValue(s.getContact());
			rows.createCell(4).setCellValue(s.getTele());
			rows.createCell(5).setCellValue(s.getEmail());

			i++;
		}

		try {
			workbook.write(os);
		}catch (IOException e){
			e.printStackTrace();
		}
		finally {
			try {
				workbook.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void doImport(FileInputStream fis) throws IOException {

		HSSFWorkbook workbook = null;
		try {
			workbook = new HSSFWorkbook(fis);

			HSSFSheet sheet = workbook.getSheetAt(0);

			String type = "";
			if ("供应商".equals(sheet.getSheetName())) {
				type = Supplier.TYPE_SUPPLIER;
			}
			else if ("客户".equals(sheet.getSheetName())) {
				type = Supplier.TYPE_CUSTOMER;
			}
			else {
				throw new ErpException("请正确命名您的工作表!");
			}

			int lastRowNum = sheet.getLastRowNum();

			for (int i = 1; i <= lastRowNum; i++) {
				Supplier supplier = null;
				String name = sheet.getRow(i).getCell(1).getStringCellValue();
				supplier = new Supplier();
				supplier.setName(name);
				List<Supplier> list = supplierDao.getList(null, supplier, null);
				if (list != null && list.size() > 0) {
					supplier = list.get(0);
				}
				supplier.setAddress(sheet.getRow(i).getCell(2).getStringCellValue());
				supplier.setContact(sheet.getRow(i).getCell(3).getStringCellValue());
				supplier.setTele(sheet.getRow(i).getCell(4).getStringCellValue());
				supplier.setEmail(sheet.getRow(i).getCell(5).getStringCellValue());
				if (list.size() == 0) {
					supplier.setType(type);
					supplierDao.add(supplier);
				}
			}
		} finally {
			try {
				workbook.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
