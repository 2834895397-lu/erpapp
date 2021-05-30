package com.dongxin.erp.bm.service;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dongxin.erp.bd.service.MaterialService;
import com.dongxin.erp.bm.entity.BiddingDtl;
import com.dongxin.erp.bm.entity.BiddingEnterprise;
import com.dongxin.erp.bm.entity.CompanyOffer;
import com.dongxin.erp.bm.entity.PurchasePlan;
import com.dongxin.erp.bm.mapper.BiddingDtlMapper;
import com.dongxin.erp.mm.entity.PlanDtl;
import com.dongxin.erp.mm.service.PlanDtlService;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.config.mybatis.TenantContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeecg.common.system.base.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 招标明细信息表
 * @Author: jeecg-boot
 * @Date: 2020-11-25
 * @Version: V1.0
 */
@Service
public class BiddingDtlService extends BaseService<BiddingDtlMapper, BiddingDtl> {

	@Autowired
	private BiddingDtlMapper biddingDtlMapper;

	@Autowired
	private PlanDtlService planDtlService;

	@Autowired
	private MaterialService materialService;

	@Autowired
	private PurchasePlanService purchasePlanService;

	public List<CompanyOffer> getCompanyOffer(BiddingEnterprise biddingEnterprise) {
		// String biddingInfId = biddingEnterprise.getBiddingInfId();
		return biddingDtlMapper.selectDtl(biddingEnterprise);

	}

	public List<BiddingDtl> selectByMainId(String mainId) {
		return biddingDtlMapper.selectByMainId(mainId, TenantContext.getTenant());
	}

//    public List<BiddingDtl> selectBiddingDtl(String id){
//		QueryWrapper<BiddingDtl> wrapper = new QueryWrapper<>();
//		wrapper.eq("bidding_inf_id", id);
//		List<BiddingDtl> list = list(wrapper);
//		return list;
//	}

	public Map<String, String> selectUnit() {
		Map<String, String> idAndNameOfMaps = new HashMap<>();
		List<Map<String, String>> maps = this.biddingDtlMapper.selectUnit(TenantContext.getTenant());
		for (Map map : maps) {
			idAndNameOfMaps.put(MapUtil.getStr(map, "id"), MapUtil.getStr(map, "name"));
		}
		return idAndNameOfMaps;

	}

	/**
	 * 添加物料明细
	 * 
	 * @param biddingDtl
	 * @param id
	 * @throws Exception
	 */
	@Transactional
	public void addMaterialDtl(List<BiddingDtl> biddingDtl, String id) throws Exception {
        
		//招标明细信息列表
		List<BiddingDtl> biddingDtlList = new ArrayList<BiddingDtl>();
		
		//招标采购计划明细列表
		List<PurchasePlan> purchasePlanList = new ArrayList<PurchasePlan>();

		for (BiddingDtl dtl : biddingDtl) {
			
			PurchasePlan tbmPurchasePlan = new PurchasePlan();
			tbmPurchasePlan.setPurchasePlanId(dtl.getId());//采购申请明细号
			tbmPurchasePlan.setBiddingInfId(dtl.getBiddingInfId());
			tbmPurchasePlan.setBiddingInfId(id);
			tbmPurchasePlan.setMaterielNo(dtl.getMaterielNo());
			tbmPurchasePlan.setMaterielName(dtl.getMaterielName());
			tbmPurchasePlan.setMeasureUnit(dtl.getMeasureUnit());
			tbmPurchasePlan.setMeasureNum(dtl.getMeasureNum());
			tbmPurchasePlan.setRemark(dtl.getRemark());
			purchasePlanList.add(tbmPurchasePlan);
			
			if (biddingDtlList.size() == 0) {
				dtl.setBiddingInfId(id);
				biddingDtlList.add(dtl);
			} else {
				Boolean if_exist = false;
				for (BiddingDtl biddingDetail : biddingDtlList) {
					// 物料号相同进行汇总
					if (dtl.getMaterielNo().equals(biddingDetail.getMaterielNo())) {
						biddingDetail.setMeasureNum(biddingDetail.getMeasureNum() + dtl.getMeasureNum());
						if_exist = true;
						break;
					}
				}
				if (if_exist == false) {
					dtl.setBiddingInfId(id);
					biddingDtlList.add(dtl);
				}
			}
		}
		
		//保存数据
		for (BiddingDtl biddingDetail : biddingDtlList) {
			String biddingDetailId = null;
			//先根据当前招投标号+物料编号查询招标明细信息表，如果存在则更新重量;否则新增
			BiddingDtl biddingDtlEntity = biddingDtlMapper.selectOneByBiddingInfIdAndMaterielNo(biddingDetail.getBiddingInfId(), biddingDetail.getMaterielNo(),TenantContext.getTenant());
			if(null == biddingDtlEntity ) {
				biddingDetail.setId(null);
				//招标明细添加
				this.save(biddingDetail);
				biddingDetailId = biddingDetail.getId();
			}else {
				biddingDtlEntity.setMeasureNum(biddingDtlEntity.getMeasureNum()+biddingDetail.getMeasureNum());
				this.updateById(biddingDtlEntity);
				biddingDetailId = biddingDtlEntity.getId();
			}

			//招标采购计划明细添加
			for (PurchasePlan purchasePlan : purchasePlanList) {
				if(biddingDetail.getMaterielNo().equals(purchasePlan.getMaterielNo())) {
					purchasePlan.setBiddingDetailId(biddingDetailId);
					purchasePlanService.save(purchasePlan);
				}
			}
		}
	}

	/**
	 * 根据招投标号删除
	 * @author huangheng
	 * @param biddingInfId 招投标号
	 */
	public void updateStatusByBiddingInfId(String biddingInfId) {

		QueryWrapper<BiddingDtl> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("bidding_inf_id", biddingInfId);
		List<BiddingDtl> BiddingDtlList = this.list(queryWrapper);
		for(BiddingDtl biddingDtl: BiddingDtlList) {
			this.logicDelete(biddingDtl);
		}
	}

	/**
	 * 根据ID获取记录
	 * @param id
	 * @return
	 */
	public BiddingDtl getBiddingDtlById(String id){
		return biddingDtlMapper.getBiddingDtlById(id,TenantContext.getTenant());
	}

	/**
	 * 更新删除标志为0-有效
	 * @param id
	 */
	public void updateDelFlagById(String id){
		biddingDtlMapper.updateDelFlagById(id,TenantContext.getTenant());
	}


	public List<BiddingDtl> listPlanNo(BiddingDtl biddingDtl) {

		if ((biddingDtl.getPlanNo() == null && biddingDtl.getEndTime() == null && biddingDtl.getBeginTime() == null)) {
			List<BiddingDtl> biddingDtls = planDtlService.planDetail(biddingDtl);
			return biddingDtls;
		}
		/*
		 * String planNo=biddingDtl.getPlanNo(); SimpleDateFormat sdf = new
		 * SimpleDateFormat("yyyy-MM-dd"); String
		 * planDate=sdf.format(biddingDtl.getPlanDate());
		 */
		return biddingDtlMapper.selctId(biddingDtl.getPlanNo(), biddingDtl.getBeginTime(), biddingDtl.getEndTime());

	}
}
