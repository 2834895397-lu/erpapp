package com.dongxin.erp.mm.controller;

import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.vo.LoginUser;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import com.dongxin.erp.mm.entity.PlanDtl;
import com.dongxin.erp.mm.entity.Plan;
import com.dongxin.erp.mm.vo.PlanPage;
import com.dongxin.erp.mm.service.PlanService;
import com.dongxin.erp.mm.service.PlanDtlService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 采购申请主表
 * @Author: jeecg-boot
 * @Date:   2020-11-18
 * @Version: V1.0
 */
@Api(tags="采购申请主表")
@RestController
@RequestMapping("/mm/plan")
@Slf4j
public class PlanController {

	@Autowired
    private PlanService planService;

    @Autowired
    private PlanDtlService planDtlService;

	/**
	 * 分页列表查询
	 *
	 * @param plan
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "采购申请主表-分页列表查询")
	@ApiOperation(value="采购申请主表-分页列表查询", notes="采购申请主表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(Plan plan,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<Plan> queryWrapper = QueryGenerator.initQueryWrapper(plan, req.getParameterMap());
		Page<Plan> page = new Page<Plan>(pageNo, pageSize);
		IPage<Plan> pageList = planService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param planPage
	 * @return
	 */
	@AutoLog(value = "采购申请主表-添加")
	@ApiOperation(value="采购申请主表-添加", notes="采购申请主表-添加")
	@PostMapping(value = "/add")
	@RequiresPermissions("plan.add")
	public Result<?> add(@RequestBody PlanPage planPage) {
		Plan plan = new Plan();
		BeanUtils.copyProperties(planPage, plan);
		planService.saveMain(plan, planPage.getPlanDtlList());
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param planPage
	 * @return
	 */
	@AutoLog(value = "采购申请主表-编辑")
	@ApiOperation(value="采购申请主表-编辑", notes="采购申请主表-编辑")
	@PutMapping(value = "/edit")
	@RequiresPermissions("plan.edit")
	public Result<?> edit(@RequestBody PlanPage planPage) {
		Plan plan = new Plan();
		BeanUtils.copyProperties(planPage, plan);
		Plan planEntity = planService.getById(plan.getId());
		if(planEntity==null) {
			return Result.error("未找到对应数据");
		}
		planService.updateMain(plan, planPage.getPlanDtlList());
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "采购申请主表-通过id删除")
	@ApiOperation(value="采购申请主表-通过id删除", notes="采购申请主表-通过id删除")
	@DeleteMapping(value = "/delete")
	@RequiresPermissions("plan.delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		String s = planService.delMain(id);
		return Result.ok(s);
	}

	 /**
	  * 通过主表ids批量逻辑删除主表和附表
	  *
	  * @param ids: 主表的ids, 多个id用,隔开
	  * @return
	  */
	@AutoLog(value = "采购申请主表-批量删除")
	@ApiOperation(value="采购申请主表-批量删除", notes="采购申请主表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	@RequiresPermissions("plan.batchDelete")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		int i = planService.delBatchMain(Arrays.asList(ids.split(",")));
		if(i == 0){
			return Result.error("请选择未审核的记录删除");
		}

		return Result.ok("成功删除" + i + "条记录");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "采购申请主表-通过id查询")
	@ApiOperation(value="采购申请主表-通过id查询", notes="采购申请主表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		Plan plan = planService.getById(id);
		if(plan==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(plan);

	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "采购申请明细表-通过主表ID查询")
	@ApiOperation(value="采购申请明细表-通过主表ID查询", notes="采购申请明细表-通过主表ID查询")
	@GetMapping(value = "/queryPlanDtlByMainId")
	public Result<?> queryPlanDtlListByMainId(@RequestParam(name="id",required=true) String id) {
		List<PlanDtl> planDtlList = planDtlService.selectByMainId(id);
		IPage <PlanDtl> page = new Page<>();
		page.setRecords(planDtlList);
		page.setTotal(planDtlList.size());
		return Result.ok(page);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param plan
    */
	@RequiresPermissions("plan.download")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Plan plan) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<Plan> queryWrapper = QueryGenerator.initQueryWrapper(plan, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //Step.2 获取导出数据
      List<Plan> queryList = planService.list(queryWrapper);
      // 过滤选中数据
      String selections = request.getParameter("selections");
      List<Plan> planList = new ArrayList<Plan>();
      if(oConvertUtils.isEmpty(selections)) {
          planList = queryList;
      }else {
          List<String> selectionList = Arrays.asList(selections.split(","));
          planList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
      }

      // Step.3 组装pageList
      List<PlanPage> pageList = new ArrayList<PlanPage>();
      for (Plan main : planList) {
          PlanPage vo = new PlanPage();
          BeanUtils.copyProperties(main, vo);
          List<PlanDtl> planDtlList = planDtlService.selectByMainId(main.getId());
          vo.setPlanDtlList(planDtlList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "采购申请主表列表");
      mv.addObject(NormalExcelConstants.CLASS, PlanPage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("采购申请主表数据", "导出人:"+sysUser.getRealname(), "采购申请主表"));
      mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
      return mv;
    }

    /**
    * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
	@RequiresPermissions("plan.import")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
      MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
      Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
      for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
          MultipartFile file = entity.getValue();// 获取上传文件对象
          ImportParams params = new ImportParams();
          params.setTitleRows(2);
          params.setHeadRows(1);
          params.setNeedSave(true);
          try {
              List<PlanPage> list = ExcelImportUtil.importExcel(file.getInputStream(), PlanPage.class, params);
              for (PlanPage page : list) {
                  Plan po = new Plan();
                  BeanUtils.copyProperties(page, po);
                  planService.saveMain(po, page.getPlanDtlList());
              }
              return Result.ok("文件导入成功！数据行数:" + list.size());
          } catch (Exception e) {
              log.error(e.getMessage(),e);
              return Result.error("文件导入失败:"+e.getMessage());
          } finally {
              try {
                  file.getInputStream().close();
              } catch (IOException e) {
                  e.printStackTrace();
              }
          }
      }
      return Result.ok("文件导入失败！");
    }

	 @RequiresPermissions("plan.check")
	 @PostMapping("/check")
	 public Result check(@RequestBody ArrayList<Plan> plans) {

		planService.check(plans);
		return Result.OK("审核成功!");

	 }
	 /**
	  * 通过id删除
	  *
	  * @param id
	  * @return
	  */
	 @AutoLog(value = "采购申请请细表-通过id删除")
	 @ApiOperation(value = "采购合同明细表-通过id删除", notes = "采购合同明细表-通过id删除")
	 @DeleteMapping(value = "/deletePlanDtl")
	 public Result<?> deleteContractDtl(@RequestParam(name = "id", required = true) List<String> id) {
		 planService.removeByIds(id);
		 return Result.OK("删除成功!");
	 }






}
