package com.dongxin.erp.ps.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dongxin.erp.ps.entity.PsBudget;
import com.dongxin.erp.ps.entity.PsWbs;
import com.dongxin.erp.ps.entity.PsWbsNode;
import com.dongxin.erp.ps.service.PsWbsNodeService;
import com.dongxin.erp.ps.service.PsWbsService;
import com.dongxin.erp.ps.vo.PsPojWbsNodeVo;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.BaseController;
import com.dongxin.erp.ps.entity.PsProjInfo;
import com.dongxin.erp.ps.service.PsProjInfoService;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.system.entity.SysDictItem;
import org.jeecg.modules.system.service.ISysDictItemService;
import org.jeecg.modules.system.service.ISysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 项目基础信息
 * @Author: ljc
 * @Date:   2020-11-13
 * @Version: V1.0
 */
@Api(tags="项目基础信息")
@RestController
@RequestMapping("/ps/psProjInfo")
@Slf4j
public class PsProjInfoController extends BaseController<PsProjInfo, PsProjInfoService> {

 @Autowired
 private PsProjInfoService infoService;


 @GetMapping("/getWbstree")
// @RequiresPermissions()
 public Result getWbstree(@RequestParam(name="id") String id, @RequestParam(name="tenantid") String tenantid){
  return Result.OK(infoService.getWbstree(id,tenantid));
 }


@GetMapping("/queryProjWbsActi")
//@RequiresPermissions()
 public Result queryProjWbsActi(@RequestParam(name="projId") String projId,
                                @RequestParam(name="tenantid") String tenantid,
                                @RequestParam(name="WbsId") String WbsId,
                                PsBudget param,
                                @RequestParam(name = "pageNo",defaultValue = "1") Integer pageNo,
                                @RequestParam(name = "pageSize",defaultValue = "10") Integer pageSize,
                                HttpServletRequest req) {
 return Result.OK(infoService.queryProjWbsActi(projId, tenantid, WbsId, param, pageNo, pageSize, req));
}


}
