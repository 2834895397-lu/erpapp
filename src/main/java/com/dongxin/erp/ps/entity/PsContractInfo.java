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
 * @Description: 工程合同信息
 * @Author: jeecg-boot
 * @Date:   2020-11-13
 * @Version: V1.0
 */
@Data
@TableName("tps_contract_info")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="tps_contract_info对象", description="工程合同信息")
public class PsContractInfo implements Serializable {
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
	/**合同编号*/
	@Excel(name = "合同编号", width = 15)
    @ApiModelProperty(value = "合同编号")
    private String code;
	/**合同名称*/
	@Excel(name = "合同名称", width = 15)
    @ApiModelProperty(value = "合同名称")
    private String name;
	/**项目id*/
	@Excel(name = "项目id", width = 15)
    @ApiModelProperty(value = "项目id")
    private String projId;
	/**是否年度合同*/
	@Excel(name = "是否年度合同", width = 15)
    @ApiModelProperty(value = "是否年度合同")
    private String annualFlag;
	/**开始时间*/
	@Excel(name = "开始时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "开始时间")
    private Date beginDate;
	/**结束时间*/
	@Excel(name = "结束时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "结束时间")
    private Date finishDate;
	/**建筑金额*/
	@Excel(name = "建筑金额", width = 15)
    @ApiModelProperty(value = "建筑金额")
    private BigDecimal constructionAmount;
	/**安装金额*/
	@Excel(name = "安装金额", width = 15)
    @ApiModelProperty(value = "安装金额")
    private BigDecimal installAmount;
	/**设备金额*/
	@Excel(name = "设备金额", width = 15)
    @ApiModelProperty(value = "设备金额")
    private BigDecimal equipmentAmount;
	/**设计金额*/
	@Excel(name = "设计金额", width = 15)
    @ApiModelProperty(value = "设计金额")
    private BigDecimal designAmount;
	/**其他金额*/
	@Excel(name = "其他金额", width = 15)
    @ApiModelProperty(value = "其他金额")
    private BigDecimal otherAmount;
	/**甲方*/
	@Excel(name = "甲方", width = 15)
    @ApiModelProperty(value = "甲方")
    private String firstParty;
	/**乙方*/
	@Excel(name = "乙方", width = 15)
    @ApiModelProperty(value = "乙方")
    private String sencondParty;
	/**是否总包合同*/
	@Excel(name = "是否总包合同", width = 15)
    @ApiModelProperty(value = "是否总包合同")
    private String epcFlag;
	/**签订日期*/
	@Excel(name = "签订日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "签订日期")
    private Date signDate;
	/**签订地点*/
	@Excel(name = "签订地点", width = 15)
    @ApiModelProperty(value = "签订地点")
    private String signPlace;
	/**结算方式*/
	@Excel(name = "结算方式", width = 15)
    @ApiModelProperty(value = "结算方式")
    private String balanceMode;
	/**状态*/
	@Excel(name = "状态", width = 15)
    @ApiModelProperty(value = "状态")
    private String status;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String remark;
}
