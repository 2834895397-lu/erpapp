package com.dongxin.erp.cs.csenum;

public enum IsCheck {


    customer("1","审核通过"),bussiness("0","未审核");

    private String code;
    private String desc;

    public String getCode() {
        return code;
    }


    public String getDesc() {
        return desc;
    }


    IsCheck(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }


}
