package com.dongxin.erp.bd.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.system.base.controller.BaseController;
import com.dongxin.erp.bd.entity.ProjectType;
import com.dongxin.erp.bd.service.ProjectTypeService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

 /**
 * @Description: 项目类型表
 * @Author: jeecg-boot
 * @Date:   2021-01-14
 * @Version: V1.0
 */
@Api(tags="项目类型表")
@RestController
@RequestMapping("/bd/projectType")
@Slf4j
public class ProjectTypeController extends BaseController<ProjectType, ProjectTypeService> {

}
