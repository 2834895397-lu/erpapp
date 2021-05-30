package com.dongxin.erp.enums;
/*结算标识*/
public enum SettleFlag {
    SETTLED("1","已结算"),UNSETTLE("0","未结算");

    private String code;
    private String desc;

    public String getCode() {
        return code;
    }


    public String getDesc() {
        return desc;
    }


    SettleFlag(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
