package com.dongxin.erp.enums;
/*含税标识*/

import io.swagger.models.auth.In;

public enum ContractTaxFlag {
    TaxFlagFail(0,"不含税"),TaxFlagSuccess(1,"含税");

    private Integer code;
    private String desc;

    public Integer getCode() {
        return code;
    }


    public String getDesc() {
        return desc;
    }


    ContractTaxFlag(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
