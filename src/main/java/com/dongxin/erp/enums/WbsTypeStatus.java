package com.dongxin.erp.enums;
/**
* @ClassName: WbsTypeStatus
* @Description: 项目wbs类型状态标识
* @author huangheng
* @date 2021年1月20日
*/
public enum WbsTypeStatus {

	ENABLE("1", "启用"), DISABLE("0", "禁用");

    private String code;
    private String desc;


    public String getCode() {
        return code;
    }


    public String getDesc() {
        return desc;
    }


    WbsTypeStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
