package com.dongxin.erp.bm.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.system.base.controller.BaseController;
import com.dongxin.erp.bm.entity.PurchasePlan;
import com.dongxin.erp.bm.service.PurchasePlanService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

 /**
 * @Description: 招标采购计划明细
 * @Author: huangheng
 * @Date:   2020-12-21
 * @Version: V1.0
 */
@Api(tags="招标采购计划明细")
@RestController
@RequestMapping("/bm/tbmPurchasePlan")
@Slf4j
public class PurchasePlanController extends BaseController<PurchasePlan, PurchasePlanService> {

}
