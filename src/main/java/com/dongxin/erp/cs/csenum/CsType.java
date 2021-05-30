package com.dongxin.erp.cs.csenum;
public enum CsType {


    informal("1","非正式"),formal("2","正式"),blacklist("3","黑名单");

    private String code;
    private String desc;

    public String getCode() {
        return code;
    }


    public String getDesc() {
        return desc;
    }


    CsType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }


}
