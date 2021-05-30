package com.dongxin.erp.cs.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dongxin.erp.cs.entity.VisitInf;
import com.dongxin.erp.cs.service.ProfileInfService;
import com.dongxin.erp.cs.service.VisitInfService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.BaseController;
import org.jeecg.common.system.query.QueryGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * @Description: 顾客拜访登记
 * @Author: jeecg-boot
 * @Date: 2020-11-10
 * @Version: V1.0
 */
@Api(tags = "顾客拜访登记")
@RestController
@RequestMapping("/cs/visitInf")
@Slf4j
public class VisitInfController extends BaseController<VisitInf, VisitInfService> {

    @Autowired
    VisitInfService visitInfService;
    @Autowired
    VisitInf visitInf;
    @Autowired
    ProfileInfService profileInfService;

    @Override
    @AutoLog("分页列表查询")
    @ApiOperation(value = "分页列表查询", notes = "分页列表查询")
    @GetMapping({"/list"})
    public Result<?> queryPageList(VisitInf param, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {


        //拿到cs_id的值后将cs_id重新设置为null 这是为了配合下面的客商名称模糊查询
        String cs_id = param.getCsId();
        param.setCsId(null);


        QueryWrapper<VisitInf> queryWrapper = QueryGenerator.initQueryWrapper(param, req.getParameterMap());
        Page<VisitInf> page = new Page<VisitInf>(pageNo, pageSize);
        IPage<VisitInf> pageList = visitInfService.page(page, queryWrapper);

        //拜访时间范围查询
        pageList.setRecords(visitInfService.visitTimeRangeQuery(param,pageList.getRecords()));

        //客商名称模糊查询
        pageList.setRecords(visitInfService.fuzzyQueryByCsName(cs_id,pageList.getRecords()));

        //以下为翻译拜访省市
        visitInfService.tranlateProvinceOrCity(pageList.getRecords());

        return Result.OK(pageList);
    }

    /**
     *   添加
     *
     * @param param
     * @return
     */
    @AutoLog(value = "添加")
    @ApiOperation(value="添加", notes="添加")
    @PostMapping(value = "/add")
    @RequiresPermissions("csVisit.add")
    public Result<?> add(@RequestBody VisitInf param) {
        visitInfService.save(param);
        return Result.OK("添加成功！");
    }

    /**
     *  编辑
     *
     * @param param
     * @return
     */
    @AutoLog(value = "编辑")
    @ApiOperation(value="编辑", notes="编辑")
    @PutMapping(value = "/edit")
    @RequiresPermissions("csVisit.edit")
    public Result<?> edit(@RequestBody VisitInf param) {
        visitInfService.updateById(param);
        return Result.OK("编辑成功!");
    }

    /**
     *   通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "通过id删除")
    @ApiOperation(value="通过id删除", notes="通过id删除")
    @DeleteMapping(value = "/delete")
    @RequiresPermissions("csVisit.del")
    public Result<?> delete(@RequestParam(name="id",required=true) String id) {
        visitInfService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     *  批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "批量删除")
    @ApiOperation(value="批量删除", notes="批量删除")
    @DeleteMapping(value = "/deleteBatch")
    @RequiresPermissions("csVisit.deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
        visitInfService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }


    /**
     * 导出excel
     *
     * @param request
     * @param param
     */
    @RequestMapping(value = "/exportXls")
    @RequiresPermissions("csVisit.exportXls")
    public ModelAndView exportXls(HttpServletRequest request, VisitInf param) {
        return exportXls(request, param, currentModelClass(), "商品测试");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    @RequiresPermissions("csVisit.importExcel")
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return importExcel(request, response, currentModelClass());
    }

}
