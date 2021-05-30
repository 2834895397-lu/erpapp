package com.dongxin.erp.bm.mapper;

import com.dongxin.erp.bm.entity.PurchasePlan;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 招标采购计划明细
 * @Author: huangheng
 * @Date:   2020-12-21
 * @Version: V1.0
 */
public interface PurchasePlanMapper extends BaseMapper<PurchasePlan> {
	
	void updateDelFlagByBiddingDetailId(@Param("biddingDetailId") String biddingDetailId,@Param("tenant") String tenant);

}
