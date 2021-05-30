package com.dongxin.erp.bd.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.system.base.controller.BaseController;
import com.dongxin.erp.bd.entity.Unit;
import com.dongxin.erp.bd.service.UnitService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

 /**
 * @Description: 计量单位信息
 * @Author: jeecg-boot
 * @Date:   2020-11-10
 * @Version: V1.0
 */
@Api(tags="计量单位信息")
@RestController
@RequestMapping("/bd/unit")
@Slf4j
public class UnitController extends BaseController<Unit, UnitService> {

}
