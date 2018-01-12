package com.redsun.bos.ws;

import com.itheima.redsun.entity.Waybilldetail;

import javax.jws.WebService;
import java.util.List;

@WebService
public interface IWaybillWs {

    List<Waybilldetail> waybilldetailList(Long sn);

    Long addWaybill(Long userid, String toaddress, String addressee, String tele, String info);
}
