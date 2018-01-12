package cn.itcast.erp.biz;
import cn.itcast.erp.entity.Storealert;
import cn.itcast.erp.entity.Storedetail;

import javax.mail.MessagingException;
import java.util.List;

/**
 * 仓库库存业务逻辑层接口
 * @author Administrator
 *
 */
public interface IStoredetailBiz extends IBaseBiz<Storedetail>{

    List<Storealert> getStorealertList();

    void sendStorealertMail() throws MessagingException;
}

