package com.dongxin.erp.bd.entity;

import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;
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
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 物料信息
 * @Author: jeecg-boot
 * @Date:   2020-11-10
 * @Version: V1.0
 */
@Data
@TableName("tbd_material")
@ApiModel(value="tbd_material对象", description="物料信息")
public class Material implements Serializable {
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
	/**父级节点*/
	@Excel(name = "父级节点", width = 15)
	@ApiModelProperty(value = "父级节点")
	private String pid;
	/**是否有子节点*/
	@Excel(name = "是否有子节点", width = 15, dicCode = "yn")
	@Dict(dicCode = "yn")
	@ApiModelProperty(value = "是否有子节点")
	private String hasChild;
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
	/**物料编码*/
	@Excel(name = "物料编码", width = 15)
	@ApiModelProperty(value = "物料编码")
	private String code;
	/**物料名称*/
	@Excel(name = "物料名称", width = 15)
	@ApiModelProperty(value = "物料名称")
	private String name;
	/**物料层级*/
	@Excel(name = "物料层级", width = 15)
	@ApiModelProperty(value = "物料层级")
	@Dict(dicCode = "tbd_node_level")
	private String materLevel;
	/**单位*/
	@Excel(name = "单位", width = 15, dictTable = "tbd_unit", dicText = "name", dicCode = "id")
	@Dict(dictTable = "tbd_unit", dicText = "name", dicCode = "id")
	@ApiModelProperty(value = "单位")
	private String tbdUnitId;
	/**物料类型*/
	@Excel(name = "物料类型", width = 15, dictTable = "tbd_material_type", dicText = "material_type_name", dicCode = "id")
	@Dict(dictTable = "tbd_material_type", dicText = "material_type_name", dicCode = "id")
	@ApiModelProperty(value = "物料类型")
	private String materialTypeId;
}
