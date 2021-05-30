package com.dongxin.erp.sm.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.system.base.controller.BaseController;
import com.dongxin.erp.sm.entity.WasteBook;
import com.dongxin.erp.sm.service.WasteBookService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: 出入库流水表
 * @Author: jeecg-boot
 * @Date:   2020-11-10
 * @Version: V1.0
 */
@Api(tags="出入库流水表")
@RestController
@RequestMapping("/sm/wasteBook")
@Slf4j
public class WasteBookController extends BaseController<WasteBook, WasteBookService> {

}
