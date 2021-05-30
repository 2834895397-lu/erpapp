package com.dongxin.erp.sm.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.BaseController;
import com.dongxin.erp.sm.entity.MatlBalance;
import com.dongxin.erp.sm.service.MatlBalanceService;
import org.jeecg.common.system.query.QueryGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: 日结存表
 * @Author: jeecg-boot
 * @Date: 2020-11-10
 * @Version: V1.0
 */
@Api(tags = "日结存表")
@RestController
@RequestMapping("/sm/matlBalance")
@Slf4j
public class MatlBalanceController extends BaseController<MatlBalance, MatlBalanceService> {
    @Autowired
    private MatlBalanceService matlBalanceService;

    @Override
    @GetMapping({"/list"})
    public Result<?> queryPageList(MatlBalance param, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        matlBalanceService.addRecords();
        QueryWrapper<MatlBalance> queryWrapper = QueryGenerator.initQueryWrapper(param, req.getParameterMap());
        Page<MatlBalance> page = new Page((long) pageNo, (long) pageSize);
        IPage<MatlBalance> pageList = matlBalanceService.page(page, queryWrapper);
        return Result.OK(pageList);
    }
}
