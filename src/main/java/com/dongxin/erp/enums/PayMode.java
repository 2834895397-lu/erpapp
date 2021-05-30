package com.dongxin.erp.enums;

public enum PayMode {
    PAYMODE1("1","应付款"),PAYMODE2("2","预付款");
    private String code;
    private String desc;

    public String getCode() {
        return code;
    }


    public String getDesc() {
        return desc;
    }


    PayMode (String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
