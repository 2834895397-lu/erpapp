package com.dongxin.erp.bd.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.system.base.controller.BaseController;
import com.dongxin.erp.bd.entity.PurchaseOrg;
import com.dongxin.erp.bd.service.PurchaseOrgService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

 /**
 * @Description: 采购组织表
 * @Author: jeecg-boot
 * @Date:   2021-01-14
 * @Version: V1.0
 */
@Api(tags="采购组织表")
@RestController
@RequestMapping("/bd/purchaseOrg")
@Slf4j
public class PurchaseOrgController extends BaseController<PurchaseOrg, PurchaseOrgService> {

}
