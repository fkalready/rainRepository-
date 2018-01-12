package cn.itcast.erp.biz;
import cn.itcast.erp.entity.Orders;
import com.redsun.bos.ws.Waybilldetail;

import java.io.OutputStream;
import java.util.List;

/**
 * 订单业务逻辑层接口
 * @author Administrator
 *
 */
public interface IOrdersBiz extends IBaseBiz<Orders>{
    void doCheck(Long uuid, Long loginEmp);

    void doStart(long id, Long uuid);

    void doInStore(Long storeuuid, Long id, Long loginEmpId);

    void doOutStore(Long storeuuid, long id, Long uuid);

    void exportById(OutputStream os, Long uuid);
}

