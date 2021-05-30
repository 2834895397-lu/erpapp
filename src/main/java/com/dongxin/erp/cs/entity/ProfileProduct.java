package com.dongxin.erp.cs.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.jeecg.common.aspect.annotation.Dict;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 客商经营品种表
 * @Author: jeecg-boot
 * @Date:   2020-11-10
 * @Version: V1.0
 */
@Data
@TableName("tcs_profile_product")
@ApiModel(value="tcs_profile_inf对象", description="客商基础信息")
public class ProfileProduct implements Serializable {
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
	/**客商ID*/
	@ApiModelProperty(value = "客商ID")
	private java.lang.String csId;
	/**客商标识*/
	@Excel(name = "客商标识", width = 15)
    @Dict(dicCode = "cs_flag")
	@ApiModelProperty(value = "客商标识")
	private java.lang.String csFlag;
	/**经营大类*/
	@Excel(name = "经营大类", width = 15)
    @Dict(dicCode = "id",dicText = "name",dictTable = "tbd_material")
	@ApiModelProperty(value = "经营大类")
	private java.lang.String busiProduct;
	/**经营品种*/
	@Excel(name = "经营品种", width = 15)
	@ApiModelProperty(value = "经营品种")
	private java.lang.String busiVariety;
	/**经营省市*/
	@Excel(name = "经营省市", width = 15)
	@ApiModelProperty(value = "经营省市")
	private java.lang.String busiPlace;
	/**租户ID*/
	@Excel(name = "租户ID", width = 15)
	@ApiModelProperty(value = "租户ID")
	private java.lang.Integer tenantId;

	@TableField(exist = false)
	private String name;

	@TableField(exist = false)
	private String provincesAndCities;

	@TableField(exist = false)
	private String busiVarietyName;
}
