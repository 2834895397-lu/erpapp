package com.dongxin.erp.enums;

public enum BiddingFlag {
    BIDPENDING("0","待处理"),BIDWIN("1","中标");
    private String code;
    private String desc;

    public String getCode() {
        return code;
    }


    public String getDesc() {
        return desc;
    }


    BiddingFlag (String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
