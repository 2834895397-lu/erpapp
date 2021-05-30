package com.dongxin.erp.enums;

/**
 * 出入库单据类型
 */
public enum WasteBookTypes {
    INORDERCOME("1", "收货单入库"),
    OUTORDEROUT("2", "领用单出库"),
    MOVEORDERMOVE("3", "移库单移库");

    private String code;
    private String desc;


    WasteBookTypes(String code, String desc){
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
