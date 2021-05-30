package com.dongxin.erp.ps1.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.jeecg.common.aspect.annotation.Dict;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 合同资金表
 * @Author: jeecg-boot
 * @Date:   2021-01-22
 * @Version: V1.0
 */
@Data
@TableName("tps_contract_fund")
@ApiModel(value="tps_contract对象", description="合同信息表")
public class TpsContractFund implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
	@ApiModelProperty(value = "主键")
	private java.lang.String id;
	/**创建人*/
	@ApiModelProperty(value = "创建人")
	private java.lang.String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "创建日期")
	private java.util.Date createTime;
	/**更新人*/
    @TableField(fill = FieldFill.UPDATE)
	@ApiModelProperty(value = "更新人")
	private java.lang.String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.UPDATE)
	@ApiModelProperty(value = "更新日期")
	private java.util.Date updateTime;
	/**所属部门*/
	@ApiModelProperty(value = "所属部门")
	private java.lang.String sysOrgCode;
	/**删除标识*/
	@Excel(name = "删除标识", width = 15)
	@ApiModelProperty(value = "删除标识")
	private java.lang.Integer delFlag;
	/**版本*/
	@Excel(name = "版本", width = 15)
    @Version
	@ApiModelProperty(value = "版本")
	private java.lang.Integer ver;
	/**租户ID*/
	@Excel(name = "租户ID", width = 15)
	@ApiModelProperty(value = "租户ID")
	private java.lang.Integer tenantId;
	/**合同ID*/
	@ApiModelProperty(value = "合同ID")
	private java.lang.String contractId;
	/**收付款日期*/
	@Excel(name = "收付款日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "收付款日期")
	private java.util.Date fundDate;
	/**资金类型*/
	@Excel(name = "资金类型", width = 15,dicCode = "fund_type")
	@ApiModelProperty(value = "资金类型")
	@Dict(dicCode = "fund_type")
	private java.lang.String fundType;
	/**交易类型*/
	@Excel(name = "交易类型", width = 15,dicCode = "fund_busi_type")
	@ApiModelProperty(value = "交易类型")
	@Dict(dicCode = "fund_busi_type")
	private java.lang.String fundBusiType;
	/**金额*/
	@Excel(name = "金额", width = 15)
	@ApiModelProperty(value = "金额")
	private java.lang.Double fundAmount;
	/**汇票日期*/
	@Excel(name = "汇票日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "汇票日期")
	private java.util.Date draftDate;
	/**出票银行*/
	@Excel(name = "出票银行", width = 15,dicCode = "bank_inf")
	@ApiModelProperty(value = "出票银行")
	@Dict(dicCode = "bank_inf")
	private java.lang.String draftBank;
	/**交易银行*/
	@Excel(name = "交易银行", width = 15,dicCode = "bank_inf")
	@ApiModelProperty(value = "交易银行")
	@Dict(dicCode = "bank_inf")
	private java.lang.String cashBank;
	/**状态*/
	@Excel(name = "状态", width = 15 ,dicCode = "ps_contract_fund_status")
	@ApiModelProperty(value = "状态")
	@Dict(dicCode = "ps_contract_fund_status")
	private java.lang.String status;
	/**备注*/
	@Excel(name = "备注", width = 15)
	@ApiModelProperty(value = "备注")
	private java.lang.String remark;
}
