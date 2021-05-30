package com.dongxin.erp.sm.entity;

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
 * @Description: 出入库流水表
 * @Author: jeecg-boot
 * @Date:   2020-11-10
 * @Version: V1.0
 */
@Data
@TableName("tsm_waste_book")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="tsm_waste_book对象", description="出入库流水表")
public class WasteBook implements Serializable {
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
    private String tenantId;
	/**库存地*/
	@Excel(name = "库存地", width = 15)
    @ApiModelProperty(value = "库存地")
    @Dict(dicCode = "id",dictTable = "tbd_node",dicText = "name")
    private String toTbdNodeId;
	/**单据类型*/
	@Excel(name = "单据类型", width = 15, dicCode = "tsm_waste_book_type")
	@Dict(dicCode = "tsm_waste_book_type")
    @ApiModelProperty(value = "单据类型")
    private String type;
	/**单据号*/
	@Excel(name = "单据号", width = 15)
    @ApiModelProperty(value = "单据号")
    private String orderId;
	/**物料*/
	@Excel(name = "物料", width = 15)
    @ApiModelProperty(value = "物料")
    @Dict(dicCode = "id",dictTable = "tbd_material",dicText = "name")
    private String tbdMaterialId;
	/**过账日期*/
	@Excel(name = "审核日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "审核日期")
    private Date postTime;
	/**数量*/
	@Excel(name = "入库数量", width = 15)
    @ApiModelProperty(value = "入库数量")
    private Integer inQty;
	/**数量*/
	@Excel(name = "出库数量", width = 15)
    @ApiModelProperty(value = "出库数量")
    private Integer outQty;
	/**单价*/
	@Excel(name = "单价", width = 15)
    @ApiModelProperty(value = "单价")
    private Double matlPrice;
	/**出入库方向*/
	@Excel(name = "出入库方向", width = 15, dicCode = "matl_direct_type")
	@Dict(dicCode = "matl_direct_type")
    @ApiModelProperty(value = "出入库方向")
    private String matlDirect;
    /**币别*/
    @Excel(name = "币别", width = 15)
    @ApiModelProperty(value = "币别")
    @Dict(dicCode = "fm_pay_bb")
    private java.lang.String payBb;

    /**业务ID*/
    @Excel(name = "业务ID", width = 15)
    @ApiModelProperty(value = "业务ID")
    @TableField(fill = FieldFill.INSERT)
    private java.lang.String busiId;
}
