package com.dongxin.erp.enums;

/**
 * 出入库方向
 */
public enum OutAndInWarehouseTypes {
    IN("1", "入库"),
    OUT("-1","出库");

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    private String code;
    private String desc;

    OutAndInWarehouseTypes(String code, String desc){
        this.code = code;
        this.desc = desc;
    }
}
