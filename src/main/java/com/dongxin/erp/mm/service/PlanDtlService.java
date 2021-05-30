package com.dongxin.erp.mm.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dongxin.erp.bd.service.MaterialService;
import com.dongxin.erp.bm.entity.BiddingDtl;
import com.dongxin.erp.bm.entity.PurchasePlan;
import com.dongxin.erp.bm.service.BiddingDtlService;
import com.dongxin.erp.bm.service.PurchasePlanService;
import com.dongxin.erp.mm.entity.PlanDtl;
import com.dongxin.erp.mm.mapper.PlanDtlMapper;
import org.jeecg.config.mybatis.TenantContext;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import org.jeecg.common.system.base.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 采购申请明细表
 * @Author: jeecg-boot
 * @Date: 2020-11-18
 * @Version: V1.0
 */
@Service
public class PlanDtlService extends BaseService<PlanDtlMapper, PlanDtl> {

	@Autowired
	private PlanDtlMapper planDtlMapper;
	@Autowired
	BiddingDtlService biddingDtlService;
	@Autowired
	private MaterialService materialService;
	@Autowired
	private PlanDtlService planDtlService;
	
	@Autowired
	private PurchasePlanService purchasePlanService;

	public List<PlanDtl> selectByMainId(String mainId) {
		return planDtlMapper.selectByMainId(mainId);
	}

	public List<BiddingDtl> purchaseDetail(BiddingDtl biddingDtl) {

		// 采购申请明细表查询
		List<BiddingDtl> biddingDtls = planDtlMapper.selectPurchaseDetail(biddingDtl.getPlanNo(),TenantContext.getTenant());
		// 招标明细信息表查询
		List<BiddingDtl> biddingDtlList = biddingDtlService.list();
		biddingDtls.removeAll(biddingDtlList);
		return biddingDtls;
	}

	public List<BiddingDtl> planDetail(BiddingDtl biddingDtl) {
		List<BiddingDtl> biddingDtls = planDtlService.purchaseDetail(biddingDtl);

		// 翻译计量单位
		Map<String, String> idAndNameOfMaps = materialService.selectUnit();
		for (BiddingDtl biddingDtl1 : biddingDtls) {
			String mea_no = biddingDtl1.getMeasureUnit();
			biddingDtl1.setMeasureUnitName(idAndNameOfMaps.get(mea_no));
		}
		return biddingDtls;
	}

	/**
	 * 获取未选择的采购明细信息
	 * huangheng 2020-12-22 15:23
	 * @param biddingInfId
	 * @return
	 */
	public List<BiddingDtl> getPurchaseDetail(String biddingInfId) {

		// 获取所有采购申请明细信息
		List<BiddingDtl> biddingDtls = planDtlMapper.selectPurchaseDetail(null,TenantContext.getTenant());
		
		//通过查询招标采购明细计划,剔除已选择记录
		QueryWrapper<PurchasePlan> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("bidding_inf_id", biddingInfId);
		queryWrapper.eq("del_flag", 0);
		List<PurchasePlan> purchasePlanList = purchasePlanService.list(queryWrapper);
		if(purchasePlanList.size()>0) {
		  for(int i=0;i<purchasePlanList.size();i++) {
			  String purchasePlanId1  = purchasePlanList.get(i).getPurchasePlanId();
			  for(int j=0;j<biddingDtls.size();j++) {
				  //采购申请明细号相等时，剔除
				  String purchasePlanId2  = biddingDtls.get(j).getId();
				  if(purchasePlanId1.equals(purchasePlanId2)) {
					  biddingDtls.remove(j);
				  }
			  }
		  }
		}
		// 翻译计量单位
		Map<String, String> idAndNameOfMaps = materialService.selectUnit();
		for (BiddingDtl biddingDtl1 : biddingDtls) {
			String mea_no = biddingDtl1.getMeasureUnit();
			biddingDtl1.setMeasureUnitName(idAndNameOfMaps.get(mea_no));
		}
		return biddingDtls;
	}

	/*
	 * //显示物料编号 public Map<String, String> selectMaterial() { Map<String, String>
	 * idAndCodeOfMaps = new HashMap<>(); List<Map<String, String>> maps =
	 * planDtlMapper.selectMaterial(); for (Map map : maps) {
	 * idAndCodeOfMaps.put(MapUtil.getStr(map, "id"), MapUtil.getStr(map, "code"));
	 * 
	 * } return idAndCodeOfMaps; }
	 */

}
