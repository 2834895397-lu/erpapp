package com.dongxin.erp.ps.controller;

import cn.hutool.core.convert.Convert;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.servlet.ShiroHttpSession;
import org.checkerframework.checker.units.qual.A;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.BaseController;
import com.dongxin.erp.ps.entity.TpsWbsModel;
import com.dongxin.erp.ps.service.TpsWbsModelService;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.config.shiro.ShiroConfig;
import org.jeecg.config.shiro.ShiroRealm;
import org.jeecg.modules.system.entity.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

/**
 * @Description: 工作分解模板
 * @Author: jeecg-boot
 * @Date:   2021-01-18
 * @Version: V1.0
 */
@Api(tags="工作分解模板")
@RestController
@RequestMapping("/ps/tpsWbsModel")
@Slf4j
public class TpsWbsModelController extends BaseController<TpsWbsModel, TpsWbsModelService> {

    @Autowired
    private TpsWbsModelService wbsModelService;

    @GetMapping("/wbsModelTree")
    @RequiresPermissions("ps:wbsModel:query")
    public Result<?> wbsModelTree() {
        return wbsModelService.wbsModelTree();
    }

    @GetMapping("/option")
    @RequiresPermissions("ps:wbsmodel:query")
    public Result<?> option() {
        return wbsModelService.option();
    }

    @Override
    @RequiresPermissions("ps:wbsmodel:edit")
    public Result<?> edit(@RequestBody TpsWbsModel wbsModel) {
        return super.edit(wbsModel);
    }

    @Override
    @RequiresPermissions("ps:wbsmodel:add")
    public Result<?> add(@RequestBody TpsWbsModel wbsModel) {

        LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        wbsModel.setCreateBy(user.getRealname())
                .setTenantId(Convert.toInt(user.getRelTenantIds()))
                .setCreateTime(new Date())
                .setDelFlag(0);

        return super.add(wbsModel);
    }


    @Override
    @RequiresPermissions("ps:wbsmodel:delete")
    public Result<?> deleteBatch(@RequestBody String ids) {
        return wbsModelService.deleteBatch(ids);
    }

}
