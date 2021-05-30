package com.dongxin.erp.enums;

/*合同状态*/
public enum ContractStatus {
    Checking("0","审核中"),CheckFail("1","审核失败"),CheckSuccess("2","运行中");

    private String code;
    private String desc;

    public String getCode() {
        return code;
    }


    public String getDesc() {
        return desc;
    }


    ContractStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
