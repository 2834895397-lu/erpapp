package com.dongxin.erp.mm.entity;

import java.io.Serializable;
import java.util.Date;
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
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 采购订单主表
 * @Author: jeecg-boot
 * @Date:   2020-11-18
 * @Version: V1.0
 */
@ApiModel(value="tmm_order对象", description="采购订单主表")
@Data
@TableName("tmm_order")
public class Order implements Serializable {
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
    @TableField(fill = FieldFill.UPDATE)
    private java.lang.String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    @TableField(fill = FieldFill.UPDATE)
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
    @Version
    private java.lang.Integer ver;
	/**租户ID*/
	@Excel(name = "租户ID", width = 15)
    @ApiModelProperty(value = "租户ID")
    private java.lang.Integer tenantId;
	/**订单类型 */
	@Excel(name = "订单类型 ", width = 15, dicCode = "tmm_order_type")
    @Dict(dicCode = "tmm_order_type")
    @ApiModelProperty(value = "订单类型 ")
    private java.lang.String type;
	/**凭证日期*/
	@Excel(name = "凭证日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "凭证日期")
    private java.util.Date voucherTime;
    /**供方ID*/
    @Excel(name = "供方ID", width = 15)
    @ApiModelProperty(value = "供方ID")
    private java.lang.String tcsProfileInfId;
	/**需方（公司）*/
	@Excel(name = "需方（公司）", width = 15)
    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "id")
    @ApiModelProperty(value = "需方（公司）")
    private java.lang.String tbdCompanyId;
	/**采购组织*/
	@Excel(name = "采购组织", width = 15)
    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "id")
    @ApiModelProperty(value = "采购组织")
    private java.lang.String purchaseOrgn;
	/**状态*/
	@Excel(name = "状态", width = 15, dicCode = "is_check")
    @Dict(dicCode = "is_check")
    @ApiModelProperty(value = "状态")
    private java.lang.String status;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private java.lang.String remark;
    /**供方编码*/
    @Excel(name = "供方编码", width = 15)
    @ApiModelProperty(value = "供方编码")
    private java.lang.String tcsProfileInfCode;
    /**供方名称*/
    @Excel(name = "供方名称", width = 15)
    @ApiModelProperty(value = "供方名称")
    private java.lang.String tcsProfileInfName;
    /**采购订单单据号*/
    @Excel(name = "采购订单单据号", width = 15)
    @ApiModelProperty(value = "采购订单单据号")
    @TableField(fill = FieldFill.INSERT)
    private java.lang.String orderNumber;
}
