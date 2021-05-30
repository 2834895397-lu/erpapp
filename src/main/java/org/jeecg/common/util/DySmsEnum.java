//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.jeecg.common.util;

import org.apache.commons.lang.StringUtils;

public enum DySmsEnum {
    LOGIN_TEMPLATE_CODE("SMS_216838333", "Jacklu", "code"),
    FORGET_PASSWORD_TEMPLATE_CODE("SMS_216838333", "Jacklu", "code"),
    REGISTER_TEMPLATE_CODE("SMS_216838333", "Jacklu", "code"),
    MEET_NOTICE_TEMPLATE_CODE("SMS_216838333", "Jacklu", "username,title,minute,time"),
    PLAN_NOTICE_TEMPLATE_CODE("SMS_216838333", "Jacklu", "username,title,time");

    private String templateCode;
    private String signName;
    private String keys;

    private DySmsEnum(String templateCode, String signName, String keys) {
        this.templateCode = templateCode;
        this.signName = signName;
        this.keys = keys;
    }

    public String getTemplateCode() {
        return this.templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }

    public String getSignName() {
        return this.signName;
    }

    public void setSignName(String signName) {
        this.signName = signName;
    }

    public String getKeys() {
        return this.keys;
    }

    public void setKeys(String keys) {
        this.keys = keys;
    }

    public static DySmsEnum toEnum(String templateCode) {
        if (StringUtils.isEmpty(templateCode)) {
            return null;
        } else {
            DySmsEnum[] var1 = values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                DySmsEnum item = var1[var3];
                if (item.getTemplateCode().equals(templateCode)) {
                    return item;
                }
            }

            return null;
        }
    }
}
