package com.dongxin.erp.enums;
/*完结标识*/
public enum ContEndFlag {
    OVER("1","已完结"),UNOVER("0","执行中");

    private String code;
    private String desc;

    public String getCode() {
        return code;
    }


    public String getDesc() {
        return desc;
    }


    ContEndFlag(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
