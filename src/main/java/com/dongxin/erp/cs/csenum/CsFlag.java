package com.dongxin.erp.cs.csenum;

public enum CsFlag {


    customer("1","客户"),bussiness("0","供应商");

    private String code;
    private String desc;

    public String getCode() {
        return code;
    }


    public String getDesc() {
        return desc;
    }


    CsFlag(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }


}
