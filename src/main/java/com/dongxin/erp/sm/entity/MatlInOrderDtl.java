package com.dongxin.erp.sm.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.ibatis.annotations.Insert;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import java.util.Date;
import java.util.Objects;

import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 收货单明细表
 * @Author: jeecg-boot
 * @Date:   2020-11-11
 * @Version: V1.0
 */
@ApiModel(value="tsm_matl_in_order对象", description="收货单主表")
@Data
@TableName("tsm_matl_in_order_dtl")
public class MatlInOrderDtl implements Serializable {
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
	/**租户*/
	@Excel(name = "租户", width = 15)
	@ApiModelProperty(value = "租户")
	private java.lang.String tenantId;
	/**物料*/
	@Excel(name = "物料", width = 15, dictTable = "tbd_material", dicText = "name", dicCode = "id")
	@Dict(dictTable = "tbd_material", dicText = "name", dicCode = "id")
	@ApiModelProperty(value = "物料")
	private java.lang.String tbdMaterialId;
	/**数量*/
	@Excel(name = "数量", width = 15)
	@ApiModelProperty(value = "数量")
	private java.lang.Integer matlQty;
	/**单价*/
	@Excel(name = "单价", width = 15)
	@ApiModelProperty(value = "单价")
	private java.lang.Double matlPrice;
	/**库存地*/
	@Excel(name = "库存地", width = 15, dictTable = "tbd_node", dicText = "name", dicCode = "id")
	@Dict(dictTable = "tbd_node", dicText = "name", dicCode = "id")
	@ApiModelProperty(value = "库存地")
	private java.lang.String tbdNodeId;
	/**收货单(入库单)主表ID*/
	@ApiModelProperty(value = "收货单(入库单)主表ID")
	private java.lang.String tsmMatlInOrderId;
	/**结算标识*/
	@Excel(name = "结算标识", width = 15, dicCode = "is_settle")
	@Dict(dicCode = "is_settle")
	@ApiModelProperty(value = "结算标识")
	private java.lang.String settleFlag;
	/**凭证单号（采购订单明细表ID）*/
	@Excel(name = "凭证单号（采购订单明细表ID）", width = 15)
	@ApiModelProperty(value = "凭证单号（采购订单明细表ID）")
	private java.lang.String voucherId;
	/**币别*/
	@Excel(name = "币别", width = 15)
	@ApiModelProperty(value = "币别")
	@Dict(dicCode = "fm_pay_bb")
	private java.lang.String payBb;
	/**采购合同主表ID*/
	@Excel(name = "采购合同主表ID", width = 15)
	@ApiModelProperty(value = "采购合同主表ID")
	private java.lang.String tmmContractId;
	/**采购合同明细表ID*/
	@Excel(name = "采购合同明细表ID", width = 15)
	@ApiModelProperty(value = "采购合同明细表ID")
	private java.lang.String tmmContractDtlId;
	/**采购订单号*/
	@TableField(exist = false)
	@Excel(name = "采购订单号", width = 15)
	@ApiModelProperty(value = "采购订单号")
	private java.lang.String orderNumber;
	/**采购合同号*/
	@TableField(exist = false)
	@Excel(name = "采购合同号", width = 15)
	@ApiModelProperty(value = "采购合同号")
	private java.lang.String contractNumber;
	
	/**采购合同明细表ID*/
	@Excel(name = "结算明细表ID", width = 15)
	@ApiModelProperty(value = "结算明细表ID")
	private java.lang.String tmmSettleDtlId;

	/**原单ID*/
	@Excel(name = "原单ID", width = 15)
	@ApiModelProperty(value = "原单ID")
	@TableField(fill = FieldFill.INSERT)
	private java.lang.String originalId;

	/**物料名称*/
	@Excel(name = "物料名称", width = 15)
	@TableField(exist = false)
	@ApiModelProperty(value = "物料名称")
	private String tbdMaterialName;
}
