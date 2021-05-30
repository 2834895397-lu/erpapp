package com.dongxin.erp.ps.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.jeecgframework.poi.excel.annotation.Excel;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 项目基础信息表
 * @Author: jeecg-boot/huangheng
 * @Date:   2021-01-15
 * @Version: V1.0
 */
@Data
@TableName("tps_project")
@ApiModel(value="tps_project对象", description="项目基础信息表")
public class Project implements Serializable {
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
	/**项目编号*/
    @Excel(name = "项目编号", width = 15)
    @ApiModelProperty(value = "项目编号")
    private java.lang.String projectNo;
	/**项目名称*/
    @Excel(name = "项目名称", width = 15)
    @ApiModelProperty(value = "项目名称")
    private java.lang.String projectName;
	/**项目合同ID*/
    @Excel(name = "项目合同ID", width = 15)
    @ApiModelProperty(value = "项目合同ID")
    private java.lang.String contractId;
	/**项目属性ID*/
    @Excel(name = "项目属性ID", width = 15)
    @ApiModelProperty(value = "项目属性ID")
    private java.lang.String projectTypeId;
	/**项目工期(天数)*/
    @Excel(name = "项目工期(天数)", width = 15)
    @ApiModelProperty(value = "项目工期(天数)")
    private java.lang.Integer projectPeriod;
	/**开始日期*/
    @Excel(name = "开始日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "开始日期")
    private java.util.Date beginDate;
	/**结束日期*/
    @Excel(name = "结束日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "结束日期")
    private java.util.Date endDate;
	/**项目负责人*/
    @Excel(name = "项目负责人", width = 15)
    @ApiModelProperty(value = "项目负责人")
    private java.lang.String projectManager;
	/**备注*/
    @Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private java.lang.String remark;
	/**状态*/
    @Excel(name = "状态", width = 15, dicCode = "tps_project_status")
    @Dict(dicCode = "tps_project_status")
    @ApiModelProperty(value = "状态")
    private java.lang.String status;
    
    /**经办人*/
    @TableField(exist = false)
	@Excel(name = "经办人", width = 15)
    @ApiModelProperty(value = "经办人")
    private java.lang.String operator;
    
    /**甲方公司名称*/
	@TableField(exist = false)
	@Excel(name = "甲方公司名称", width = 15)
    @ApiModelProperty(value = "甲方公司名称")
    private java.lang.String partyAName;
	
	/**乙方公司名称*/
	@TableField(exist = false)
	@Excel(name = "乙方公司名称", width = 15)
    @ApiModelProperty(value = "乙方公司名称")
    private java.lang.String partyBName;
	
	@TableField(exist = false)
    @ApiModelProperty(value = "合同号")
    private java.lang.String contractNo;
	
	/**币别*/
	@TableField(exist = false)
    @ApiModelProperty(value = "币别")
    private java.lang.String payBb;
    
}
