package com.dongxin.erp.enums;

/**
 * 审核状态
 */
public enum Status {

    CHECK("1", "已审核"), UNCHECK("0", "未审核");

    private String code;
    private String desc;


    public String getCode() {
        return code;
    }


    public String getDesc() {
        return desc;
    }


    Status(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
