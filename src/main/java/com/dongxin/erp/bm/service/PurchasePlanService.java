package com.dongxin.erp.bm.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dongxin.erp.bm.entity.BiddingDtl;
import com.dongxin.erp.bm.entity.PurchasePlan;
import com.dongxin.erp.bm.mapper.PurchasePlanMapper;

import org.jeecg.config.mybatis.TenantContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import org.jeecg.common.system.base.service.BaseService;

/**
 * @Description: 招标采购计划明细
 * @Author: jeecg-boot
 * @Date: 2020-12-21
 * @Version: V1.0
 */
@Service
public class PurchasePlanService extends BaseService<PurchasePlanMapper, PurchasePlan> {
	
	@Autowired
	private PurchasePlanMapper purchasePlanMapper;

	/**
	 * 根据招投标号删除招标采购计划明细
	 * @param biddingInfId
	 * @param
	 */
	public void updateStatusByBiddingInfId(String biddingInfId) {

		QueryWrapper<PurchasePlan> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("bidding_inf_id", biddingInfId);
		List<PurchasePlan> purchasePlanList = this.list(queryWrapper);
		for (PurchasePlan purchasePlan : purchasePlanList) {
			this.logicDelete(purchasePlan);
		}
	}
	
	/**
	 * 更新删除标志为0-有效
	 * @param biddingDetailId 招投标号
	 */
	public void updateDelFlagByBiddingDetailId(String biddingDetailId){
		purchasePlanMapper.updateDelFlagByBiddingDetailId(biddingDetailId, TenantContext.getTenant());
	}
}
