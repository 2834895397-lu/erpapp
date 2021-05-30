package com.dongxin.erp.cs.csenum;

public enum DelFlag {


    deleted(1,"已删除"),notDeleted(0,"未删除");

    private int code;
    private String desc;

    public int getCode() {
        return code;
    }


    public String getDesc() {
        return desc;
    }


    DelFlag(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }


}
