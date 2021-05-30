package com.dongxin.erp.ps.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.BaseController;
import com.dongxin.erp.ps.entity.PsActivity;
import com.dongxin.erp.ps.service.PsActivityService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

 /**
 * @Description: 活动
 * @Author: jeecg-boot
 * @Date:   2020-11-13
 * @Version: V1.0
 */
@Slf4j
@Api(tags="活动")
@RestController
@RequestMapping("/ps/psActivity")
public class PsActivityController extends BaseController<PsActivity, PsActivityService> {

 }
