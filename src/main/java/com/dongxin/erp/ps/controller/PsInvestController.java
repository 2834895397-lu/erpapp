package com.dongxin.erp.ps.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.system.base.controller.BaseController;
import com.dongxin.erp.ps.entity.PsInvest;
import com.dongxin.erp.ps.service.PsInvestService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

 /**
 * @Description: 投资完成进度
 * @Author: jeecg-boot
 * @Date:   2020-11-13
 * @Version: V1.0
 */
@Api(tags="投资完成进度")
@RestController
@RequestMapping("/ps/psInvest")
@Slf4j
public class PsInvestController extends BaseController<PsInvest, PsInvestService> {

}
