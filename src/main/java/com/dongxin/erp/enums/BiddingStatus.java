package com.dongxin.erp.enums;

public enum BiddingStatus {
    BIDPENDING("1","待处理"),BIDWIN("2","中标"),
    BIDeNDING("3","已截标"),BIDOPENING("4","已开标");
    private String code;
    private String desc;

    public String getCode() {
        return code;
    }


    public String getDesc() {
        return desc;
    }


    BiddingStatus (String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}
