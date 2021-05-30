package com.dongxin.erp.sm.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import java.util.Date;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 移库单明细表
 * @Author: jeecg-boot
 * @Date:   2020-11-10
 * @Version: V1.0
 */
@ApiModel(value="tsm_matl_move_order对象", description="移库单主表")
@Data
@TableName("tsm_matl_move_order_dtl")
public class MatlMoveOrderDtl implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
	@ApiModelProperty(value = "主键")
	private String id;
	/**创建人*/
	@ApiModelProperty(value = "创建人")
	private String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "创建日期")
	private Date createTime;
	/**更新人*/
	@ApiModelProperty(value = "更新人")
	private String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "更新日期")
	private Date updateTime;
	/**所属部门*/
	@ApiModelProperty(value = "所属部门")
	private String sysOrgCode;
	/**删除标识*/
	@Excel(name = "删除标识", width = 15)
	@ApiModelProperty(value = "删除标识")
	private Integer delFlag;
	/**版本*/
	@Excel(name = "版本", width = 15)
	@ApiModelProperty(value = "版本")
	private Integer ver;
	/**租户ID*/
	@Excel(name = "租户ID", width = 15)
	@ApiModelProperty(value = "租户ID")
	private String tenantId;
	/**物料*/
	@Excel(name = "物料", width = 15, dictTable = "tbd_material", dicText = "name", dicCode = "id")
	@Dict(dictTable = "tbd_material", dicText = "name", dicCode = "id")
	@ApiModelProperty(value = "物料")
	private String tbdMaterialId;
	/**数量*/
	@Excel(name = "数量", width = 15)
	@ApiModelProperty(value = "数量")
	private Integer matlQty;
	/**单价*/
	@Excel(name = "单价", width = 15)
	@ApiModelProperty(value = "单价")
	private Double matlPrice;
	/**移库起点库存地*/
	@Excel(name = "移库起点库存地", width = 15, dictTable = "tbd_node", dicText = "name", dicCode = "id")
	@Dict(dictTable = "tbd_node", dicText = "name", dicCode = "id")
	@ApiModelProperty(value = "移库起点库存地")
	private String fromTbdNodeId;
	/**移库终点库存地*/
	@Excel(name = "移库终点库存地", width = 15, dictTable = "tbd_node", dicText = "name", dicCode = "id")
	@Dict(dictTable = "tbd_node", dicText = "name", dicCode = "id")
	@ApiModelProperty(value = "移库终点库存地")
	private String toTbdNodeId;
	/**移库单主表ID*/
	@ApiModelProperty(value = "移库单主表ID")
	private String tsmMatlMoveOrderId;
	/**币别*/
	@Excel(name = "币别", width = 15)
	@ApiModelProperty(value = "币别")
	@Dict(dicCode = "fm_pay_bb")
	private java.lang.String payBb;
	/**移库起点名称*/
	@Excel(name = "移库起点名称", width = 15)
	@TableField(exist = false)
	@ApiModelProperty(value = "移库起点名称")
	private String fromTbdNodeName;

	/**冲销单ID*/
	@Excel(name = "冲销单ID", width = 15)
	@ApiModelProperty(value = "冲销单ID")
	@TableField(fill = FieldFill.INSERT)
	private java.lang.String originalId;
}
