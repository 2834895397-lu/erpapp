package com.dongxin.erp.ps.controller;

import org.jeecg.common.system.query.QueryGenerator;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.api.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import java.util.Arrays;
import org.jeecg.common.util.oConvertUtils;
import com.dongxin.erp.ps.entity.ProjectCost;
import com.dongxin.erp.ps.entity.ProjectCostInfo;
import com.dongxin.erp.ps.entity.ProjectSaveEntity;
import com.dongxin.erp.exception.ErpException;
import com.dongxin.erp.ps.entity.Project;
import com.dongxin.erp.ps.service.ProjectService;
import com.dongxin.erp.ps.service.ProjectCostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description: 项目基础信息表
 * @Author: jeecg-boot/huangheng
 * @Date: 2021-01-15
 * @Version: V1.0
 */
@Api(tags = "项目基础信息表")
@RestController
@RequestMapping("/ps/project")
@Slf4j
public class ProjectController extends JeecgController<Project, ProjectService> {

	@Autowired
	private ProjectService projectService;

	@Autowired
	private ProjectCostService projectCostService;

	/**
	 * 分页列表查询
	 * 
	 * @param project
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "项目基础信息表-分页列表查询")
	@ApiOperation(value = "项目基础信息表-分页列表查询", notes = "项目基础信息表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(Project project, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
		QueryWrapper<Project> queryWrapper = QueryGenerator.initQueryWrapper(project, req.getParameterMap());
		Page<Project> page = new Page<Project>(pageNo, pageSize);
		IPage<Project> pageList = projectService.page(page, queryWrapper);
		pageList.setRecords(projectService.setContractInfo(pageList.getRecords()));
		return Result.OK(pageList);
	}

	/**
	 * 添加
	 * 
	 * @param project
	 * @return
	 */
	@AutoLog(value = "项目基础信息表-添加")
	@ApiOperation(value = "项目基础信息表-添加", notes = "项目基础信息表-添加")
	@PostMapping(value = "/add")
	@RequiresPermissions("ps:project:handleAdd")
	public Result<?> add(@RequestBody ProjectSaveEntity project) {
		projectService.add(project);
		return Result.OK("添加成功！");
	}

	/**
	 * 编辑
	 * 
	 * @param project
	 * @return
	 */
	@AutoLog(value = "项目基础信息表-编辑")
	@ApiOperation(value = "项目基础信息表-编辑", notes = "项目基础信息表-编辑")
	@PutMapping(value = "/edit")
	@RequiresPermissions("ps:project:handleEdit")
	public Result<?> edit(@RequestBody ProjectSaveEntity project) {
		projectService.edit(project);
		return Result.OK("编辑成功!");
	}

	/**
	 * 通过id删除
	 * 
	 * @param id
	 * @return
	 */
	@AutoLog(value = "项目基础信息表-通过id删除")
	@ApiOperation(value = "项目基础信息表-通过id删除", notes = "项目基础信息表-通过id删除")
	@DeleteMapping(value = "/delete")
	@RequiresPermissions("ps:project:handleDelete")
	public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
		projectService.delMain(id);
		return Result.OK("删除成功!");
	}

	/**
	 * 批量删除
	 * 
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "项目基础信息表-批量删除")
	@ApiOperation(value = "项目基础信息表-批量删除", notes = "项目基础信息表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	@RequiresPermissions("ps:project:batchDel")
	public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
		this.projectService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 导出
	 * 
	 * @return
	 */
	@RequestMapping(value = "/exportXls")
	@RequiresPermissions("ps:project:handleExportXls")
	public ModelAndView exportXls(HttpServletRequest request, Project project) {
		return super.exportXls(request, project, Project.class, "项目基础信息表");
	}

	/**
	 * 审核功能
	 * 
	 * @param projectList
	 * @return
	 */
	@AutoLog(value = "项目基础信息表-审核")
	@ApiOperation(value = "项目基础信息表-审核", notes = "项目基础信息表-审核")
	@PostMapping("/check")
	@RequiresPermissions("ps:project:handleCheck")
	public Result<Object> check(@RequestBody List<Project> projectList) {
		try {
			projectService.check(projectList);
		} catch (ErpException e) {
			return Result.OK(e.getMessage());
		}
		return Result.OK("审核成功!");
	}

	/**
	 * 获取项目详细信息
	 * 
	 * @param id
	 * @return
	 */
	@AutoLog(value = "项目管理-获取项目详细信息")
	@ApiOperation(value = "项目管理-获取项目详细信息", notes = "项目管理-获取项目详细信息")
	@GetMapping("/getProjectInfo")
	public Result<Object> getProjectInfo(@RequestParam(name = "id", required = true) String id) {
		return Result.OK(projectService.getProjectInfo(id));
	}

	/**
	 * 通过主表ID查询费用明细
	 * 
	 * @return
	 */
	@AutoLog(value = "项目费用表-通过主表ID查询")
	@ApiOperation(value = "项目费用表-通过主表ID查询", notes = "项目费用表-通过主表ID查询")
	@GetMapping(value = "/listProjectCostByMainId")
	public Result<?> listProjectCostByMainId(ProjectCost projectCost,
			@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
		QueryWrapper<ProjectCost> queryWrapper = QueryGenerator.initQueryWrapper(projectCost, req.getParameterMap());
		Page<ProjectCost> page = new Page<ProjectCost>(pageNo, pageSize);
		IPage<ProjectCost> pageList = projectCostService.page(page, queryWrapper);
		pageList.setRecords(projectCostService.getWbsTypeName(pageList.getRecords()));
		return Result.OK(pageList);
	}

	/**
	 * 添加
	 * 
	 * @param projectCost
	 * @return
	 */
