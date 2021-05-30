package com.dongxin.erp.sm.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dongxin.erp.exception.ErpException;
import com.dongxin.erp.mm.service.OrderDtlService;
import com.dongxin.erp.sm.entity.MatlInOrder;
import com.dongxin.erp.sm.entity.MatlInOrderDtl;
import com.dongxin.erp.sm.service.impl.MatlInOrderDtlServiceImpl;
import com.dongxin.erp.sm.service.impl.MatlInOrderServiceImpl;
import com.dongxin.erp.sm.vo.MatlInOrderPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description: 收货单主表
 * @Author: jeecg-boot
 * @Date: 2020-11-11
 * @Version: V1.0
 */
@Api(tags = "收货单主表")
@RestController
@RequestMapping("/sm/matlInOrder")
@Slf4j
public class MatlInOrderController {
    @Autowired
    private MatlInOrderDtlServiceImpl matlInOrderDtlService;
    @Autowired
    private MatlInOrderServiceImpl matlInOrderService;

    @Autowired
    private OrderDtlService orderDtlService;

    /**
     * 分页列表查询
     *
     * @param matlInOrder
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "收货单主表-分页列表查询")
    @ApiOperation(value = "收货单主表-分页列表查询", notes = "收货单主表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(MatlInOrder matlInOrder,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<MatlInOrder> queryWrapper = QueryGenerator.initQueryWrapper(matlInOrder, req.getParameterMap());
        Page<MatlInOrder> page = new Page<MatlInOrder>(pageNo, pageSize);
        IPage<MatlInOrder> pageList = matlInOrderService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param matlInOrderPage
     * @return
     */
    @AutoLog(value = "收货单主表-添加")
    @ApiOperation(value = "收货单主表-添加", notes = "收货单主表-添加")
    @PostMapping(value = "/add")
    @RequiresPermissions("inorder.add")
    public Result<?> add(@RequestBody MatlInOrderPage matlInOrderPage) {
        MatlInOrder matlInOrder = new MatlInOrder();
        BeanUtils.copyProperties(matlInOrderPage, matlInOrder);
        try{
            matlInOrderService.saveAndUpdateOrderDet(matlInOrder, matlInOrderPage.getMatlInOrderDtlList());
        }catch (ErpException e){
            return Result.error(e.getMessage());
        }
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param matlInOrderPage
     * @return
     */
    @AutoLog(value = "收货单主表-编辑")
    @ApiOperation(value = "收货单主表-编辑", notes = "收货单主表-编辑")
    @PutMapping(value = "/edit")
    @RequiresPermissions("inorder.edit")
    public Result<?> edit(@RequestBody MatlInOrderPage matlInOrderPage) {
        MatlInOrder matlInOrder = new MatlInOrder();
        BeanUtils.copyProperties(matlInOrderPage, matlInOrder);
        MatlInOrder matlInOrderEntity = matlInOrderService.getById(matlInOrder.getId());
        if (matlInOrderEntity == null) {
            return Result.error("未找到对应数据");
        }
        matlInOrderService.updateMain(matlInOrder, matlInOrderPage.getMatlInOrderDtlList());
        return Result.ok("编辑成功!");
    }

    /**
     * 通过主表ids批量逻辑删除主表和附表
     *
     * @param ids: 主表的ids, 多个id用,隔开
     * @return
     */
    @AutoLog(value = "收货单主表-批量删除")
    @ApiOperation(value = "收货单主表-批量删除", notes = "收货单主表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    @RequiresPermissions("inorder.delete")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        int i = matlInOrderService.delBatchMain(Arrays.asList(ids.split(",")));
        if (i == 0) {
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
    @AutoLog(value = "收货单主表-通过id查询")
    @ApiOperation(value = "收货单主表-通过id查询", notes = "收货单主表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        MatlInOrder matlInOrder = matlInOrderService.getById(id);
        if (matlInOrder == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(matlInOrder);

    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "收货单明细表-通过主表ID查询")
    @ApiOperation(value = "收货单明细表-通过主表ID查询", notes = "收货单明细表-通过主表ID查询")
    @GetMapping(value = "/queryMatlInOrderDtlByMainId")
    public Result<?> queryMatlInOrderDtlListByMainId(@RequestParam(name = "id", required = true) String id) {

        List<MatlInOrderDtl> matlInOrderDtlList = matlInOrderDtlService.selectByMainId(id);
        IPage<MatlInOrderDtl> page = new Page<>();
        page.setRecords(matlInOrderDtlService.addTempField(matlInOrderDtlList));
        page.setTotal(matlInOrderDtlList.size());
        return Result.ok(page);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param matlInOrder
     */
    @RequestMapping(value = "/exportXls")
    @RequiresPermissions("inorder.out")
    public ModelAndView exportXls(HttpServletRequest request, MatlInOrder matlInOrder) {
        // Step.1 组装查询条件查询数据
        QueryWrapper<MatlInOrder> queryWrapper = QueryGenerator.initQueryWrapper(matlInOrder, request.getParameterMap());
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        //Step.2 获取导出数据
        List<MatlInOrder> queryList = matlInOrderService.list(queryWrapper);
        // 过滤选中数据
        String selections = request.getParameter("selections");
        List<MatlInOrder> matlInOrderList = new ArrayList<MatlInOrder>();
        if (oConvertUtils.isEmpty(selections)) {
            matlInOrderList = queryList;
        } else {
            List<String> selectionList = Arrays.asList(selections.split(","));
            matlInOrderList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
        }

        // Step.3 组装pageList
        List<MatlInOrderPage> pageList = new ArrayList<MatlInOrderPage>();
        for (MatlInOrder main : matlInOrderList) {
            MatlInOrderPage vo = new MatlInOrderPage();
            BeanUtils.copyProperties(main, vo);
            List<MatlInOrderDtl> matlInOrderDtlList = matlInOrderDtlService.selectByMainId(main.getId());
            vo.setMatlInOrderDtlList(matlInOrderDtlList);
            pageList.add(vo);
        }

        // Step.4 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "收货单主表列表");
        mv.addObject(NormalExcelConstants.CLASS, MatlInOrderPage.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("收货单主表数据", "导出人:" + sysUser.getRealname(), "收货单主表"));
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
    @RequiresPermissions("inorder.in")
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
                List<MatlInOrderPage> list = ExcelImportUtil.importExcel(file.getInputStream(), MatlInOrderPage.class, params);
                for (MatlInOrderPage page : list) {
                    MatlInOrder po = new MatlInOrder();
                    BeanUtils.copyProperties(page, po);
                    matlInOrderService.saveMain(po, page.getMatlInOrderDtlList());
                }
                return Result.ok("文件导入成功！数据行数:" + list.size());
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
        return Result.ok("文件导入失败！");
    }

    /**
     * 收货单审核操作
     *
     * @param matlInOrders: 前端传过来的记录数组
     * @return
     */
    @PostMapping("/check")
    @RequiresPermissions("inorder.check")
    public synchronized Result<Object> check(@RequestBody ArrayList<MatlInOrder> matlInOrders) {
        String result = matlInOrderService.checks(matlInOrders);
        return Result.OK(result);
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "收货单明细表-通过id删除")
    @ApiOperation(value = "收货单明细表-通过id删除", notes = "收货单明细表-通过id删除")
    @DeleteMapping(value = "/deleteMatlInOrderDtl")
    @RequiresPermissions("inorder.delete")
    public Result<?> deleteMatlInOrderDtl(@RequestParam(name = "id", required = true) List<String> id) {
        matlInOrderDtlService.removeByIds(id);
        return Result.OK("删除成功!");
    }


    /******************冲红*******************/

    /**
     * 入库单明细冲红
     *
     * @param matlInOrderPage: 入库单明细表单
     * @return
     */
    @AutoLog(value = "入库明细冲红")
    @ApiOperation(value = "入库明细冲红", notes = "入库明细冲红")
    @PutMapping(value = "/redFlushDtl")
    @RequiresPermissions("inorder.redflushdtl")
    public Result<?> redFlushByDtl(@RequestBody MatlInOrderPage matlInOrderPage) {
        try {
            matlInOrderService.redFlushByIds(matlInOrderPage.getMatlInOrderDtlList());
            return Result.OK("成功冲红记录");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 入库单整单冲红
     *
     * @param id: 入库单表单 ID
     * @return
     */
    @AutoLog(value = "入库单整单冲红")
    @ApiOperation(value = "入库单整单冲红", notes = "入库单整单冲红")
    @DeleteMapping(value = "/redFlush")
    @RequiresPermissions("inorder.redflush")
    public Result<?> redFlush(@RequestParam(name = "ids", required = false) String id) {
        try {
            matlInOrderService.redFlushById(id);
            return Result.OK("成功冲红记录");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }


    /**
     * 通过id查询未冲红数据
     *
     * @param id
     * @return
     */
    @AutoLog(value = "领用单明细表-通过主表ID查询")
    @ApiOperation(value = "领用单明细表-通过主表ID查询", notes = "领用单明细表-通过主表ID查询")
    @GetMapping(value = "/queryNoRedDtlListByMainId")
    public Result<?> queryNoRedDtlListByMainId(@RequestParam(name = "id", required = true) String id) {
        List<MatlInOrderDtl> matlInOrderDtlList = matlInOrderDtlService.selectNoRedFlushDtlByMainId(id);
        IPage<MatlInOrderDtl> page = new Page<>();
        page.setRecords(matlInOrderDtlService.addTempField(matlInOrderDtlList));
        page.setTotal(matlInOrderDtlList.size());
        return Result.ok(page);
    }

}
