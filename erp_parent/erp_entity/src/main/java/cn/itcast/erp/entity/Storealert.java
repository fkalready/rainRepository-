package cn.itcast.erp.entity;

public class Storealert {
    private Long uuid; //商品编号

    private String name;//商品名称

    private String storenum;
    private String outnum;

    public Long getUuid() {
        return uuid;
    }

    public void setUuid(Long uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStorenum() {
        return storenum;
    }

    public void setStorenum(String storenum) {
        this.storenum = storenum;
    }

    public String getOutnum() {
        return outnum;
    }

    public void setOutnum(String outnum) {
        this.outnum = outnum;
    }
}
