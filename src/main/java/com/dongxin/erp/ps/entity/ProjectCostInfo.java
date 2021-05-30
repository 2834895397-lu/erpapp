package com.dongxin.erp.ps.entity;

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
 * @Description: 项目费用信息
 * @Author: huangheng
 * @Date:   2021-01-20
 * @Version: V1.0
 */
@Data
@ApiModel(value="ProjectCostInfo对象", description="项目费用信息")
public class ProjectCostInfo implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
	@ApiModelProperty(value = "主键")
	private java.lang.String id;
	
	/**项目*/
	@ApiModelProperty(value = "项目")
	private java.lang.String projectId;
	/**款项类型*/
	@Excel(name = "款项类型", width = 15)
	@ApiModelProperty(value = "款项类型")
	private java.lang.String wbsTypeId;
	
	/**类型名称*/
	@Excel(name = "类型名称", width = 15)
    @ApiModelProperty(value = "类型名称")
    private java.lang.String wbsTypeName;
	
	/**费用金额*/
	@Excel(name = "费用金额", width = 15)
	@ApiModelProperty(value = "费用金额")
	private java.lang.Double cost;
}