//	@AutoLog(value = "项目费用表-添加")
//	@ApiOperation(value = "项目费用表-添加", notes = "项目费用表-添加")
//	@PostMapping(value = "/addProjectCost")
//	public Result<?> addProjectCost(@RequestBody ProjectCost projectCost) {
//		projectCostService.save(projectCost);
//		return Result.OK("添加成功！");
//	}

	/**
	 * 编辑
	 * 
	 * @param projectCost
	 * @return
	 */
//	@AutoLog(value = "项目费用表-编辑")
//	@ApiOperation(value = "项目费用表-编辑", notes = "项目费用表-编辑")
//	@PutMapping(value = "/editProjectCost")
//	public Result<?> editProjectCost(@RequestBody ProjectCost projectCost) {
//		projectCostService.updateById(projectCost);
//		return Result.OK("编辑成功!");
//	}

	/**
	 * 通过id删除
	 * 
	 * @param id
	 * @return
	 */
//	@AutoLog(value = "项目费用表-通过id删除")
//	@ApiOperation(value = "项目费用表-通过id删除", notes = "项目费用表-通过id删除")
//	@DeleteMapping(value = "/deleteProjectCost")
//	public Result<?> deleteProjectCost(@RequestParam(name = "id", required = true) String id) {
//		projectCostService.removeById(id);
//		return Result.OK("删除成功!");
//	}

	/**
	 * 批量删除
	 * 
	 * @param ids
	 * @return
	 */
//	@AutoLog(value = "项目费用表-批量删除")
//	@ApiOperation(value = "项目费用表-批量删除", notes = "项目费用表-批量删除")
//	@DeleteMapping(value = "/deleteBatchProjectCost")
//	public Result<?> deleteBatchProjectCost(@RequestParam(name = "ids", required = true) String ids) {
//		this.projectCostService.removeByIds(Arrays.asList(ids.split(",")));
//		return Result.OK("批量删除成功!");
//	}

	/**
	 * 通过项目ID查询费用明细
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "项目费用表-通过主表ID查询")
	@ApiOperation(value = "项目费用表-通过主表ID查询", notes = "项目费用表-通过主表ID查询")
	@GetMapping(value = "/queryProjectCostByMainId")
	public Result<?> queryProjectCostListByMainId(String wbsId, String projectId) {

		List<ProjectCostInfo> projectCostInfos = projectService.queryProjectCostList(wbsId, projectId);
		IPage<ProjectCostInfo> page = new Page<>();
		page.setRecords(projectCostInfos);
		page.setTotal(projectCostInfos.size());
		return Result.OK(page);
	}

	/**
	 * 导出
	 * 
	 * @return
	 */
	@RequestMapping(value = "/exportProjectCost")
	public ModelAndView exportProjectCost(HttpServletRequest request, ProjectCost projectCost) {
		// Step.1 组装查询条件
		QueryWrapper<ProjectCost> queryWrapper = QueryGenerator.initQueryWrapper(projectCost,
				request.getParameterMap());
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

		// Step.2 获取导出数据
		List<ProjectCost> pageList = projectCostService.list(queryWrapper);
		List<ProjectCost> exportList = null;

		// 过滤选中数据
		String selections = request.getParameter("selections");
		if (oConvertUtils.isNotEmpty(selections)) {
			List<String> selectionList = Arrays.asList(selections.split(","));
			exportList = pageList.stream().filter(item -> selectionList.contains(item.getId()))
					.collect(Collectors.toList());
		} else {
			exportList = pageList;
		}

		// Step.3 AutoPoi 导出Excel
		ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
		mv.addObject(NormalExcelConstants.FILE_NAME, "项目费用表"); // 此处设置的filename无效 ,前端会重更新设置一下
		mv.addObject(NormalExcelConstants.CLASS, ProjectCost.class);
		mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("项目费用表报表", "导出人:" + sysUser.getRealname(), "项目费用表"));
		mv.addObject(NormalExcelConstants.DATA_LIST, exportList);
		return mv;
	}

	

	

}
