package com.dongxin.erp.ps.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.system.base.controller.BaseController;
import com.dongxin.erp.ps.entity.PsProgress;
import com.dongxin.erp.ps.service.PsProgressService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

 /**
 * @Description: 进度
 * @Author: jeecg-boot
 * @Date:   2020-11-13
 * @Version: V1.0
 */
@Api(tags="进度")
@RestController
@RequestMapping("/ps/psProgress")
@Slf4j
public class PsProgressController extends BaseController<PsProgress, PsProgressService> {

}
