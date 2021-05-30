package com.dongxin.erp.mm.vo;

import java.util.List;
import com.dongxin.erp.mm.entity.Contract;
import com.dongxin.erp.mm.entity.ContractDtl;
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
 * @Description: 采购合同主表
 * @Author: jeecg-boot
 * @Date:   2020-11-18
 * @Version: V1.0
 */
@Data
@ApiModel(value="tmm_contractPage对象", description="采购合同主表")
public class ContractPage {

	/**主键*/
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
	@ApiModelProperty(value = "更新人")
	private java.lang.String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
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
	@ApiModelProperty(value = "版本")
	private java.lang.Integer ver;
	/**租户ID*/
	@Excel(name = "租户ID", width = 15)
	@ApiModelProperty(value = "租户ID")
	private java.lang.Integer tenantId;
	/**合同号*/
	@Excel(name = "合同号", width = 15)
	@ApiModelProperty(value = "合同号")
	private java.lang.String contractNo;
	/**合同名称*/
	@Excel(name = "合同名称", width = 15)
	@ApiModelProperty(value = "合同名称")
	private java.lang.String contractName;
	/**合同类型*/
	@Excel(name = "合同类型", width = 15, dicCode = "tmm_contract_type")
    @Dict(dicCode = "tmm_contract_type")
	@ApiModelProperty(value = "合同类型")
	private java.lang.String type;
	/**凭证日期*/
	@Excel(name = "凭证日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "凭证日期")
	private java.util.Date voucherTime;
	/**签订日期*/
	@Excel(name = "签订日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "签订日期")
	private java.util.Date signDate;
	/**签订地点*/
	@Excel(name = "签订地点", width = 15)
	@ApiModelProperty(value = "签订地点")
	private java.lang.String signPlace;
	/**合同有效期起*/
	@Excel(name = "合同有效期起", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "合同有效期起")
	private java.util.Date validDateFrom;
	/**合同有效期止*/
	@Excel(name = "合同有效期止", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "合同有效期止")
	private java.util.Date validDateTo;
	/**供方ID*/
	@Excel(name = "供方ID", width = 15)
	@ApiModelProperty(value = "供方ID")
	private java.lang.String tcsProfileInfId;
	/**需方（公司）*/
	@Excel(name = "需方（公司）", width = 15)
	@ApiModelProperty(value = "需方（公司）")
	private java.lang.String tbdCompanyId;
	/**采购组织*/
	@Excel(name = "采购组织", width = 15)
	@ApiModelProperty(value = "采购组织")
	private java.lang.String purchaseOrgn;
	/**状态*/
	@Excel(name = "状态", width = 15, dicCode = "is_check")
    @Dict(dicCode = "is_check")
	@ApiModelProperty(value = "状态")
	private java.lang.String status;
	/**备注*/
	@Excel(name = "备注", width = 15)
	@ApiModelProperty(value = "备注")
	private java.lang.String remark;
	
	@ExcelCollection(name="采购合同明细表")
	@ApiModelProperty(value = "采购合同明细表")
	private List<ContractDtl> contractDtlList;

	/**完结标识*/
	@Excel(name = "完结标识", width = 15)
	@ApiModelProperty(value = "完结标识")
	@Dict(dicCode = "tmm_cont_end_flag")
	private java.lang.String contEndFlag;
	/**供方编码*/
	@Excel(name = "供方编码", width = 15)
	@ApiModelProperty(value = "供方编码")
	private java.lang.String tcsProfileInfCode;
	/**供方名称*/
	@Excel(name = "供方名称", width = 15)
	@ApiModelProperty(value = "供方名称")
	private java.lang.String tcsProfileInfName;
	
}
