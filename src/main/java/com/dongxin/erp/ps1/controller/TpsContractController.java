package com.dongxin.erp.ps1.controller;

import com.dongxin.erp.exception.ErpException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
import com.dongxin.erp.ps1.entity.TpsContractFund;
import com.dongxin.erp.ps1.entity.TpsContract;
import com.dongxin.erp.ps1.entity.TpsContractSave;
import com.dongxin.erp.ps1.service.TpsContractService;
import com.dongxin.erp.ps1.service.TpsContractFundService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.apache.shiro.SecurityUtils;
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
 * @Description: 合同信息表
 * @Author: jeecg-boot
 * @Date:   2021-01-22
 * @Version: V1.0
 */
@Api(tags="合同信息表")
@RestController
@RequestMapping("/ps1/tpsContract")
@Slf4j
public class TpsContractController extends JeecgController<TpsContract, TpsContractService> {

	@Autowired
	private TpsContractService tpsContractService;

	@Autowired
	private TpsContractFundService tpsContractFundService;


	/*---------------------------------主表处理-begin-------------------------------------*/

	/**
	 * 分页列表查询
	 * @param tpsContract
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "合同信息表-分页列表查询")
	@ApiOperation(value="合同信息表-分页列表查询", notes="合同信息表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(TpsContract tpsContract,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<TpsContract> queryWrapper = QueryGenerator.initQueryWrapper(tpsContract, req.getParameterMap());
		Page<TpsContract> page = new Page<TpsContract>(pageNo, pageSize);
		IPage<TpsContract> pageList = tpsContractService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
     *   添加
     * @param tpsContract
     * @return
     */
    @AutoLog(value = "合同信息表-添加")
    @ApiOperation(value="合同信息表-添加", notes="合同信息表-添加")
    @PostMapping(value = "/add")
    @RequiresPermissions("contract:add")
    public Result<?> add(@RequestBody TpsContractSave tpsContract) {
//        tpsContractService.save(tpsContract);
        tpsContractService.add(tpsContract);
        return Result.OK("添加成功！");
    }

	 /**
	  * 审核功能
	  *
	  * @param tpsContractList
	  * @return
	  */
	 @AutoLog(value = "合同信息表-审核")
	 @ApiOperation(value = "合同信息表-审核", notes = "合同信息表-审核")
	 @PostMapping("/check")
	 @RequiresPermissions("contract:edit")
	 public Result<Object> check(@RequestBody List<TpsContract> tpsContractList) {
		 try {
			 tpsContractService.check(tpsContractList);
		 } catch (ErpException e) {
			 return Result.OK(e.getMessage());
		 }
		 return Result.OK("审核成功!");
	 }

