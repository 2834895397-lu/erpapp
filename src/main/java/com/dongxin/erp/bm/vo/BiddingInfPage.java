package com.dongxin.erp.bm.vo;

import java.util.List;
import com.dongxin.erp.bm.entity.BiddingInf;
import com.dongxin.erp.bm.entity.BiddingDtl;
import com.dongxin.erp.bm.entity.BiddingEnterprise;
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
 * @Description: 招标信息表
 * @Author: jeecg-boot
 * @Date:   2020-11-25
 * @Version: V1.0
 */
@Data
@ApiModel(value="tbm_bidding_infPage对象", description="招标信息表")
public class BiddingInfPage {

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
	/**招投标号*/
	@Excel(name = "招投标号", width = 15)
	@ApiModelProperty(value = "招投标号")
	private java.lang.String biddingNo;
	/**分类*/
	@Excel(name = "分类", width = 15, dicCode = "bm_bidding_type")
    @Dict(dicCode = "bm_bidding_type")
	@ApiModelProperty(value = "分类")
	private java.lang.String biddingType;
	/**采购标题*/
	@Excel(name = "采购标题", width = 15)
	@ApiModelProperty(value = "采购标题")
	private java.lang.String biddingTitle;
	/**采购类型*/
	@Excel(name = "采购类型", width = 15, dicCode = "bm_bidding_category")
    @Dict(dicCode = "bm_bidding_category")
	@ApiModelProperty(value = "采购类型")
	private java.lang.String biddingCategory;
	/**采购方式*/
	@Excel(name = "采购方式", width = 15, dicCode = "bm_purchase_type")
    @Dict(dicCode = "bm_purchase_type")
	@ApiModelProperty(value = "采购方式")
	private java.lang.String biddingWay;
	/**申请单位*/
	@Excel(name = "申请单位", width = 15)
	@ApiModelProperty(value = "申请单位")
	private java.lang.String biddingDepaterment;
	/**申请人*/
	@Excel(name = "申请人", width = 15)
	@ApiModelProperty(value = "申请人")
	private java.lang.String biddingMan;
	/**开标时间*/
	@Excel(name = "开标时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "开标时间")
	private java.util.Date openBiddingDate;
	/**截标时间*/
	@Excel(name = "截标时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "截标时间")
	private java.util.Date endBiddingDate;
	/**申请时间*/
	@Excel(name = "申请时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "申请时间")
	private java.util.Date biddingDate;
	/**状态*/
	@Excel(name = "状态", width = 15, dicCode = "bm_bidding_status")
    @Dict(dicCode = "bm_bidding_status")
	@ApiModelProperty(value = "状态")
	private java.lang.String biddingFlag;
	/**第三方公司*/
	@Excel(name = "第三方公司", width = 15)
	@ApiModelProperty(value = "第三方公司")
	private java.lang.String biddingCompany;
	/**备注*/
	@Excel(name = "备注", width = 15)
	@ApiModelProperty(value = "备注")
	private java.lang.String remark;
	
	@ExcelCollection(name="招标明细信息表")
	@ApiModelProperty(value = "招标明细信息表")
	private List<BiddingDtl> biddingDtlList;
	@ExcelCollection(name="招标企业信息表")
	@ApiModelProperty(value = "招标企业信息表")
	private List<BiddingEnterprise> biddingEnterpriseList;
	
	@ExcelCollection(name="附件地址")
	@ApiModelProperty(value = "附件地址")
	private String url;
	
}
