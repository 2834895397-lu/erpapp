package com.dongxin.erp.cs.csenum;

public enum IsUse {


    yes("1","是"),no("0","否");

    private String code;
    private String desc;

    public String getCode() {
        return code;
    }


    public String getDesc() {
        return desc;
    }


    IsUse(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }


}
