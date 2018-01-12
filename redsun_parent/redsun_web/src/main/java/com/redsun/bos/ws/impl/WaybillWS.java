package com.redsun.bos.ws.impl;

import com.itheima.redsun.biz.IWaybillBiz;
import com.itheima.redsun.biz.IWaybilldetailBiz;
import com.itheima.redsun.entity.Waybill;
import com.itheima.redsun.entity.Waybilldetail;
import com.redsun.bos.ws.IWaybillWs;

import java.util.List;

public class WaybillWS implements IWaybillWs {

    private IWaybilldetailBiz waybilldetailBiz;
    private IWaybillBiz waybillBiz;

    public void setWaybilldetailBiz(IWaybilldetailBiz waybilldetailBiz) {
        this.waybilldetailBiz = waybilldetailBiz;
    }

    public void setWaybillBiz(IWaybillBiz waybillBiz) {
        this.waybillBiz = waybillBiz;
    }

    public List<Waybilldetail> waybilldetailList(Long sn){

        Waybilldetail waybilldetail = new Waybilldetail();

        waybilldetail.setSn(sn);
        List<Waybilldetail> list = waybilldetailBiz.getList(waybilldetail, null, null);
        System.out.println(list);
        return list;
    }

    public Long addWaybill(Long userid, String toaddress, String addressee, String tele, String info){

        Waybill waybill = new Waybill();
        waybill.setUserid(userid);
        waybill.setToaddress(toaddress);
        waybill.setAddressee(addressee);
        waybill.setTele(tele);
        waybill.setInfo(info);

        waybillBiz.add(waybill);

        return waybill.getSn();
    }

}
