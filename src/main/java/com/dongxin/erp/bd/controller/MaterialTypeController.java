package com.dongxin.erp.bd.controller;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dongxin.erp.bd.entity.PurchaseOrg;
import com.dongxin.erp.bd.service.PurchaseOrgService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.BaseController;
import com.dongxin.erp.bd.entity.MaterialType;
import com.dongxin.erp.bd.service.MaterialTypeService;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.system.mapper.SysDepartMapper;
import org.jeecg.modules.system.service.impl.SysDepartServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;


/**
 * @Description: 物料类型表
 * @Author: jeecg-boot
 * @Date:   2021-01-14
 * @Version: V1.0
 */
@Api(tags="物料类型表")
@RestController
@RequestMapping("/bd/materialType")
@Slf4j
public class MaterialTypeController extends BaseController<MaterialType, MaterialTypeService> {
 @Autowired
 PurchaseOrgService purchaseOrgService;
 @Autowired
 SysDepartServiceImpl sysDepartService;
 @Autowired
 SysDepartMapper sysDepartMapper;
 @Autowired
 MaterialTypeService materialTypeService;

 @AutoLog("分页列表查询")
 @ApiOperation(
         value = "分页列表查询",
         notes = "分页列表查询"
 )
 @GetMapping({"/list"})
 public Result<?> queryPageList(MaterialType materialType, @RequestParam(name = "pageNo",defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize",defaultValue = "10") Integer pageSize, HttpServletRequest req) {
  QueryWrapper<MaterialType> queryWrapper = QueryGenerator.initQueryWrapper(materialType, req.getParameterMap());
  Page<MaterialType> page = new Page((long)pageNo, (long)pageSize);
  IPage<MaterialType> pageList = materialTypeService.page(page, queryWrapper);
//列表页显示采购组织对应的名称(有简称优先显示简称,无简称就显示全称)
  for (int i = 0; i < pageList.getRecords().size(); i++) {
   String ids= pageList.getRecords().get(i).getOrgId();
   String purchaseOrgs=purchaseOrgService.selectMaterialType(ids);
    if(purchaseOrgs!=null){
     pageList.getRecords().get(i).setOrgId(purchaseOrgs);
    }else{
     String departName=purchaseOrgService.selectDepartName(ids);
     pageList.getRecords().get(i).setOrgId(departName);
    /*Map<String,String> map=new HashMap<>();
    map.put(MapUtil.getStr(departName, "id"), MapUtil.getStr(departName, "depart_name"));
     pageList.getRecords().get(i).setOrgId(map.get(departName));*/
    }

  }
  return Result.OK(pageList);
 }



 @GetMapping("/selectDepart")
 public Result selectDepart() {
  /* List<String> departIds = purchaseOrgService.list().stream().map(PurchaseOrg::getDepartId).collect(Collectors.toList());
   Map<String, String> map = sysDepartMapper.selectBatchIds(departIds).stream().collect(Collectors.toMap(SysDepart::getId, SysDepart::getDepartName));*/
  /* Map<String,String> idsAndNames=purchaseOrgService.list().stream().collect(Collectors.toMap(PurchaseOrg::getDepartId,PurchaseOrg::getOrgName));
   idsAndNames.forEach(MaterialTypeController::a);*/
  /*  *//* map.forEach(MaterialTypeController::a);

   map.forEach(new BiConsumer<String, String>() {
    @Override
    public void accept(String s, String s2) {
      a(s,s2);
    }
   });
   *//*
   Map<String,String> idsAndNames=purchaseOrgService.list().stream().collect(Collectors.toMap(PurchaseOrg::getDepartId,PurchaseOrg::getOrgName));
   */
  //表单页显示采购组织名称(有简称优先显示简称,无简称就显示全称)
  Map<String,String> map1=new HashMap<>();
  List<PurchaseOrg> purchaseOrgList = purchaseOrgService.list();
  for (int i = 0; i < purchaseOrgList.size(); i++) {
   String id=purchaseOrgList.get(i).getDepartId();
   String orgName= purchaseOrgList.get(i).getOrgName();
   if(orgName==null){
    Map<String, String> idAndName = purchaseOrgService.selectDepart(id);
    map1.put(MapUtil.getStr(idAndName, "id"), MapUtil.getStr(idAndName, "depart_name"));
   }else{
    map1.put(id,orgName);
   }
   }


   return Result.OK(map1);
  }


}
 /* public static void a(String key, String value){
   System.out.println(key+"-->"+ value);

  }*/

