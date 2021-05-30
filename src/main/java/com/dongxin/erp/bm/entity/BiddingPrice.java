package com.dongxin.erp.bm.entity;

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
 * @Description: 招标报价表
 * @Author: jeecg-boot
 * @Date:   2020-11-25
 * @Version: V1.0
 */
@Data
@TableName("tbm_bidding_price")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="tbm_bidding_price对象", description="招标报价表")
public class BiddingPrice implements Serializable {
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
	/**招投明细ID*/
	@Excel(name = "招投明细ID", width = 15, dictTable = "tbm_bidding_dtl", dicText = "id", dicCode = "id")
	@Dict(dictTable = "tbm_bidding_dtl", dicText = "id", dicCode = "id")
    @ApiModelProperty(value = "招投明细ID")
    private java.lang.String biddingDetailId;
	/**招标企业ID*/
	@Excel(name = "招标企业ID", width = 15, dictTable = "tbm_bidding_enterprise", dicText = "id", dicCode = "id")
	@Dict(dictTable = "tbm_bidding_enterprise", dicText = "id", dicCode = "id")
    @ApiModelProperty(value = "招标企业ID")
    private java.lang.String biddingEnterpriseId;
	/**报价*/
	@Excel(name = "报价", width = 15)
    @ApiModelProperty(value = "报价")
    private java.lang.Double offerPrice;
	/**报价数量*/
	@Excel(name = "报价数量", width = 15)
    @ApiModelProperty(value = "报价数量")
    private java.lang.Double offerNum;
	/**报价日期*/
	@Excel(name = "报价日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "报价日期")
    private java.util.Date offerDate;
	/**中标标识*/
	@Excel(name = "中标标识", width = 15, dicCode = "bm_bidding_flag")
	@Dict(dicCode = "bm_bidding_flag")
    @ApiModelProperty(value = "中标标识")
    private java.lang.String biddingFlag;
}
