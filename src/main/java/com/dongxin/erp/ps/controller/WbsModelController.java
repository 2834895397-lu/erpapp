package com.dongxin.erp.ps.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.BaseController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dongxin.erp.ps.entity.ProjectCostInfo;
import com.dongxin.erp.ps.entity.WbsModel;
import com.dongxin.erp.ps.service.WbsModelService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

 /**
 * @Description: 工作分解模板
 * @Author: jeecg-boot
 * @Date:   2021-01-20
 * @Version: V1.0
 */
@Api(tags="工作分解模板")
@RestController
@RequestMapping("/ps/wbsModel")
@Slf4j
public class WbsModelController extends BaseController<WbsModel, WbsModelService> {
	
	@Autowired
	private WbsModelService wbsModelService;

	/**
	 * 获取模板列表
	 *
	 * @return
	 */
	@AutoLog(value = "工作分解模板-获取模板列表")
	@ApiOperation(value="工作分解模板-获取模板列表", notes="工作分解模板-获取模板列表")
	@GetMapping(value = "/queryWbsModelList")
	public Result<?> queryWbsModelList() {
		return Result.OK(wbsModelService.queryWbsModelList());
	}
}
