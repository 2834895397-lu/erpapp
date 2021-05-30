package com.dongxin.erp.ps.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 项目作业物料采购
 * @Author: jeecg-boot
 * @Date:   2021-01-21
 * @Version: V1.0
 */
@Data
@TableName("tps_material_plan")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="tps_material_plan对象", description="项目作业物料采购")
public class PsMaterialPlan implements Serializable {
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
    @TableField(fill = FieldFill.UPDATE)
    @ApiModelProperty(value = "更新人")
    private String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.UPDATE)
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
    @Version
    @ApiModelProperty(value = "版本")
    private Integer ver;
	/**租户ID*/
	@Excel(name = "租户ID", width = 15)
    @ApiModelProperty(value = "租户ID")
    private Integer tenantId;
    /**租户ID*/
    @Excel(name = "作业ID", width = 15)
    @ApiModelProperty(value = "作业ID")
    private String projectJobId;
	/**采购组织*/
	@Excel(name = "采购组织", width = 15)
    @ApiModelProperty(value = "采购组织")
    private String purchaseOrgId;
	/**物料ID*/
	@Excel(name = "物料ID", width = 15)
    @ApiModelProperty(value = "物料ID")
    private String materialId;
	/**物料名称*/
	@Excel(name = "物料名称", width = 15)
    @ApiModelProperty(value = "物料名称")
    private String materialName;
	/**物料编码*/
	@Excel(name = "物料编码", width = 15)
    @ApiModelProperty(value = "物料编码")
    private String materialCode;
	/**需求数量*/
	@Excel(name = "需求数量", width = 15)
    @ApiModelProperty(value = "需求数量")
    private Double planNum;
	/**允许偏差数*/
	@Excel(name = "允许偏差数", width = 15)
    @ApiModelProperty(value = "允许偏差数")
    private Double offsetPlanNum;
	/**计划交货日期*/
	@Excel(name = "计划交货日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "计划交货日期")
    private Date tackDate;
	/**预估单价*/
	@Excel(name = "预估单价", width = 15)
    @ApiModelProperty(value = "预估单价")
    private Double budgetPrice;
	/**预估金额*/
	@Excel(name = "预估金额", width = 15)
    @ApiModelProperty(value = "预估金额")
    private Double budgetAmount;
}
