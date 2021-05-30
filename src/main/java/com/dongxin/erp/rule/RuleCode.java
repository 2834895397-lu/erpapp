package com.dongxin.erp.rule;

import com.alibaba.fastjson.JSONObject;

/**
 * @author Succy
 * create on 2020/12/9
 */
public class RuleCode {



    /**
     * 支付订单编码生成规则参数
     */
    public static final JSONObject PAYMENT_FORM_DATA = new JSONObject();

    public static final String PAYMENT_RULE_CODE = "PAYMENT_RULE";

    /**
     * 采购订单编码生成规则参数
     */
    public static final JSONObject PURCHASE_FORM_DATA = new JSONObject();

    public static final String PURCHASE_RULE_CODE = "PURCHASE_RULE";

    static {
        PAYMENT_FORM_DATA.put("biz_code", "PAYMENT");
        PURCHASE_FORM_DATA.put("biz_code", "PURCHASE");
    }
}
