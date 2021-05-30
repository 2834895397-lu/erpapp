package com.dongxin.erp.ps.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.BaseController;
import com.dongxin.erp.ps.entity.PsMaterialPlan;
import com.dongxin.erp.ps.service.PsMaterialPlanService;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Description: 项目作业物料采购
 * @Author: jeecg-boot
 * @Date:   2021-01-21
 * @Version: V1.0
 */
@Api(tags="项目作业物料采购")
@RestController
@RequestMapping("/ps/psMaterialPlan")
@Slf4j
public class PsMaterialPlanController extends BaseController<PsMaterialPlan, PsMaterialPlanService> {

 @Autowired
 private PsMaterialPlanService materialPlanService;

 @Override
 public Result<?> add(@RequestBody PsMaterialPlan param) {
  return super.add(param);
 }

 @PutMapping("update")
// @RequiresPermissions()
 public Result<?> update(@RequestBody List<PsMaterialPlan> list){
  return  materialPlanService.batchUpdate(list);
 }

}
