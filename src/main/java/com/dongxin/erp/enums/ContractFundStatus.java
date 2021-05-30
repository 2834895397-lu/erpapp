package com.dongxin.erp.enums;

/*合同资金状态*/
public enum ContractFundStatus {
    Checking("0","未审核"),CheckSuccess("1","已审核");

    private String code;
    private String desc;

    public String getCode() {
        return code;
    }


    public String getDesc() {
        return desc;
    }


    ContractFundStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
