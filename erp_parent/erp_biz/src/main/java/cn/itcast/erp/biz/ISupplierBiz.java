package cn.itcast.erp.biz;
import cn.itcast.erp.entity.Supplier;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 供应商业务逻辑层接口
 * @author Administrator
 *
 */
public interface ISupplierBiz extends IBaseBiz<Supplier>{

    public void export(OutputStream os, Supplier supplier);

    void doImport(FileInputStream fos) throws IOException;
}

