package com.dongxin.erp.ps.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.system.base.controller.BaseController;
import com.dongxin.erp.ps.entity.PsContract;
import com.dongxin.erp.ps.service.PsContractService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

 /**
 * @Description: 合同信息表
 * @Author: jeecg-boot
 * @Date:   2021-01-18
 * @Version: V1.0
 */
@Api(tags="合同信息表")
@RestController
@RequestMapping("/ps/psContract")
@Slf4j
public class PsContractController extends BaseController<PsContract, PsContractService> {

}
