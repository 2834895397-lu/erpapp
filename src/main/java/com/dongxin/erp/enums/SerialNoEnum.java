package com.dongxin.erp.enums;

public enum SerialNoEnum {
    PAYMENT_RULE("PAYMENT_RULE", "付款编码"),


    EXAMPLE("EXAMPLE","样例"),

    MM_SETTLE_RULE("MM_SETTLE_RULE","结算单号"),

    SM_OUT_RULE("SM_OUT_RULE","领用单号"),

    SM_IN_RULE("SM_IN_RULE","收货单号"),

    SM_MV_RULE("SM_MV_RULE","移库单号"),

    FM_PAY1_RULE("FM_PAY1_RULE ","应付款单号"),
    FM_PAY2_RULE("FM_PAY2_RULE ","预付款单号"),
    BM_INVITE_RULE("BM_INVITE_RULE","招标编号"),
    BM_BIDDING_RULE("BM_BIDDING_RULE"," 投标编号"),

    MM_PLAN_RULE("MM_PLAN_RULE", "采购申请号"),

    CS_INFO_RULE("CS_INFO_RULE", "客商编号"),

    MM_CONTRACT_RULE("MM_CONTRACT_RULE", "采购合同号"),

    MM_ORDER_RULE("MM_ORDER_RULE", "采购订单号");
    private String bizCode;

    private String desc;

    SerialNoEnum(String bizCode, String name) {
        this.bizCode = bizCode;
        this.desc = name;
    }

    public String getBizCode() {
        return bizCode;
    }

    public String getDesc() {
        return desc;
    }
}
