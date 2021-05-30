package com.dongxin.erp.enums;
/**
* @ClassName: TpsProjectStatus
* @Description: 项目状态
* @author huangheng
* @date 2021年1月18日
*/
public enum TpsProjectStatus {

	 PENDING("0", "待审批"), 
	 APPROVE("1", "审批通过"), 
	 APPROVAL_FAILED("2", "审批未通过"), 
	 IN_SERVICE("3", "运行中");

    private String code;
    private String desc;


    public String getCode() {
        return code;
    }


    public String getDesc() {
        return desc;
    }


    TpsProjectStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
