package com.dongxin.erp.ps.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.system.base.controller.BaseController;
import com.dongxin.erp.ps.entity.PsWbsNode;
import com.dongxin.erp.ps.service.PsWbsNodeService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

 /**
 * @Description: WBS节点信息
 * @Author: jeecg-boot
 * @Date:   2020-11-13
 * @Version: V1.0
 */
@Api(tags="WBS节点信息")
@RestController
@RequestMapping("/ps/psWbsNode")
@Slf4j
public class PsWbsNodeController extends BaseController<PsWbsNode, PsWbsNodeService> {

}
