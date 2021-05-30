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
import com.dongxin.erp.mm.entity.OrderDtl;
import com.dongxin.erp.mm.entity.Order;
import com.dongxin.erp.mm.vo.OrderPage;
import com.dongxin.erp.mm.service.OrderService;
import com.dongxin.erp.mm.service.OrderDtlService;
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
 * @Description: 采购订单主表
 * @Author: jeecg-boot
 * @Date:   2020-11-18
 * @Version: V1.0
 */
@Api(tags="采购订单主表")
@RestController
@RequestMapping("/mm/order")
@Slf4j
public class OrderController {

	@Autowired
    private OrderService orderService;

    @Autowired
    private OrderDtlService orderDtlService;
	
	/**
	 * 分页列表查询
	 *
	 * @param order
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "采购订单主表-分页列表查询")
	@ApiOperation(value="采购订单主表-分页列表查询", notes="采购订单主表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(Order order,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<Order> queryWrapper = QueryGenerator.initQueryWrapper(order, req.getParameterMap());
		Page<Order> page = new Page<Order>(pageNo, pageSize);
		IPage<Order> pageList = orderService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param orderPage
	 * @return
	 */
	@RequiresPermissions("order.add")
	@AutoLog(value = "采购订单主表-添加")
	@ApiOperation(value="采购订单主表-添加", notes="采购订单主表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody OrderPage orderPage) {
		Order order = new Order();
		BeanUtils.copyProperties(orderPage, order);
		orderService.saveMain(order, orderPage.getOrderDtlList());
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param orderPage
	 * @return
	 */
	@RequiresPermissions("order.edit")
	@AutoLog(value = "采购订单主表-编辑")
	@ApiOperation(value="采购订单主表-编辑", notes="采购订单主表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody OrderPage orderPage) {
		Order order = new Order();
		BeanUtils.copyProperties(orderPage, order);
		Order orderEntity = orderService.getById(order.getId());
		if(orderEntity==null) {
			return Result.error("未找到对应数据");
		}
		orderService.updateMain(order, orderPage.getOrderDtlList());
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@RequiresPermissions("order.delete")
	@AutoLog(value = "采购订单主表-通过id删除")
	@ApiOperation(value="采购订单主表-通过id删除", notes="采购订单主表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		String s = orderService.delMain(id);
		return Result.ok(s);
	}

	 /**
	  * 通过主表ids批量逻辑删除主表和附表
	  *
	  * @param ids: 主表的ids, 多个id用,隔开
	  * @return
	  */
	@AutoLog(value = "采购订单主表-批量删除")
	@ApiOperation(value="采购订单主表-批量删除", notes="采购订单主表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	@RequiresPermissions("order.batchDelete")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		int i = orderService.delBatchMain(Arrays.asList(ids.split(",")));
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
	@AutoLog(value = "采购订单主表-通过id查询")
	@ApiOperation(value="采购订单主表-通过id查询", notes="采购订单主表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		Order order = orderService.getById(id);
		if(order==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(order);

	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "采购订单明细表-通过主表ID查询")
	@ApiOperation(value="采购订单明细表-通过主表ID查询", notes="采购订单明细表-通过主表ID查询")
	@GetMapping(value = "/queryOrderDtlByMainId")
	public Result<?> queryOrderDtlListByMainId(@RequestParam(name="id",required=true) String id) {
		List<OrderDtl> orderDtlList = orderDtlService.selectByMainId(id);

		//设置采购单据
		orderDtlService.setContractNumber(orderDtlList);

		IPage <OrderDtl> page = new Page<>();
		page.setRecords(orderDtlList);
		page.setTotal(orderDtlList.size());
		return Result.ok(page);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param order
    */
	@RequiresPermissions("order.download")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Order order) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<Order> queryWrapper = QueryGenerator.initQueryWrapper(order, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //Step.2 获取导出数据
      List<Order> queryList = orderService.list(queryWrapper);
      // 过滤选中数据
      String selections = request.getParameter("selections");
      List<Order> orderList = new ArrayList<Order>();
      if(oConvertUtils.isEmpty(selections)) {
          orderList = queryList;
      }else {
          List<String> selectionList = Arrays.asList(selections.split(","));
          orderList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
      }

      // Step.3 组装pageList
      List<OrderPage> pageList = new ArrayList<OrderPage>();
      for (Order main : orderList) {
          OrderPage vo = new OrderPage();
          BeanUtils.copyProperties(main, vo);
          List<OrderDtl> orderDtlList = orderDtlService.selectByMainId(main.getId());
          vo.setOrderDtlList(orderDtlList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "采购订单主表列表");
      mv.addObject(NormalExcelConstants.CLASS, OrderPage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("采购订单主表数据", "导出人:"+sysUser.getRealname(), "采购订单主表"));
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
	@RequiresPermissions("order.import")
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
              List<OrderPage> list = ExcelImportUtil.importExcel(file.getInputStream(), OrderPage.class, params);
              for (OrderPage page : list) {
                  Order po = new Order();
                  BeanUtils.copyProperties(page, po);
                  orderService.saveMain(po, page.getOrderDtlList());
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
	 @PostMapping("/check")
	 @RequiresPermissions("order.check")
	 public Result check(@RequestBody ArrayList<Order> orders) {

		 orderService.check(orders);
		 return Result.OK("审核成功!");

	 }

	 /**
	  * 通过id删除
	  *
	  * @param id
	  * @return
	  */
	 @AutoLog(value = "采购订单明细表-通过id删除")
	 @ApiOperation(value = "采购订单明细表-通过id删除", notes = "采购订单明细表-通过id删除")
	 @DeleteMapping(value = "/deleteOrderDtl")
	 public Result<?> deleteOrderDtl(@RequestParam(name = "id", required = true) List<String> id) {
		 orderDtlService.removeByIds(id);
		 return Result.OK("删除成功!");
	 }

 }
