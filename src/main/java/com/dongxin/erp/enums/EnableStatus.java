package com.dongxin.erp.enums;

/**
 *
 */
public enum EnableStatus {

    ENABLE("1", "正常"), DISABLE("0", "禁用");

    private String code;
    private String desc;


    public String getCode() {
        return code;
    }


    public String getDesc() {
        return desc;
    }


    EnableStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
