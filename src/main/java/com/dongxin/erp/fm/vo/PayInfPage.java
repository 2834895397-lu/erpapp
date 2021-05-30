package com.dongxin.erp.fm.vo;

import java.util.List;
import com.dongxin.erp.fm.entity.PayInf;
import com.dongxin.erp.fm.entity.PayDtl;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelEntity;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 财务付款信息表
 * @Author: jeecg-boot
 * @Date:   2020-11-18
 * @Version: V1.0
 */
@Data
@ApiModel(value="tfm_pay_infPage对象", description="财务付款信息表")
public class PayInfPage {

	/**主键*/
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
	@ApiModelProperty(value = "更新人")
	private java.lang.String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
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
	@ApiModelProperty(value = "版本")
	private java.lang.Integer ver;
	/**租户ID*/
	@Excel(name = "租户ID", width = 15)
	@ApiModelProperty(value = "租户ID")
	private java.lang.Integer tenantId;
	/**付款单编号*/
	@Excel(name = "付款单编号", width = 15)
	@ApiModelProperty(value = "付款单编号")
	private java.lang.String code;
	/**申请付款日期*/
	@Excel(name = "申请付款日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "申请付款日期")
	private java.util.Date applyDate;
	/**应付日期*/
	@Excel(name = "应付日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "应付日期")
	private java.util.Date payDate;
	/**付款总金额*/
	@Excel(name = "付款总金额", width = 15)
	@ApiModelProperty(value = "付款总金额")
	private java.math.BigDecimal totalSum;
	/**付款类型*/
	@Excel(name = "付款类型", width = 15, dicCode = "fm_pay_mode")
    @Dict(dicCode = "fm_pay_mode")
	@ApiModelProperty(value = "付款类型")
	private java.lang.String payMode;
	/**是否预算外*/
	@Excel(name = "是否预算外", width = 15, dicCode = "fm_budget_flag")
    @Dict(dicCode = "fm_budget_flag")
	@ApiModelProperty(value = "是否预算外")
	private java.lang.String budgetFlag;
	/**款项性质*/
	@Excel(name = "款项性质", width = 15)
	@ApiModelProperty(value = "款项性质")
	private java.lang.String payNature;
	/**紧急程度*/
	@Excel(name = "紧急程度", width = 15, dicCode = "fm_urgency")
    @Dict(dicCode = "fm_urgency")
	@ApiModelProperty(value = "紧急程度")
	private java.lang.String urgency;
	/**付款原因*/
	@Excel(name = "付款原因", width = 15)
	@ApiModelProperty(value = "付款原因")
	private java.lang.String reason;
	/**采购合同ID*/
	@Excel(name = "采购合同ID", width = 15)
	@ApiModelProperty(value = "采购合同ID")
	private java.lang.String contractId;
	/**采购合同号*/
	@Excel(name = "采购合同号", width = 15)
	@ApiModelProperty(value = "采购合同号")
	private java.lang.String contractCode;
	/**采购合同名称*/
	@Excel(name = "采购合同名称", width = 15)
	@ApiModelProperty(value = "采购合同名称")
	private java.lang.String contractName;
	/**发票ID*/
	@Excel(name = "发票ID", width = 15)
	@ApiModelProperty(value = "发票ID")
	private java.lang.String invoiceId;
	/**发票号*/
	@Excel(name = "发票号", width = 15)
	@ApiModelProperty(value = "发票号")
	private java.lang.String invoiceCode;
	/**支付公司ID*/
	@Excel(name = "支付公司ID", width = 15)
	@ApiModelProperty(value = "支付公司ID")
	private java.lang.String csIdPay;
	/**支付账号*/
	@Excel(name = "支付账号", width = 15)
	@ApiModelProperty(value = "支付账号")
	private java.lang.String payAccount;
	/**供应商ID*/
	@Excel(name = "供应商ID", width = 15)
	@ApiModelProperty(value = "供应商ID")
	private java.lang.String csIdRecept;
	/**供应商名称*/
	@Excel(name = "供应商名称", width = 15)
	@ApiModelProperty(value = "供应商名称")
	private java.lang.String csNameRecept;
	/**收款开户行*/
	@Excel(name = "收款开户行", width = 15)
	@ApiModelProperty(value = "收款开户行")
	private java.lang.String receptBank;
	/**收款账号*/
	@Excel(name = "收款账号", width = 15)
	@ApiModelProperty(value = "收款账号")
	private java.lang.String receptAccount;
	/**审批日期*/
	@Excel(name = "审批日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "审批日期")
	private java.util.Date appDate;
	/**单据状态*/
	@Excel(name = "单据状态", width = 15)
	@ApiModelProperty(value = "单据状态")
	private java.lang.String appStatus;
	/**支付公司名称*/
	@Excel(name = "支付公司名称", width = 15)
	@ApiModelProperty(value = "支付公司名称")
	private java.lang.String csNamePay;
	@Excel(name = "付款公司编码", width = 15)
	@ApiModelProperty(value = "付款公司编码")
	private java.lang.String csCodePay;
	@ExcelCollection(name="财务付款明细信息")
	@ApiModelProperty(value = "财务付款明细信息")
	private List<PayDtl> payDtlList;
	
}
