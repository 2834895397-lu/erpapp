package com.dongxin.erp.enums;

public enum BiddingType {
    BIDPENDING("1","投标"),BIDWIN("3","招标");

    private String code;
    private String desc;

    public String getCode() {
        return code;
    }


    public String getDesc() {
        return desc;
    }


    BiddingType (String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
