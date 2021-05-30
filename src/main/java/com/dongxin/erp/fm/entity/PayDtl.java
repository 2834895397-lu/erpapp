package com.dongxin.erp.fm.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import java.util.Date;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 财务付款明细信息
 * @Author: jeecg-boot
 * @Date:   2020-11-18
 * @Version: V1.0
 */
@ApiModel(value="tfm_pay_inf对象", description="财务付款信息表")
@Data
@TableName("tfm_pay_dtl")
public class PayDtl implements Serializable {
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
	/**付款单ID*/
	@ApiModelProperty(value = "付款单ID")
	private java.lang.String payId;
	/**付款单号*/
	@Excel(name = "付款单号", width = 15)
	@ApiModelProperty(value = "付款单号")
	private java.lang.String payCode;
	/**付款方式*/
	@Excel(name = "付款方式", width = 15, dicCode = "fm_pay_kind")
	@Dict(dicCode = "fm_pay_kind")
	@ApiModelProperty(value = "付款方式")
	private java.lang.Integer payKind;
	/**币别*/
	@Excel(name = "币别", width = 15, dicCode = "fm_pay_bb")
	@Dict(dicCode = "fm_pay_bb")
	@ApiModelProperty(value = "币别")
	private java.lang.String payBb;
	/**付款金额*/
	@Excel(name = "付款金额", width = 15)
	@ApiModelProperty(value = "付款金额")
	private java.math.BigDecimal money;
}
