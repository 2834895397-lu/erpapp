package com.dongxin.erp.ps.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.system.base.controller.BaseController;
import com.dongxin.erp.ps.entity.PsBudget;
import com.dongxin.erp.ps.service.PsBudgetService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

 /**
 * @Description: 预算
 * @Author: jeecg-boot
 * @Date:   2020-11-13
 * @Version: V1.0
 */
@Api(tags="预算")
@RestController
@RequestMapping("/ps/psBudget")
@Slf4j
public class PsBudgetController extends BaseController<PsBudget, PsBudgetService> {

}
