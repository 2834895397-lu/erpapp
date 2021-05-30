package com.dongxin.erp.cs.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dongxin.erp.cs.csenum.BlacklistStatus;
import com.dongxin.erp.cs.entity.BlacklistInf;
import com.dongxin.erp.cs.service.BlacklistInfService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.BaseController;
import org.jeecg.common.system.query.QueryGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

/**
 * @Description: 黑名单管理
 * @Author: jeecg-boot
 * @Date: 2020-11-17
 * @Version: V1.0
 */
@Api(tags = "黑名单管理")
@RestController
@RequestMapping("/cs/blacklistInf")
@Slf4j
public class BlacklistInfController extends BaseController<BlacklistInf, BlacklistInfService> {
    @Autowired
    private BlacklistInfService blacklistInfService;


    @Override
    @AutoLog("添加")
    @ApiOperation(value = "添加", notes = "添加")
    @PostMapping({"/add"})
    public Result<?> add(@RequestBody BlacklistInf param) {
        BlacklistStatus blacklist = BlacklistStatus.BLACKLIST;
        param.setStatus(blacklist.getCode());
        this.blacklistInfService.save(param);
        return Result.OK("添加成功！");
    }

    @AutoLog(value = "分页列表查询")
    @ApiOperation(value = "分页列表查询", notes = "分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(BlacklistInf param, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        QueryWrapper<BlacklistInf> queryWrapper = QueryGenerator.initQueryWrapper(param, req.getParameterMap());
        Page<BlacklistInf> page = new Page<BlacklistInf>(pageNo, pageSize);
        IPage<BlacklistInf> pageList = blacklistInfService.page(page, queryWrapper);

        //黑名单联动
        blacklistInfService.blacklistLinkage();

        return Result.OK(pageList);
    }

    @Override
    @DeleteMapping({"/delete"})
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {

        //为黑名单时不可删除
        String message = blacklistInfService.canNotDelete(id);
        if (message.equals("状态为黑名单时不可删除")) {
            return Result.error("状态为黑名单时不可删除");
        }

        //黑名单在删除时 将对应的客商类型表的信息重新设置为非正式
        blacklistInfService.resetCsType(id);

        this.blacklistInfService.removeById(id);
        return Result.OK("删除成功!");
    }

    @AutoLog("批量删除")
    @ApiOperation(value = "批量删除", notes = "批量删除")
    @DeleteMapping({"/deleteBatch"})
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {

        //黑名单在批量删除时 选中的数据如果包含状态为黑名单时 不可删除
        String message = blacklistInfService.canNotBatchDelete(ids);
        if (message.equals("状态为黑名单时不可删除!")) {
            return Result.error(message);
        }

        //黑名单在批量删除时 将对应的客商类型表的信息重新设置为非正式
        blacklistInfService.resetBatchCsType(ids);

        this.blacklistInfService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    @RequestMapping(value = "/recoverBlacklist")
    public Result<?> recoverBlacklist(@RequestBody List<String> ids) {

        //将选中的黑名单的对应客商类型 中的cs_type重新设置为非正式
        String message = blacklistInfService.recoverBlacklist(ids);

        return Result.OK(message);
    }


}
