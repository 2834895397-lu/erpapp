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
 * @Description: 客商委托书
 * @Author: jeecg-boot
 * @Date:   2020-11-10
 * @Version: V1.0
 */
@Data
@TableName("tcs_profile_poa")
@ApiModel(value="tcs_profile_inf对象", description="客商基础信息")
public class ProfilePoa implements Serializable {
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
	/**委托人身份证号码*/
	@Excel(name = "委托人身份证号码", width = 15)
	@ApiModelProperty(value = "委托人身份证号码")
	private java.lang.String clientIdNum;
	/**委托人职务*/
	@Excel(name = "委托人职务", width = 15)
	@ApiModelProperty(value = "委托人职务")
	private java.lang.String clientDuty;
	/**委托人联系电话*/
	@Excel(name = "委托人联系电话", width = 15)
	@ApiModelProperty(value = "委托人联系电话")
	private java.lang.String clientContact;
	/**受托人姓名*/
	@Excel(name = "受托人姓名", width = 15)
	@ApiModelProperty(value = "受托人姓名")
	private java.lang.String trusteeName;
	/**受托人性别*/
	@Excel(name = "受托人性别", width = 15)
    @Dict(dicCode = "sex")
	@ApiModelProperty(value = "受托人性别")
	private java.lang.String trusteeSex;
	/**受托人职务*/
	@Excel(name = "受托人职务", width = 15)
	@ApiModelProperty(value = "受托人职务")
	private java.lang.String trusteeDuty;
	/**受托人联系电话*/
	@Excel(name = "受托人联系电话", width = 15)
	@ApiModelProperty(value = "受托人联系电话")
	private java.lang.String trusteePhone;
	/**受托人身份证号*/
	@Excel(name = "受托人身份证号", width = 15)
	@ApiModelProperty(value = "受托人身份证号")
	private java.lang.String trusteeIdNum;
	/**委托起始日期*/
	@Excel(name = "委托起始日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "委托起始日期")
	private java.util.Date beginDate;
	/**委托终止日期*/
	@Excel(name = "委托终止日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "委托终止日期")
	private java.util.Date endDate;
	/**租户ID*/
	@Excel(name = "租户ID", width = 15)
	@ApiModelProperty(value = "租户ID")
	private java.lang.Integer tenantId;
}
