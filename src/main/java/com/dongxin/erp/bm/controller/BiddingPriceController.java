package com.dongxin.erp.bm.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.system.base.controller.BaseController;
import com.dongxin.erp.bm.entity.BiddingPrice;
import com.dongxin.erp.bm.service.BiddingPriceService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

 /**
 * @Description: 招标报价表
 * @Author: jeecg-boot
 * @Date:   2020-11-25
 * @Version: V1.0
 */
@Api(tags="招标报价表")
@RestController
@RequestMapping("/bm/biddingPrice")
@Slf4j
public class BiddingPriceController extends BaseController<BiddingPrice, BiddingPriceService> {

}
