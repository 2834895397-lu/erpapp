package com.dongxin.erp.ps.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.system.base.controller.BaseController;
import com.dongxin.erp.ps.entity.PsContractInfo;
import com.dongxin.erp.ps.service.PsContractInfoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

 /**
 * @Description: 工程合同信息
 * @Author: jeecg-boot
 * @Date:   2020-11-13
 * @Version: V1.0
 */
@Api(tags="工程合同信息")
@RestController
@RequestMapping("/ps/psContractInfo")
@Slf4j
public class PsContractInfoController extends BaseController<PsContractInfo, PsContractInfoService> {

}
