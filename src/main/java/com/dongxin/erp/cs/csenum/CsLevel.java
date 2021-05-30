package com.dongxin.erp.cs.csenum;
public enum CsLevel {


    common("1","普通"),excellent("2","优秀"),strategy("3","战略"),notYet("4","暂无");

    private String code;
    private String desc;

    public String getCode() {
        return code;
    }


    public String getDesc() {
        return desc;
    }


    CsLevel(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }


}
