package com.dongxin.erp.mm.controller;

import java.io.IOException;
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
import com.dongxin.erp.mm.entity.SettleDtl;
import com.dongxin.erp.exception.ErpException;
import com.dongxin.erp.mm.entity.Settle;
import com.dongxin.erp.mm.vo.SettlePage;
import com.dongxin.erp.mm.service.SettleService;
import com.dongxin.erp.mm.service.SettleDtlService;
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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

/**
 * @Description: 采购结算主表
 * @Author: jeecg-boot/huangheng
 * @Date: 2020-11-23
 * @Version: V1.0
 */
@Api(tags = "采购结算主表")
@RestController
@RequestMapping("/mm/settle")
@Slf4j
public class SettleController {

	@Autowired
	private SettleService settleService;

	@Autowired
	private SettleDtlService settleDtlService;

	/**
	 * 分页列表查询
	 * 
	 * @param settle
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "采购结算主表-分页列表查询")
	@ApiOperation(value = "采购结算主表-分页列表查询", notes = "采购结算主表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(Settle settle, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
		QueryWrapper<Settle> queryWrapper = QueryGenerator.initQueryWrapper(settle, req.getParameterMap());
		Page<Settle> page = new Page<Settle>(pageNo, pageSize);
		IPage<Settle> pageList = settleService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 * 添加
	 *
	 * @param settlePage
	 * @return
	 */
	@AutoLog(value = "采购结算主表-添加")
	@ApiOperation(value = "采购结算主表-添加", notes = "采购结算主表-添加")
	@PostMapping(value = "/add")
	@RequiresPermissions("mm:settle:handleAdd")
	public Result<?> add(@RequestBody SettlePage settlePage) {

		settleService.add(settlePage);

		return Result.OK("添加成功！");
	}

	/**
	 * 编辑
	 *
	 * @param settlePage
	 * @return
	 */
	@AutoLog(value = "采购结算主表-编辑")
	@ApiOperation(value = "采购结算主表-编辑", notes = "采购结算主表-编辑")
	@PutMapping(value = "/edit")
	@RequiresPermissions("mm:settle:handleEdit")
	public Result<?> edit(@RequestBody SettlePage settlePage) {

		settleService.edit(settlePage);

		return Result.OK("编辑成功!");
	}

	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "采购结算主表-通过id删除")
	@ApiOperation(value = "采购结算主表-通过id删除", notes = "采购结算主表-通过id删除")
	@DeleteMapping(value = "/delete")
	@RequiresPermissions("mm:settle:handleDelete")
	public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
		settleService.delMain(id);
		return Result.OK("删除成功!");
	}

	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "采购结算主表-批量删除")
	@ApiOperation(value = "采购结算主表-批量删除", notes = "采购结算主表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	@RequiresPermissions("mm:settle:batchDel")
	public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
		this.settleService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "采购结算主表-通过id查询")
	@ApiOperation(value = "采购结算主表-通过id查询", notes = "采购结算主表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
		Settle settle = settleService.getById(id);
		if (settle == null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(settle);

	}

	/**
	 * 通过采购结算id查询明细
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "采购结算明细表-通过主表ID查询")
	@ApiOperation(value = "采购结算明细表-通过主表ID查询", notes = "采购结算明细表-通过主表ID查询")
	@GetMapping(value = "/querySettleDtlByMainId")
	public Result<?> querySettleDtlListByMainId(@RequestParam(name = "id", required = true) String id) {

		List<SettleDtl> settleDtlList = settleDtlService.querySettleDtlListByMainId(id);
		IPage<SettleDtl> page = new Page<>();
		page.setRecords(settleDtlList);
		page.setTotal(settleDtlList.size());
		return Result.OK(page);
	}

	/**
	 * 导出excel
	 *
	 * @param request
	 * @param settle
	 */
	@AutoLog(value = "采购结算记录-导出")
	@ApiOperation(value = "采购结算记录-导出", notes = "采购结算记录-导出")
	@RequestMapping(value = "/exportXls")
	@RequiresPermissions("mm:settle:handleExportXls")
	public ModelAndView exportXls(HttpServletRequest request, Settle settle) {
		// Step1 组装查询条件查询数据
		QueryWrapper<Settle> queryWrapper = QueryGenerator.initQueryWrapper(settle, request.getParameterMap());
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

		// Step2 获取导出数据
		List<Settle> queryList = settleService.list(queryWrapper);
		// 过滤选中数据
		String selections = request.getParameter("selections");
		List<Settle> settleList = new ArrayList<Settle>();
		if (oConvertUtils.isEmpty(selections)) {
			settleList = queryList;
		} else {
			List<String> selectionList = Arrays.asList(selections.split(","));
			settleList = queryList.stream().filter(item -> selectionList.contains(item.getId()))
					.collect(Collectors.toList());
		}
		// Step3 组装pageList
		List<SettlePage> pageList = new ArrayList<SettlePage>();
		for (Settle main : settleList) {
			SettlePage vo = new SettlePage();
			BeanUtils.copyProperties(main, vo);
			List<SettleDtl> settleDtlList = settleDtlService.selectByMainId(main.getId());
			vo.setSettleDtlList(settleDtlList);
			pageList.add(vo);
		}
		// Step4 AutoPoi 导出Excel
		ModelAndView modelAndView = new ModelAndView(new JeecgEntityExcelView());
		modelAndView.addObject(NormalExcelConstants.FILE_NAME, "采购结算列表");
		modelAndView.addObject(NormalExcelConstants.CLASS, SettlePage.class);
		modelAndView.addObject(NormalExcelConstants.PARAMS,
				new ExportParams("采购结算数据", "导出人:" + sysUser.getRealname(), "采购结算列表"));
		modelAndView.addObject(NormalExcelConstants.DATA_LIST, pageList);
		return modelAndView;
	}

	/**
	 * 通过excel导入数据
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/importExcel", method = RequestMethod.POST)
	@RequiresPermissions("mm:settle:import")
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
				List<SettlePage> list = ExcelImportUtil.importExcel(file.getInputStream(), SettlePage.class, params);
				for (SettlePage page : list) {
					Settle po = new Settle();
					BeanUtils.copyProperties(page, po);
					settleService.saveMain(po, page.getSettleDtlList());
				}
				return Result.OK("文件导入成功！数据行数:" + list.size());
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				return Result.error("文件导入失败:" + e.getMessage());
			} finally {
				try {
					file.getInputStream().close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return Result.OK("文件导入失败！");
	}

	/**
	 * 审核功能
	 * 
	 * @param settleList
	 * @return
	 */
	@AutoLog(value = "采购结算明细表-审核")
	@ApiOperation(value = "采购结算明细表-审核", notes = "采购结算明细表-审核")
	@PostMapping("/check")
	@RequiresPermissions("mm:settle:check")
	public Result<Object> check(@RequestBody List<Settle> settleList) {
		try {
			settleService.check(settleList);
		} catch (ErpException e) {
			return Result.OK(e.getMessage());
		}
		return Result.OK("审核成功!");
	}
}
