package com.dongxin.erp.enums;

public enum WbsStatus {
    START("1","启动"),FORBIDDEN("0","禁用");
    private String code;
    private String desc;

    public String getCode() {
        return code;
    }


    public String getDesc() {
        return desc;
    }


    WbsStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