    /**
     *  编辑
     * @param tpsContractSave
     * @return
     */
    @AutoLog(value = "合同信息表-编辑")
    @ApiOperation(value="合同信息表-编辑", notes="合同信息表-编辑")
    @PutMapping(value = "/edit")
    @RequiresPermissions("contract:edit")
    public Result<?> edit(@RequestBody TpsContractSave tpsContractSave) {
//        tpsContractService.updateById(tpsContract);
		tpsContractService.edit(tpsContractSave);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     * @param id
     * @return
     */
    @AutoLog(value = "合同信息表-通过id删除")
    @ApiOperation(value="合同信息表-通过id删除", notes="合同信息表-通过id删除")
    @DeleteMapping(value = "/delete")
    @RequiresPermissions("contract:delete")
    public Result<?> delete(@RequestParam(name="id",required=true) String id) {
        tpsContractService.delMain(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     * @param ids
     * @return
     */
    @AutoLog(value = "合同信息表-批量删除")
    @ApiOperation(value="合同信息表-批量删除", notes="合同信息表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    @RequiresPermissions("contract:deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
        this.tpsContractService.delBatchMain(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 导出
     * @return
     */
    @RequestMapping(value = "/exportXls")
    @RequiresPermissions("contract:download")
    public ModelAndView exportXls(HttpServletRequest request, TpsContract tpsContract) {
        return super.exportXls(request, tpsContract, TpsContract.class, "合同信息表");
    }

    /**
     * 导入
     * @return
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, TpsContract.class);
    }
	/*---------------------------------主表处理-end-------------------------------------*/


    /*--------------------------------子表处理-合同资金表-begin----------------------------------------------*/
	/**
	 * 通过主表ID查询
	 * @return
	 */
	@AutoLog(value = "合同资金表-通过主表ID查询")
	@ApiOperation(value="合同资金表-通过主表ID查询", notes="合同资金表-通过主表ID查询")
	@GetMapping(value = "/listTpsContractFundByMainId")
    public Result<?> listTpsContractFundByMainId(TpsContractFund tpsContractFund,
                                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                    HttpServletRequest req) {
        QueryWrapper<TpsContractFund> queryWrapper = QueryGenerator.initQueryWrapper(tpsContractFund, req.getParameterMap());
        Page<TpsContractFund> page = new Page<TpsContractFund>(pageNo, pageSize);
        IPage<TpsContractFund> pageList = tpsContractFundService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

	/**
	 * 添加
	 * @param tpsContractFund
	 * @return
	 */
	@AutoLog(value = "合同资金表-添加")
	@ApiOperation(value="合同资金表-添加", notes="合同资金表-添加")
	@PostMapping(value = "/addTpsContractFund")
    @RequiresPermissions("contractfund:add")
	public Result<?> addTpsContractFund(@RequestBody TpsContractFund tpsContractFund) {
		tpsContractFundService.add(tpsContractFund);
		return Result.OK("添加成功！");
	}

    /**
	 * 编辑
	 * @param tpsContractFund
	 * @return
	 */
	@AutoLog(value = "合同资金表-编辑")
	@ApiOperation(value="合同资金表-编辑", notes="合同资金表-编辑")
	@PutMapping(value = "/editTpsContractFund")
    @RequiresPermissions("contractfund:edit")
    public Result<?> editTpsContractFund(@RequestBody TpsContractFund tpsContractFund) {
		tpsContractFundService.updateById(tpsContractFund);
		return Result.OK("编辑成功!");
	}

	/**
	 * 通过id删除
	 * @param id
	 * @return
	 */
	@AutoLog(value = "合同资金表-通过id删除")
	@ApiOperation(value="合同资金表-通过id删除", notes="合同资金表-通过id删除")
	@DeleteMapping(value = "/deleteTpsContractFund")
    @RequiresPermissions("contractfund:delete")
    public Result<?> deleteTpsContractFund(@RequestParam(name="id",required=true) String id) {
		tpsContractFundService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "合同资金表-批量删除")
	@ApiOperation(value="合同资金表-批量删除", notes="合同资金表-批量删除")
	@DeleteMapping(value = "/deleteBatchTpsContractFund")
    @RequiresPermissions("contractfund:delete_all")
    public Result<?> deleteBatchTpsContractFund(@RequestParam(name="ids",required=true) String ids) {
	    this.tpsContractFundService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

    /**
     * 导出
     * @return
     */
    @RequestMapping(value = "/exportTpsContractFund")
    @RequiresPermissions("contractfund:download")
    public ModelAndView exportTpsContractFund(HttpServletRequest request, TpsContractFund tpsContractFund) {
		 // Step.1 组装查询条件
		 QueryWrapper<TpsContractFund> queryWrapper = QueryGenerator.initQueryWrapper(tpsContractFund, request.getParameterMap());
		 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

		 // Step.2 获取导出数据
		 List<TpsContractFund> pageList = tpsContractFundService.list(queryWrapper);
		 List<TpsContractFund> exportList = null;

		 // 过滤选中数据
		 String selections = request.getParameter("selections");
		 if (oConvertUtils.isNotEmpty(selections)) {
			 List<String> selectionList = Arrays.asList(selections.split(","));
			 exportList = pageList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
		 } else {
			 exportList = pageList;
		 }

		 // Step.3 AutoPoi 导出Excel
		 ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
		 mv.addObject(NormalExcelConstants.FILE_NAME, "合同资金表"); //此处设置的filename无效 ,前端会重更新设置一下
		 mv.addObject(NormalExcelConstants.CLASS, TpsContractFund.class);
		 mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("合同资金表报表", "导出人:" + sysUser.getRealname(), "合同资金表"));
		 mv.addObject(NormalExcelConstants.DATA_LIST, exportList);
		 return mv;
    }

    /**
     * 导入
     * @return
     */
    @RequestMapping(value = "/importTpsContractFund/{mainId}")
    public Result<?> importTpsContractFund(HttpServletRequest request, HttpServletResponse response, @PathVariable("mainId") String mainId) {
		 MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		 Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		 for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
			 MultipartFile file = entity.getValue();// 获取上传文件对象
			 ImportParams params = new ImportParams();
			 params.setTitleRows(2);
			 params.setHeadRows(1);
			 params.setNeedSave(true);
			 try {
				 List<TpsContractFund> list = ExcelImportUtil.importExcel(file.getInputStream(), TpsContractFund.class, params);
				 for (TpsContractFund temp : list) {
                    temp.setContractId(mainId);
				 }
				 long start = System.currentTimeMillis();
				 tpsContractFundService.saveBatch(list);
				 log.info("消耗时间" + (System.currentTimeMillis() - start) + "毫秒");
				 return Result.OK("文件导入成功！数据行数：" + list.size());
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
		 return Result.error("文件导入失败！");
    }

	 /**
	  * 审核功能
	  *
	  * @param tpsContractFundList
	  * @return
	  */
	 @AutoLog(value = "合同资金信息表-审核")
	 @ApiOperation(value = "合同资金信息表-审核", notes = "合同资金信息表-审核")
	 @PostMapping("/checkTpsContractFund")
	 @RequiresPermissions("contractfund:edit")
	 public Result<Object> checkTpsContractFund(@RequestBody List<TpsContractFund> tpsContractFundList) {
		 try {
			 tpsContractFundService.check(tpsContractFundList);
		 } catch (ErpException e) {
			 return Result.error(e.getMessage());
		 }
		 return Result.OK("审核成功!");
	 }

    /*--------------------------------子表处理-合同资金表-end----------------------------------------------*/




}
