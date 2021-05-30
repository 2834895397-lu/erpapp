package com.dongxin.erp.mm.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * @Description: 采购订单明细表
 * @Author: jeecg-boot
 * @Date:   2020-11-18
 * @Version: V1.0
 */
@ApiModel(value="tmm_order对象", description="采购订单主表")
@Data
@TableName("tmm_order_dtl")
public class OrderDtl implements Serializable {
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
	/**物料ID*/
	@Excel(name = "物料ID", width = 15, dictTable = "tbd_material", dicText = "name", dicCode = "id")
	@Dict(dictTable = "tbd_material", dicText = "name", dicCode = "id")
	@ApiModelProperty(value = "物料ID")
	private java.lang.String tbdMaterialId;
	/**短文本*/
	@Excel(name = "短文本", width = 15)
	@ApiModelProperty(value = "短文本")
	private java.lang.String shortText;
	/**数量*/
	@Excel(name = "数量", width = 15)
	@ApiModelProperty(value = "数量")
	private java.lang.Integer matlQty;
	/**单价*/
	@Excel(name = "单价", width = 15)
	@ApiModelProperty(value = "单价")
	private java.lang.Double matlPrice;
	/**采购合同明细表ID*/
	@Excel(name = "采购合同明细表ID", width = 15)
	@ApiModelProperty(value = "采购合同明细表ID")
	private java.lang.String tmmContractDtlId;
	/**交货日期*/
	@Excel(name = "交货日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "交货日期")
	private java.util.Date deliveryDate;
	/**交货地点*/
	@Excel(name = "交货地点", width = 15)
	@ApiModelProperty(value = "交货地点")
	private java.lang.String deliveryPlace;
	/**采购订单主表ID*/
	@ApiModelProperty(value = "采购订单主表ID")
	private java.lang.String tmmPurchaseOrderId;
	/**备注*/
	@Excel(name = "备注", width = 15)
	@ApiModelProperty(value = "备注")
	private java.lang.String remark;
	/**币别*/
	@Excel(name = "币别", width = 15)
	@ApiModelProperty(value = "币别")
	@Dict(dicCode = "fm_pay_bb")
	private java.lang.String payBb;

	/**采购合同单据号*/
	@TableField(exist=false)
	private java.lang.String contractNumber;
}
