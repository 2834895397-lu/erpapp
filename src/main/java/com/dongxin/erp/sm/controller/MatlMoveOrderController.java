package com.dongxin.erp.sm.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dongxin.erp.sm.vo.MatlOutOrderPage;
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
import com.dongxin.erp.sm.entity.MatlMoveOrderDtl;
import com.dongxin.erp.sm.entity.MatlMoveOrder;
import com.dongxin.erp.sm.vo.MatlMoveOrderPage;
import com.dongxin.erp.sm.service.IMatlMoveOrderService;
import com.dongxin.erp.sm.service.IMatlMoveOrderDtlService;
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
 * @Description: 移库单主表
 * @Author: jeecg-boot
 * @Date:   2020-11-10
 * @Version: V1.0
 */
@Api(tags="移库单主表")
@RestController
@RequestMapping("/sm/matlMoveOrder")
@Slf4j
public class MatlMoveOrderController {
	@Autowired
	private IMatlMoveOrderService matlMoveOrderService;
	@Autowired
	private IMatlMoveOrderDtlService matlMoveOrderDtlService;
	
	/**
	 * 分页列表查询
	 *
	 * @param matlMoveOrder
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "移库单主表-分页列表查询")
	@ApiOperation(value="移库单主表-分页列表查询", notes="移库单主表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(MatlMoveOrder matlMoveOrder,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<MatlMoveOrder> queryWrapper = QueryGenerator.initQueryWrapper(matlMoveOrder, req.getParameterMap());
		Page<MatlMoveOrder> page = new Page<MatlMoveOrder>(pageNo, pageSize);
		IPage<MatlMoveOrder> pageList = matlMoveOrderService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param matlMoveOrderPage
	 * @return
	 */
	@AutoLog(value = "移库单主表-添加")
	@ApiOperation(value="移库单主表-添加", notes="移库单主表-添加")
	@PostMapping(value = "/add")
	@RequiresPermissions("moveorder.add")
	public Result<?> add(@RequestBody MatlMoveOrderPage matlMoveOrderPage) {
		MatlMoveOrder matlMoveOrder = new MatlMoveOrder();
		BeanUtils.copyProperties(matlMoveOrderPage, matlMoveOrder);
		matlMoveOrderService.saveMain(matlMoveOrder, matlMoveOrderPage.getMatlMoveOrderDtlList());
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param matlMoveOrderPage
	 * @return
	 */
	@AutoLog(value = "移库单主表-编辑")
	@ApiOperation(value="移库单主表-编辑", notes="移库单主表-编辑")
	@PutMapping(value = "/edit")
	@RequiresPermissions("moveorder.edit")
	public Result<?> edit(@RequestBody MatlMoveOrderPage matlMoveOrderPage) {
		MatlMoveOrder matlMoveOrder = new MatlMoveOrder();
		BeanUtils.copyProperties(matlMoveOrderPage, matlMoveOrder);
		MatlMoveOrder matlMoveOrderEntity = matlMoveOrderService.getById(matlMoveOrder.getId());
		if(matlMoveOrderEntity==null) {
			return Result.error("未找到对应数据");
		}
		matlMoveOrderService.updateMain(matlMoveOrder, matlMoveOrderPage.getMatlMoveOrderDtlList());
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	/*@AutoLog(value = "移库单主表-通过id删除")
	@ApiOperation(value="移库单主表-通过id删除", notes="移库单主表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		matlMoveOrderService.delMain(id);
		return Result.ok("删除成功!");
	}*/
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "移库单主表-批量删除")
	@ApiOperation(value="移库单主表-批量删除", notes="移库单主表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	@RequiresPermissions("moveorder.delete")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		int i = matlMoveOrderService.myDelBatchMain(Arrays.asList(ids.split(",")));
		if(i == 0){
			return Result.error("请选择未审核的记录删除");
		}

		return Result.ok("成功删除" + i + "条记录");
	}
	
	/**
	 * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "移库单主表-通过id查询")
	@ApiOperation(value="移库单主表-通过id查询", notes="移库单主表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		MatlMoveOrder matlMoveOrder = matlMoveOrderService.getById(id);
		if(matlMoveOrder==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(matlMoveOrder);

	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "移库单明细表-通过主表ID查询")
	@ApiOperation(value="移库单明细表-通过主表ID查询", notes="移库单明细表-通过主表ID查询")
	@GetMapping(value = "/queryMatlMoveOrderDtlByMainId")
	public Result<?> queryMatlMoveOrderDtlListByMainId(@RequestParam(name="id",required=true) String id) {
		List<MatlMoveOrderDtl> matlMoveOrderDtlList = matlMoveOrderDtlService.selectByMainId(id);
		IPage <MatlMoveOrderDtl> page = new Page<>();
		page.setRecords(matlMoveOrderDtlService.addTempField(matlMoveOrderDtlList));
		page.setTotal(matlMoveOrderDtlList.size());
		return Result.ok(page);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param matlMoveOrder
    */
    @RequestMapping(value = "/exportXls")
	@RequiresPermissions("moveorder.out")
    public ModelAndView exportXls(HttpServletRequest request, MatlMoveOrder matlMoveOrder) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<MatlMoveOrder> queryWrapper = QueryGenerator.initQueryWrapper(matlMoveOrder, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //Step.2 获取导出数据
      List<MatlMoveOrder> queryList = matlMoveOrderService.list(queryWrapper);
      // 过滤选中数据
      String selections = request.getParameter("selections");
      List<MatlMoveOrder> matlMoveOrderList = new ArrayList<MatlMoveOrder>();
      if(oConvertUtils.isEmpty(selections)) {
          matlMoveOrderList = queryList;
      }else {
          List<String> selectionList = Arrays.asList(selections.split(","));
          matlMoveOrderList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
      }

      // Step.3 组装pageList
      List<MatlMoveOrderPage> pageList = new ArrayList<MatlMoveOrderPage>();
      for (MatlMoveOrder main : matlMoveOrderList) {
          MatlMoveOrderPage vo = new MatlMoveOrderPage();
          BeanUtils.copyProperties(main, vo);
          List<MatlMoveOrderDtl> matlMoveOrderDtlList = matlMoveOrderDtlService.selectByMainId(main.getId());
          vo.setMatlMoveOrderDtlList(matlMoveOrderDtlList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "移库单主表列表");
      mv.addObject(NormalExcelConstants.CLASS, MatlMoveOrderPage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("移库单主表数据", "导出人:"+sysUser.getRealname(), "移库单主表"));
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
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
	@RequiresPermissions("moveorder.in")
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
              List<MatlMoveOrderPage> list = ExcelImportUtil.importExcel(file.getInputStream(), MatlMoveOrderPage.class, params);
              for (MatlMoveOrderPage page : list) {
                  MatlMoveOrder po = new MatlMoveOrder();
                  BeanUtils.copyProperties(page, po);
                  matlMoveOrderService.saveMain(po, page.getMatlMoveOrderDtlList());
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

	 /**
	  * 多条或单条删除
	  *
	  * @param id
	  * @return
	  */
	 @AutoLog(value = "移库单明细表-通过id删除")
	 @ApiOperation(value = "移库单明细表-通过id删除", notes = "移库单明细表-通过id删除")
	 @DeleteMapping(value = "/deleteMatlMoveOrderDtl")
	 @RequiresPermissions("moveorder.delete")
	 public Result<?> deleteMatlMoveOrderDtl(@RequestParam(name = "id", required = true) List<String> id) {
		 matlMoveOrderDtlService.removeByIds(id);
		 return Result.OK("删除成功!");
	 }

	 /**
	  * @param matlMoveOrders: 前端传过来的记录数组
	  * @return
	  */
	 @PostMapping("/check")
	 @RequiresPermissions("moveorder.check")
	 public synchronized Result<Object> check(@RequestBody ArrayList<MatlMoveOrder> matlMoveOrders) {
		 String ok = matlMoveOrderService.checks(matlMoveOrders);
		 return Result.OK(ok);
	 }



	 /**
	  * 领料单明细冲红
	  *
	  * @param matlMoveOrderPage: 领料单明细表单
	  * @return
	  */
	 @AutoLog(value = "领货明细冲红")
	 @ApiOperation(value = "领货明细冲红", notes = "领货明细冲红")
	 @PutMapping(value = "/redFlushDtl")
	 @RequiresPermissions("moveorder.redflushdtl")
	 public Result<?> redFlushByDtl(@RequestBody MatlMoveOrderPage matlMoveOrderPage) {
		 try {
			 matlMoveOrderService.redFlushByIds(matlMoveOrderPage.getMatlMoveOrderDtlList());
			 return Result.OK("成功冲红记录");
		 }catch (Exception e){
			 return Result.error(e.getMessage());
		 }
	 }

	 /**
	  * 领料单整单冲红
	  *
	  * @param ids: 领料单表单 ID
	  * @return
	  */
	 @AutoLog(value = "领料单整单冲红")
	 @ApiOperation(value = "领料单整单冲红", notes = "领料单整单冲红")
	 @DeleteMapping(value = "/redFlush")
	 @RequiresPermissions("moveorder.redflush")
	 public Result<?> redFlush(@RequestParam(name = "ids", required = false) String ids) {
		 try {
			 matlMoveOrderService.redFlushById(ids);
			 return Result.OK("成功冲红记录");
		 }catch (Exception e){
			 return Result.error(e.getMessage());
		 }
	 }


	 /**
	  * 通过id查询未冲红数据
	  *
	  * @param id
	  * @return
	  */
	 @AutoLog(value = "移库单明细表-通过主表ID查询")
	 @ApiOperation(value="移库单明细表-通过主表ID查询", notes="移库单明细表-通过主表ID查询")
	 @GetMapping(value = "/queryNoRedDtlListByMainId")
	 public Result<?> queryNoRedDtlListByMainId(@RequestParam(name="id",required=true) String id) {
		 List<MatlMoveOrderDtl> matlMoveOrderDtlList = matlMoveOrderDtlService.selectNoRedFlushDtlByMainId(id);
		 IPage <MatlMoveOrderDtl> page = new Page<>();
		 page.setRecords(matlMoveOrderDtlService.addTempField(matlMoveOrderDtlList));
		 page.setTotal(matlMoveOrderDtlList.size());
		 return Result.ok(page);
	 }

 }
