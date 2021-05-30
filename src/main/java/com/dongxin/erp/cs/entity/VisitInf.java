package com.dongxin.erp.cs.entity;

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
import org.springframework.stereotype.Repository;

/**
 * @Description: 顾客拜访登记
 * @Author: jeecg-boot
 * @Date:   2020-11-10
 * @Version: V1.0
 */
@Repository
@Data
@TableName("tcs_visit_inf")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="tcs_visit_inf对象", description="顾客拜访登记")
public class VisitInf implements Serializable {
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
	/**客商名称*/
	@Excel(name = "客商名称", width = 15, dictTable = "tcs_profile_inf", dicText = "cs_name", dicCode = "id")
	@Dict(dictTable = "tcs_profile_inf", dicText = "cs_name", dicCode = "id")
    @ApiModelProperty(value = "客商名称")
    private java.lang.String csId;
	/**拜访人*/
	@Excel(name = "拜访人", width = 15)
    @ApiModelProperty(value = "拜访人")
    private java.lang.String visMan;
	/**拜访时间*/
	@Excel(name = "拜访时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "拜访时间")
    private java.util.Date visDate;
	/**拜访省市*/
	@Excel(name = "拜访省市", width = 15)
    @ApiModelProperty(value = "拜访省市")
    private java.lang.String visPlace;
	/**拜访详细地址*/
	@Excel(name = "拜访详细地址", width = 15)
    @ApiModelProperty(value = "拜访详细地址")
    private java.lang.String visAddress;
	/**拜访记录*/
	@Excel(name = "拜访记录", width = 15)
    @ApiModelProperty(value = "拜访记录")
    private java.lang.String visRecord;
	/**上传附件*/
	@Excel(name = "上传附件", width = 15)
    @ApiModelProperty(value = "上传附件")
    private java.lang.String upload;
	/**租户ID*/
	@Excel(name = "租户ID", width = 15)
    @ApiModelProperty(value = "租户ID")
    private java.lang.Integer tenantId;


    @TableField(exist = false)
    private String provincesAndCities;


    @TableField(exist = false)
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private java.util.Date beginTime;

    @TableField(exist = false)
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private java.util.Date endTime;


}
