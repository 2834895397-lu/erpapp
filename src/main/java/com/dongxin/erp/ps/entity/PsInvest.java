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
 * @Description: 投资完成进度
 * @Author: jeecg-boot
 * @Date:   2020-11-13
 * @Version: V1.0
 */
@Data
@TableName("tps_invest")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="tps_invest对象", description="投资完成进度")
public class PsInvest implements Serializable {
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
	/**归属项目id*/
	@Excel(name = "归属项目id", width = 15)
    @ApiModelProperty(value = "归属项目id")
    private String projId;
	/**合同id*/
	@Excel(name = "合同id", width = 15)
    @ApiModelProperty(value = "合同id")
    private String contractId;
	/**年度*/
	@Excel(name = "年度", width = 15)
    @ApiModelProperty(value = "年度")
    private Integer year;
	/**月份*/
	@Excel(name = "月份", width = 15)
    @ApiModelProperty(value = "月份")
    private Integer month;
	/**本月投资完成金额*/
	@Excel(name = "本月投资完成金额", width = 15)
    @ApiModelProperty(value = "本月投资完成金额")
    private BigDecimal monthAllInvest;
	/**本月建筑完成金额*/
	@Excel(name = "本月建筑完成金额", width = 15)
    @ApiModelProperty(value = "本月建筑完成金额")
    private BigDecimal monthConstructionInvest;
	/**本月安装完成金额*/
	@Excel(name = "本月安装完成金额", width = 15)
    @ApiModelProperty(value = "本月安装完成金额")
    private BigDecimal monthInstallationInvest;
	/**本月设备完成金额*/
	@Excel(name = "本月设备完成金额", width = 15)
    @ApiModelProperty(value = "本月设备完成金额")
    private BigDecimal monthEquipmentInvest;
	/**本月其他完成金额*/
	@Excel(name = "本月其他完成金额", width = 15)
    @ApiModelProperty(value = "本月其他完成金额")
    private BigDecimal monthOtherInvest;
	/**本年投资完成金额*/
	@Excel(name = "本年投资完成金额", width = 15)
    @ApiModelProperty(value = "本年投资完成金额")
    private BigDecimal yearAllInvest;
	/**本年建筑完成金额*/
	@Excel(name = "本年建筑完成金额", width = 15)
    @ApiModelProperty(value = "本年建筑完成金额")
    private BigDecimal yearConstructionInvest;
	/**本年安装完成金额*/
	@Excel(name = "本年安装完成金额", width = 15)
    @ApiModelProperty(value = "本年安装完成金额")
    private BigDecimal yearInstallationInvest;
	/**本年设备完成金额*/
	@Excel(name = "本年设备完成金额", width = 15)
    @ApiModelProperty(value = "本年设备完成金额")
    private BigDecimal yearEquipmentInvest;
	/**本年其他完成金额*/
	@Excel(name = "本年其他完成金额", width = 15)
    @ApiModelProperty(value = "本年其他完成金额")
    private BigDecimal yearOtherInvest;
	/**累计投资完成金额*/
	@Excel(name = "累计投资完成金额", width = 15)
    @ApiModelProperty(value = "累计投资完成金额")
    private BigDecimal totalAllInvest;
	/**累计建筑完成金额*/
	@Excel(name = "累计建筑完成金额", width = 15)
    @ApiModelProperty(value = "累计建筑完成金额")
    private BigDecimal totalConstructionInvest;
	/**累计安装完成金额*/
	@Excel(name = "累计安装完成金额", width = 15)
    @ApiModelProperty(value = "累计安装完成金额")
    private BigDecimal totalInstallationInvest;
	/**累计设备完成金额*/
	@Excel(name = "累计设备完成金额", width = 15)
    @ApiModelProperty(value = "累计设备完成金额")
    private BigDecimal totalEquipmentInvest;
	/**累计其他完成金额*/
	@Excel(name = "累计其他完成金额", width = 15)
    @ApiModelProperty(value = "累计其他完成金额")
    private BigDecimal totalOtherInvest;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String reamark;
}
