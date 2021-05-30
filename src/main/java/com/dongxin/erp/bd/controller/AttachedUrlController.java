package com.dongxin.erp.bd.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.BaseController;
import com.dongxin.erp.bd.entity.AttachedUrl;
import com.dongxin.erp.bd.service.AttachedUrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

 /**
 * @Description: 附件地址表
 * @Author: huangheng
 * @Date:   2020-12-24
 * @Version: V1.0
 */
@Api(tags="附件地址表")
@RestController
@RequestMapping("/bd/attachedUrl")
@Slf4j
public class AttachedUrlController extends BaseController<AttachedUrl, AttachedUrlService> {
	
	@Autowired
	private AttachedUrlService attachedUrlService;

	@AutoLog(value = "附件地址表-通过关联主键查询附件地址")
	@ApiOperation(value = "附件地址表-通过关联主键查询附件地址", notes = "附件地址表-通过关联主键查询附件地址")
	@GetMapping(value = "/getAttachedUrlByRelationId")
	public Result<?> getAttachedUrlByRelationId(@RequestParam(name = "id", required = true) String relationId) {
		String urls = attachedUrlService.getAttachedUrlByRelationId(relationId);
		return Result.OK(urls);
	}
}
