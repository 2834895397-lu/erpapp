package com.dongxin.erp.enums;

public enum BillProp {
    RED(-1 , "红单"),BLUE(1 , "蓝单");
    private Integer value;
    private String desc;

    public Integer getValue() {
        return value;
    }
    public String getDesc(){return desc;}

    BillProp(Integer value , String desc) {
        this.value = value;
        this.desc = desc;
    }
}
