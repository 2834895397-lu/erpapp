package com.dongxin.erp.bd.controller;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dongxin.erp.bd.entity.Material;
import com.dongxin.erp.bd.entity.MaterialType;
import com.dongxin.erp.enums.WbsStatus;
import com.dongxin.erp.ps.entity.TpsWbsModel;
import com.dongxin.erp.ps.service.TpsWbsModelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.BaseController;
import com.dongxin.erp.bd.entity.WbsType;
import com.dongxin.erp.bd.service.WbsTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Description: 项目wbs类型
 * @Author: jeecg-boot
 * @Date:   2021-01-14
 * @Version: V1.0
 */
@Api(tags="项目wbs类型")
@RestController
@RequestMapping("/bd/wbsType")
@Slf4j
public class WbsTypeController extends BaseController<WbsType, WbsTypeService> {
  @Autowired
  WbsTypeService wbsTypeService;
  @Autowired
    TpsWbsModelService tpsWbsModelService;

    @AutoLog("编辑")
    @ApiOperation(
            value = "编辑",
            notes = "编辑"
    )
    @PutMapping({"/edit"})
    public Result<?> edit(@RequestBody WbsType wbsType) {
        List<String> ids=wbsTypeService.edit(wbsType);
            if(CollUtil.isNotEmpty(ids)){
                return Result.error("已被wbs模板使用,不可禁用!");
            }
        wbsTypeService.updateById(wbsType);
        return Result.OK("编辑成功!");
    }


    @AutoLog("通过id删除")
    @ApiOperation(
            value = "通过id删除",
            notes = "通过id删除"
    )
    @DeleteMapping({"/delete"})
    public Result<?> delete(@RequestParam(name = "id",required = true) String id) {
        List<String> ids=wbsTypeService.delete(id);
            if(CollUtil.isNotEmpty(ids)){
                return Result.error("已被wbs模板使用,不可删除!");
            }
        wbsTypeService.removeById(id);
        return Result.OK("删除成功!");
    }

    @AutoLog("批量删除")
    @ApiOperation(
            value = "批量删除",
            notes = "批量删除"
    )
    @DeleteMapping({"/deleteBatch"})
    public Result<?> deleteBatch(@RequestParam(name = "ids",required = true) String ids) {
        List<String> bsIds= new ArrayList<>(Arrays.asList(ids.split(",")));
        List<String> wIds=new ArrayList<>(Arrays.asList(ids.split(",")));
        Set<String> wbsIds=wbsTypeService.deleteBatch(ids);
        if(wbsIds.isEmpty()){
            wbsTypeService.removeByIds(wIds);
            return Result.OK("批量删除成功!");
        }else {
            wIds.removeAll(wbsIds);
            bsIds.removeAll(wIds);
            QueryWrapper<WbsType> wbsTypeQueryWrapper=new QueryWrapper<>();
            wbsTypeQueryWrapper.in("id",bsIds);
            List<String> wbsCodes=wbsTypeService.list(wbsTypeQueryWrapper).stream().map(WbsType::getWbsTypeCode).collect(Collectors.toList());

            return Result.error(wbsCodes.toString().replace("[","").replace("]","") + "已被wbs模板使用,不可删除!");
        }
    }
}

