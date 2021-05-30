package com.dongxin.erp.cs.csenum;

public enum BlacklistStatus {

    WHILELIST("1","白名单"),BLACKLIST("2","黑名单");

    private String code;
    private String desc;

    public String getCode() {
        return code;
    }


    public String getDesc() {
        return desc;
    }


    BlacklistStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }


}
