package com.dongxin.erp.cs.vo;


import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@ApiModel(value = "tcs_profile_inf对象", description = "客商基础信息")
public class ProfileTypeLevelHis {

    @TableField(exist = false)
    private java.lang.String csLevel;
    @TableField(exist = false)
    private java.lang.String csType;
    /**生效时间*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @TableField(exist = false)
    private java.util.Date effectiveTime;
}
