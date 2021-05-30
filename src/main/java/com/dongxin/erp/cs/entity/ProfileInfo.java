package com.dongxin.erp.cs.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * @Description: 客商基本信息及客商委托书、银行账号等
 * @Author: huangheng
 * @Date: 2021-01-18
 * @Version: V1.0
 */
@Data
@ApiModel(value = "ProfileInfo对象", description = "客商基本信息及客商委托书、银行账号")
public class ProfileInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
    /**
     * 创建日期
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
    /**
     * 更新人
     */
    @ApiModelProperty(value = "更新人")
    @TableField(fill = FieldFill.UPDATE)
    private java.lang.String updateBy;
    /**
     * 更新日期
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    @TableField(fill = FieldFill.UPDATE)
    private java.util.Date updateTime;
    /**
     * 所属部门
     */
    @ApiModelProperty(value = "所属部门")
    private java.lang.String sysOrgCode;
    /**
     * 删除标识
     */
    @Excel(name = "删除标识", width = 15)
    @ApiModelProperty(value = "删除标识")
    private java.lang.Integer delFlag;
    /**
     * 版本
     */
    @Excel(name = "版本", width = 15)
    @ApiModelProperty(value = "版本")
    @Version
    private java.lang.Integer ver;
    /**
     * 客商代码
     */
    @Excel(name = "客商代码", width = 15)
    @ApiModelProperty(value = "客商代码")
    @TableField(fill = FieldFill.INSERT)
    private java.lang.String csCode;
    /**
     * 客商名称
     */
    @Excel(name = "客商名称", width = 15)
    @ApiModelProperty(value = "客商名称")
    private java.lang.String csName;
    /**
     * 法人代表
     */
    @Excel(name = "法人代表", width = 15)
    @ApiModelProperty(value = "法人代表")
    private java.lang.String legal;
    /**
     * 税号
     */
    @Excel(name = "税号", width = 15)
    @ApiModelProperty(value = "税号")
    private java.lang.String dutySign;
    /**
     * 注册地省市
     */
    @Excel(name = "注册地省市", width = 15)
    @ApiModelProperty(value = "注册地省市")
    private java.lang.String regisPlace;
    /**
     * 详细注册地址
     */
    @Excel(name = "详细注册地址", width = 15)
    @ApiModelProperty(value = "详细注册地址")
    private java.lang.String regisAddress;
    /**
     * 联系电话
     */
    @Excel(name = "联系电话", width = 15)
    @ApiModelProperty(value = "联系电话")
    private java.lang.String contaNumber;
    /**
     * 传真
     */
    @Excel(name = "传真", width = 15)
    @ApiModelProperty(value = "传真")
    private java.lang.String fax;
    /**
     * 电子邮箱
     */
    @Excel(name = "电子邮箱", width = 15)
    @ApiModelProperty(value = "电子邮箱")
    private java.lang.String email;
    /**
     * 办公地址
     */
    @Excel(name = "办公地址", width = 15)
    @ApiModelProperty(value = "办公地址")
    private java.lang.String officeAddress;
    /**
     * 是否启用
     */
    @Excel(name = "是否启用", width = 15, dicCode = "is_use")
    @Dict(dicCode = "is_use")
    @ApiModelProperty(value = "是否启用")
    private java.lang.String isUse;
    /**
     * 审核状态
     */
    @Excel(name = "审核状态", width = 15, dicCode = "is_check")
    @Dict(dicCode = "is_check")
    @ApiModelProperty(value = "审核状态")
    private java.lang.String isCheck;
    /**
     * 审核人
     */
    @Excel(name = "审核人", width = 15)
    @ApiModelProperty(value = "审核人")
    private java.lang.String checker;
    /**
     * 审核日期
     */
    @Excel(name = "审核日期", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "审核日期")
    private java.util.Date checkDate;
    /**
     * 租户ID
     */
    @Excel(name = "租户ID", width = 15)
    @ApiModelProperty(value = "租户ID")
    private java.lang.Integer tenantId;

    @TableField(exist = false)
    private String csFlag;

    @TableField(exist = false)
    private String csType;

    @TableField(exist = false)
    private String csLevel;

    @TableField(exist = false)
    private String provincesAndCities;

    @TableField(exist = false)
    private String resumeId;
    
	/**受托人姓名*/
	@Excel(name = "受托人姓名", width = 15)
	@ApiModelProperty(value = "受托人姓名")
	private java.lang.String trusteeName;
	
	/**开户行*/
	@Excel(name = "开户行", width = 15)
	@ApiModelProperty(value = "开户行")
	private java.lang.String openingBank;
	/**账号*/
	@Excel(name = "账号", width = 15)
	@ApiModelProperty(value = "账号")
	private java.lang.String bankAccount;

}
