package com.dongxin.erp.sm.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.*;
import com.dongxin.erp.enums.BillProp;
import com.dongxin.erp.enums.SerialNoEnum;
import com.dongxin.erp.rule.SerialNoUtil;
import lombok.Builder;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 收货单主表
 * @Author: jeecg-boot
 * @Date:   2020-11-11
 * @Version: V1.0
 */
@ApiModel(value="tsm_matl_in_order对象", description="收货单主表")
@Data
@TableName("tsm_matl_in_order")
public class MatlInOrder implements Serializable {
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
	/**类型*/
	@Excel(name = "类型", width = 15, dicCode = "tsm_matl_in_order_type")
    @Dict(dicCode = "tsm_matl_in_order_type")
    @ApiModelProperty(value = "类型")
    private java.lang.String type;
	/**租户*/
	@Excel(name = "租户", width = 15)
    @ApiModelProperty(value = "租户")
    private java.lang.String tenantId;
	/**凭证日期*/
	@Excel(name = "审核日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "审核日期")
    private java.util.Date voucherTime;
	/**状态*/
	@Excel(name = "状态", width = 15, dicCode = "is_check")
    @Dict(dicCode = "is_check")
    @ApiModelProperty(value = "状态")
    private java.lang.String status;
    /**过账日期*/
    @Excel(name = "过账日期", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "过账日期")
    private java.util.Date postingDate;
    /**过账日期*/
    @Excel(name = "收货单单据号", width = 15)
    @ApiModelProperty(value = "收货单单据号")
    @TableField(fill = FieldFill.INSERT)
    private java.lang.String matlInNumber;

    /**单据性质*/
    @Excel(name = "单据性质", width = 15)
    @ApiModelProperty(value = "单据性质")
    @TableField(fill = FieldFill.INSERT)
    private java.lang.Integer billProp;

    /**冲销单ID*/
    @Excel(name = "冲销单ID", width = 15)
    @ApiModelProperty(value = "冲销单ID")
    @TableField(fill = FieldFill.INSERT)
    private java.lang.String originalId;

}
