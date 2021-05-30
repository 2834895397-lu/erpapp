package com.dongxin.erp.mm.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import java.util.Date;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 采购申请明细表
 * @Author: jeecg-boot
 * @Date:   2020-11-18
 * @Version: V1.0
 */
@ApiModel(value="tmm_plan对象", description="采购申请主表")
@Data
@TableName("tmm_plan_dtl")
public class PlanDtl implements Serializable {
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
	/**采购申请主表ID*/
	@ApiModelProperty(value = "采购申请主表ID")
	private java.lang.String tmmPlanId;
	/**物料ID*/
	@Excel(name = "物料ID", width = 15, dictTable = "tbd_material", dicText = "name", dicCode = "id")
	@Dict(dictTable = "tbd_material", dicText = "name", dicCode = "id")
	@ApiModelProperty(value = "物料ID")
	private java.lang.String tbdMaterialId;
	/**短文本*/
	@Excel(name = "短文本", width = 15)
	@ApiModelProperty(value = "短文本")
	private java.lang.String shortText;
	/**数量*/
	@Excel(name = "数量", width = 15)
	@ApiModelProperty(value = "数量")
	private java.lang.Integer matlQty;
	/**采购申请来源*/
	@Excel(name = "采购申请来源", width = 15, dicCode = "tmm_apply_from_type")
	@Dict(dicCode = "tmm_apply_from_type")
	@ApiModelProperty(value = "采购申请来源")
	private java.lang.String applyFrom;
	/**采购申请来源号*/
	@Excel(name = "采购申请来源号", width = 15)
	@ApiModelProperty(value = "采购申请来源号")
	private java.lang.String applyFromNo;
	/**备注*/
	@Excel(name = "备注", width = 15)
	@ApiModelProperty(value = "备注")
	private java.lang.String remark;
}
