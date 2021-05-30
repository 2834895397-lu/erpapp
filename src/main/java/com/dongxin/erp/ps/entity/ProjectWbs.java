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
 * @Description: 项目工作分解结构
 * @Author: jeecg-boot
 * @Date:   2021-01-20
 * @Version: V1.0
 */
@Data
@TableName("tps_project_wbs")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="tps_project_wbs对象", description="项目工作分解结构")
public class ProjectWbs implements Serializable {
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
	/**wbs模板ID*/
	@Excel(name = "wbs模板ID", width = 15)
    @ApiModelProperty(value = "wbs模板ID")
    private java.lang.String wbsId;
	/**项目分解描述*/
	@Excel(name = "项目分解描述", width = 15)
    @ApiModelProperty(value = "项目分解描述")
    private java.lang.String wbsDesc;
	/**WBS上级ID*/
	@Excel(name = "WBS上级ID", width = 15)
    @ApiModelProperty(value = "WBS上级ID")
    private java.lang.String parentId;
	/**预算成本*/
	@Excel(name = "预算成本", width = 15)
    @ApiModelProperty(value = "预算成本")
    private java.lang.String wbsBudget;
	/**所属wbs类型编码*/
	@Excel(name = "所属wbs类型编码", width = 15)
    @ApiModelProperty(value = "所属wbs类型编码")
    private java.lang.String wbsTypeCode;
	/**项目ID*/
	@Excel(name = "项目ID", width = 15)
    @ApiModelProperty(value = "项目ID")
    private java.lang.String projectId;
}
