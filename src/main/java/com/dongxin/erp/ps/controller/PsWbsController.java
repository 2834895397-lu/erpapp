package com.dongxin.erp.ps.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.system.base.controller.BaseController;
import com.dongxin.erp.ps.entity.PsWbs;
import com.dongxin.erp.ps.service.PsWbsService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

 /**
 * @Description: WBS结构信息
 * @Author: jeecg-boot
 * @Date:   2020-11-13
 * @Version: V1.0
 */
@Api(tags="WBS结构信息")
@RestController
@RequestMapping("/ps/psWbs")
@Slf4j
public class PsWbsController extends BaseController<PsWbs, PsWbsService> {

}
