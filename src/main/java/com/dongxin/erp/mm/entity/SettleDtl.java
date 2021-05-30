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
 * @Description: 采购结算明细表
 * @Author: jeecg-boot
 * @Date:   2020-11-23
 * @Version: V1.0
 */
@ApiModel(value="tmm_settle对象", description="采购结算主表")
@Data
@TableName("tmm_settle_dtl")
public class SettleDtl implements Serializable {
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
	/**采购结算主表ID*/
	@ApiModelProperty(value = "采购结算主表ID")
	private java.lang.String tmmSettleId;
	/**物料ID*/
	@Excel(name = "物料ID", width = 15)
	@ApiModelProperty(value = "物料ID")
	@Dict(dictTable = "tbd_material", dicText = "name", dicCode = "id")
	private java.lang.String tbdMaterialId;
	/**数量*/
	@Excel(name = "数量", width = 15)
	@ApiModelProperty(value = "数量")
	private java.lang.Integer matlQty;
	/**单价*/
	@Excel(name = "单价", width = 15)
	@ApiModelProperty(value = "单价")
	private java.lang.Double matlPrice;
	/**收货单主表ID*/
	@Excel(name = "收货单明细表ID", width = 15)
	@ApiModelProperty(value = "收货单明细表ID")
	private java.lang.String tsmMatlInOrderDtlId;
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
	/**采购合同单据号*/
	@TableField(exist=false)
	private java.lang.String contractNumber;
	
	/** 物料编号 */
	@TableField(exist=false)
	private String materialCode;
	
	/** 入库单号 */
	@TableField(exist=false)
	private String matlInNumber;
	
	/** 入库单审核日期 */
	@TableField(exist=false)
    private java.util.Date voucherTime;
	
}
