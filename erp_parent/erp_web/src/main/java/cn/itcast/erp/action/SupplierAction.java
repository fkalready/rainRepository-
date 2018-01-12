package cn.itcast.erp.action;
import cn.itcast.erp.biz.ISupplierBiz;
import cn.itcast.erp.biz.utils.ErpException;
import cn.itcast.erp.entity.Supplier;
import cn.itcast.erp.utils.ActionUtil;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * 供应商Action 
 * @author Administrator
 *
 */
public class SupplierAction extends BaseAction<Supplier> {

	private ISupplierBiz supplierBiz;

	public void setSupplierBiz(ISupplierBiz supplierBiz) {
		this.supplierBiz = supplierBiz;
		super.setBaseBiz(this.supplierBiz);
	}

	private String q;

	public void setQ(String q) {
		this.q = q;
	}

	@Override
	public void list() {
		Supplier supplier = getT1();
		if (supplier == null) {
			setT1(new Supplier());
		}
		supplier.setName(q);
		super.list();
	}

	public void export(){
		HttpServletResponse response = ServletActionContext.getResponse();

		String filename = "";
		if ("1".equals(getT1().getType())){
			filename = "供应商.xls";
		}
		if ("2".equals(getT1().getType())){
			filename = "客户.xls";
		}
		try {
			String file = new String(filename.getBytes(), "iso-8859-1");
			response.setHeader("Content-Disposition","attachment;filename="+file);
			supplierBiz.export(response.getOutputStream(),getT1());
		}catch (IOException e){
			e.printStackTrace();
			ActionUtil.ajaxReturn(false,"导出失败!");
		}
	}

	private File file;
	private String contentType;
	private String fileFileName;

	public void setFile(File file) {
		this.file = file;
	}

	public void setFileContentType(String contentType) {
		this.contentType = contentType;
	}

	public void setFileFileName(String fileFileName) {
		this.fileFileName = fileFileName;
	}

	public void upload(){
		if (!"application/vnd.ms-excel".equals(contentType)) {
			if (null!=fileFileName && !fileFileName.endsWith(".xls")) {
				ActionUtil.ajaxReturn(false,"亲,请上传Excel的文件!");
				return;
			}
		}
		try {
			supplierBiz.doImport(new FileInputStream(file));
			ActionUtil.ajaxReturn(true, "导入成功!");
		} catch (ErpException e) {
			ActionUtil.ajaxReturn(false, e.getMessage());
		} catch (Exception e) {
			ActionUtil.ajaxReturn(false,"导入失败!");
			e.printStackTrace();
		}
	}
}
