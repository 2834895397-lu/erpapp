package com.dongxin.erp.sm.vo;

import java.util.List;
import com.dongxin.erp.sm.entity.MatlOutOrder;
import com.dongxin.erp.sm.entity.MatlOutOrderDtl;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelEntity;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 领用单主表
 * @Author: jeecg-boot
 * @Date:   2020-11-10
 * @Version: V1.0
 */
@Data
@ApiModel(value="tsm_matl_out_orderPage对象", description="领用单主表")
public class MatlOutOrderPage {

	/**主键*/
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
	@ApiModelProperty(value = "更新人")
	private String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
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
	@ApiModelProperty(value = "版本")
	private Integer ver;
	/**租户ID*/
	@Excel(name = "租户ID", width = 15)
	@ApiModelProperty(value = "租户ID")
	private String tenantId;
	/**类型*/
	@Excel(name = "类型", width = 15, dicCode = "tsm_matl_out_order_type")
    @Dict(dicCode = "tsm_matl_out_order_type")
	@ApiModelProperty(value = "类型")
	private String type;
	/**凭证日期*/
	@Excel(name = "凭证日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "凭证日期")
	private Date voucherTime;
	/**状态*/
	@Excel(name = "状态", width = 15, dicCode = "is_check")
    @Dict(dicCode = "is_check")
	@ApiModelProperty(value = "状态")
	private String status;
	/**过账日期*/
	@Excel(name = "过账日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "过账日期")
	private java.util.Date postingDate;
	
	@ExcelCollection(name="领用单明细表")
	@ApiModelProperty(value = "领用单明细表")
	private List<MatlOutOrderDtl> matlOutOrderDtlList;
	
}
