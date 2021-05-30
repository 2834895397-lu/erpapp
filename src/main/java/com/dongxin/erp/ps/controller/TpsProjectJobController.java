package com.dongxin.erp.ps.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.BaseController;
import com.dongxin.erp.ps.entity.TpsProjectJob;
import com.dongxin.erp.ps.service.TpsProjectJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Description: 项目作业
 * @Author: jeecg-boot
 * @Date:   2021-01-21
 * @Version: V1.0
 */
@Api(tags="项目作业")
@RestController
@RequestMapping("/ps/tpsProjectJob")
@Slf4j
public class TpsProjectJobController extends BaseController<TpsProjectJob, TpsProjectJobService> {

 @Autowired
 private TpsProjectJobService projectJobService;


 @GetMapping("/jobTree")
// @RequiresPermissions()
 public Result<?> jobTree(@RequestParam String id){
  return projectJobService.jobTree(id);
 }

 @GetMapping("/materialInfo")
 // @RequiresPermissions()
 public Result<?> materialInfo(@RequestParam String id){
  return projectJobService.materialInfo(id);
 }

 @Override
 // @RequiresPermissions()
 public Result<?> edit(@RequestBody TpsProjectJob param) {
  return super.edit(param);
 }

 @Override
 // @RequiresPermissions()
 public Result<?> add(@RequestBody TpsProjectJob param) {
  param.setPurchaseStatus(0);
  return super.add(param);
 }

 @PutMapping("/orderPlan")
// @RequiresPermissions()
 public Result<?>orderPlan(@RequestParam("id") String id){
  return projectJobService.orderPlan(id);
 }
}
