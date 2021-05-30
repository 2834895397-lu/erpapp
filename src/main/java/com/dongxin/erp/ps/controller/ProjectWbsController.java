package com.dongxin.erp.ps.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.system.base.controller.BaseController;
import com.dongxin.erp.ps.entity.ProjectWbs;
import com.dongxin.erp.ps.service.ProjectWbsService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

 /**
 * @Description: 项目工作分解结构
 * @Author: jeecg-boot
 * @Date:   2021-01-20
 * @Version: V1.0
 */
@Api(tags="项目工作分解结构")
@RestController
@RequestMapping("/ps/projectWbs")
@Slf4j
public class ProjectWbsController extends BaseController<ProjectWbs, ProjectWbsService> {

}
