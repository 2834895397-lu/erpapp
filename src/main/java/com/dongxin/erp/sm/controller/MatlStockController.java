package com.dongxin.erp.sm.controller;

import com.dongxin.erp.sm.entity.CurrentStock;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.system.base.controller.BaseController;
import com.dongxin.erp.sm.entity.MatlStock;
import com.dongxin.erp.sm.service.MatlStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Description: 总结存表
 * @Author: jeecg-boot
 * @Date: 2020-11-10
 * @Version: V1.0
 */
@Api(tags = "总结存表")
@RestController
@RequestMapping("/sm/matlStock")
@Slf4j
public class MatlStockController extends BaseController<MatlStock, MatlStockService> {

    @Autowired
    MatlStockService matlStockService;

    @GetMapping("/currentStock")
    public List<CurrentStock> currentStock(CurrentStock currentStock) {
        List<CurrentStock> currentStocks = matlStockService.currentStock(currentStock);
        return currentStocks;
    }
}
