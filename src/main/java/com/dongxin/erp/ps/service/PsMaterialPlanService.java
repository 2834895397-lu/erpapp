package com.dongxin.erp.ps.service;

import com.dongxin.erp.ps.entity.PsMaterialPlan;
import com.dongxin.erp.ps.mapper.PsMaterialPlanMapper;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.vo.LoginUser;
import org.springframework.stereotype.Service;
import org.jeecg.common.system.base.service.BaseService;

import java.util.Date;
import java.util.List;

/**
 * @Description: 项目作业物料采购
 * @Author: jeecg-boot
 * @Date:   2021-01-21
 * @Version: V1.0
 */
@Service
public class PsMaterialPlanService extends BaseService<PsMaterialPlanMapper, PsMaterialPlan> {

    public Result<?> batchUpdate(List<PsMaterialPlan> list) {

        LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        list.forEach(x->{
            x.setUpdateBy(user.getUsername());
            x.setUpdateTime(new Date());
            this.updateById(x);
        });

        return Result.OK();
    }
}
