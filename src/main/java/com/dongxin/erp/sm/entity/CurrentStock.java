package com.dongxin.erp.sm.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;

@Data
public class CurrentStock {
    @Excel(name = "库存地ID", width = 15)
    @ApiModelProperty(value = "库存地")
    private String ToTbdNodeId;
    @Excel(name = "库存地名称", width = 15)
    @ApiModelProperty(value = "库存地")
    private String ToTbdNodeName;
    @Excel(name = "物料ID", width = 15)
    @ApiModelProperty(value = "物料")
    private String TbdMaterialId;
    @Excel(name = "物料名称", width = 15)
    @ApiModelProperty(value = "物料")
    private String TbdMaterialName;
    @Excel(name = "入库数量", width = 15)
    @ApiModelProperty(value = "数量")
    private Integer count;
    @Excel(name = "租户ID", width = 15)
    @ApiModelProperty(value = "租户ID")
    private String tenantId;
}
