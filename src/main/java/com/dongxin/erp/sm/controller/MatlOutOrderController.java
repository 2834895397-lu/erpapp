package com.dongxin.erp.sm.controller;

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
import com.dongxin.erp.sm.entity.MatlOutOrderDtl;
import com.dongxin.erp.sm.entity.MatlOutOrder;
import com.dongxin.erp.sm.vo.MatlOutOrderPage;
import com.dongxin.erp.sm.service.IMatlOutOrderService;
import com.dongxin.erp.sm.service.IMatlOutOrderDtlService;
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
 * @Description: 领用单主表
 * @Author: jeecg-boot
 * @Date: 2020-11-10
 * @Version: V1.0
 */
@Api(tags = "领用单主表")
@RestController
@RequestMapping("/sm/matlOutOrder")
@Slf4j
public class MatlOutOrderController {
    @Autowired
    private IMatlOutOrderService matlOutOrderService;
    @Autowired
    private IMatlOutOrderDtlService matlOutOrderDtlService;

    /**
     * 分页列表查询
     *
     * @param matlOutOrder
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "领用单主表-分页列表查询")
    @ApiOperation(value = "领用单主表-分页列表查询", notes = "领用单主表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(MatlOutOrder matlOutOrder,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<MatlOutOrder> queryWrapper = QueryGenerator.initQueryWrapper(matlOutOrder, req.getParameterMap());
        Page<MatlOutOrder> page = new Page<MatlOutOrder>(pageNo, pageSize);
        IPage<MatlOutOrder> pageList = matlOutOrderService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param matlOutOrderPage
     * @return
     */
    @AutoLog(value = "领用单主表-添加")
    @ApiOperation(value = "领用单主表-添加", notes = "领用单主表-添加")
    @PostMapping(value = "/add")
    @RequiresPermissions("outorder.add")
    public Result<?> add(@RequestBody MatlOutOrderPage matlOutOrderPage) {
        MatlOutOrder matlOutOrder = new MatlOutOrder();
        BeanUtils.copyProperties(matlOutOrderPage, matlOutOrder);
        matlOutOrderService.saveMain(matlOutOrder, matlOutOrderPage.getMatlOutOrderDtlList());
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param matlOutOrderPage
     * @return
     */
    @AutoLog(value = "领用单主表-编辑")
    @ApiOperation(value = "领用单主表-编辑", notes = "领用单主表-编辑")
    @PutMapping(value = "/edit")
    @RequiresPermissions("outorder.edit")
    public Result<?> edit(@RequestBody MatlOutOrderPage matlOutOrderPage) {
        MatlOutOrder matlOutOrder = new MatlOutOrder();
        BeanUtils.copyProperties(matlOutOrderPage, matlOutOrder);
        MatlOutOrder matlOutOrderEntity = matlOutOrderService.getById(matlOutOrder.getId());
        if (matlOutOrderEntity == null) {
            return Result.error("未找到对应数据");
        }
        matlOutOrderService.updateMain(matlOutOrder, matlOutOrderPage.getMatlOutOrderDtlList());
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
   /* @AutoLog(value = "领用单主表-通过id删除")
    @ApiOperation(value = "领用单主表-通过id删除", notes = "领用单主表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        matlOutOrderService.delMain(id);
        return Result.ok("删除成功!");
    }
*/
    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "领用单主表-批量删除")
    @ApiOperation(value = "领用单主表-批量删除", notes = "领用单主表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    @RequiresPermissions("outorder.delete")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        int i = matlOutOrderService.delBatchMain(Arrays.asList(ids.split(",")));
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
    @AutoLog(value = "领用单主表-通过id查询")
    @ApiOperation(value = "领用单主表-通过id查询", notes = "领用单主表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        MatlOutOrder matlOutOrder = matlOutOrderService.getById(id);
        if (matlOutOrder == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(matlOutOrder);

    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "领用单明细表-通过主表ID查询")
    @ApiOperation(value = "领用单明细表-通过主表ID查询", notes = "领用单明细表-通过主表ID查询")
    @GetMapping(value = "/queryMatlOutOrderDtlByMainId")
    public Result<?> queryMatlOutOrderDtlListByMainId(@RequestParam(name = "id", required = true) String id) {
        List<MatlOutOrderDtl> matlOutOrderDtlList = matlOutOrderDtlService.selectByMainId(id);
        IPage<MatlOutOrderDtl> page = new Page<>();
        page.setRecords(matlOutOrderDtlService.addTempField(matlOutOrderDtlList));
        page.setTotal(matlOutOrderDtlList.size());
        return Result.ok(page);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param matlOutOrder
     */
    @RequestMapping(value = "/exportXls")
    @RequiresPermissions("outorder.out")
    public ModelAndView exportXls(HttpServletRequest request, MatlOutOrder matlOutOrder) {
        // Step.1 组装查询条件查询数据
        QueryWrapper<MatlOutOrder> queryWrapper = QueryGenerator.initQueryWrapper(matlOutOrder, request.getParameterMap());
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        //Step.2 获取导出数据
        List<MatlOutOrder> queryList = matlOutOrderService.list(queryWrapper);
        // 过滤选中数据
        String selections = request.getParameter("selections");
        List<MatlOutOrder> matlOutOrderList = new ArrayList<MatlOutOrder>();
        if (oConvertUtils.isEmpty(selections)) {
            matlOutOrderList = queryList;
        } else {
            List<String> selectionList = Arrays.asList(selections.split(","));
            matlOutOrderList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
        }

        // Step.3 组装pageList
        List<MatlOutOrderPage> pageList = new ArrayList<MatlOutOrderPage>();
        for (MatlOutOrder main : matlOutOrderList) {
            MatlOutOrderPage vo = new MatlOutOrderPage();
            BeanUtils.copyProperties(main, vo);
            List<MatlOutOrderDtl> matlOutOrderDtlList = matlOutOrderDtlService.selectByMainId(main.getId());
            vo.setMatlOutOrderDtlList(matlOutOrderDtlList);
            pageList.add(vo);
        }

        // Step.4 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "领用单主表列表");
        mv.addObject(NormalExcelConstants.CLASS, MatlOutOrderPage.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("领用单主表数据", "导出人:" + sysUser.getRealname(), "领用单主表"));
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
    @RequiresPermissions("outorder.in")
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
                List<MatlOutOrderPage> list = ExcelImportUtil.importExcel(file.getInputStream(), MatlOutOrderPage.class, params);
                for (MatlOutOrderPage page : list) {
                    MatlOutOrder po = new MatlOutOrder();
                    BeanUtils.copyProperties(page, po);
                    matlOutOrderService.saveMain(po, page.getMatlOutOrderDtlList());
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
     * 单条或多条删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "领用单明细表-通过id删除")
    @ApiOperation(value = "领用单明细表-通过id删除", notes = "领用单明细表-通过id删除")
    @DeleteMapping(value = "/deleteMatlOutOrderDtl")
    @RequiresPermissions("outorder.delete")
    public Result<?> deleteList(@RequestParam(name = "ids", required = true) List<String> ids) {
        matlOutOrderDtlService.deleteList(ids);
        return Result.ok("删除成功!");
    }


    /**
     * 领用单审核操作
     * status: 0: 未审核,   1: 已审核
     *
     * @param matlOutOrders: 前端传过来的记录数组
     * @return
     */
    @PostMapping("/check")
    @RequiresPermissions("outorder.check")
    public synchronized Result<Object> check(@RequestBody ArrayList<MatlOutOrder> matlOutOrders) {
        String result = matlOutOrderService.checks(matlOutOrders);
        return Result.OK(result);
    }


    /**
     * 领料单明细冲红
     *
     * @param matlOutOrderPage: 领料单明细表单
     * @return
     */
    @AutoLog(value = "领货明细冲红")
    @ApiOperation(value = "领货明细冲红", notes = "领货明细冲红")
    @PutMapping(value = "/redFlushDtl")
    @RequiresPermissions("outorder.redflushdtl")
    public Result<?> redFlushByDtl(@RequestBody MatlOutOrderPage matlOutOrderPage) {
        try {
            matlOutOrderService.redFlushByIds(matlOutOrderPage.getMatlOutOrderDtlList());
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
    @RequiresPermissions("outorder.redflush")
    public Result<?> redFlush(@RequestParam(name = "ids", required = false) String ids) {
        try {
            matlOutOrderService.redFlushById(ids);
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
    @AutoLog(value = "领用单明细表-通过主表ID查询")
    @ApiOperation(value = "领用单明细表-通过主表ID查询", notes = "领用单明细表-通过主表ID查询")
    @GetMapping(value = "/queryNoRedDtlListByMainId")
    public Result<?> queryNoRedDtlListByMainId(@RequestParam(name = "id", required = true) String id) {
        List<MatlOutOrderDtl> matlOutOrderDtlList = matlOutOrderDtlService.selectNoRedFlushDtlByMainId(id);
        IPage<MatlOutOrderDtl> page = new Page<>();
        page.setRecords(matlOutOrderDtlService.addTempField(matlOutOrderDtlList));
        page.setTotal(matlOutOrderDtlList.size());
        return Result.ok(page);
    }

}
