package com.dongxin.erp.bm.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dongxin.erp.bm.entity.BiddingEnterprise;
import com.dongxin.erp.bm.entity.BiddingPrice;
import com.dongxin.erp.bm.mapper.BiddingEnterpriseMapper;
import com.dongxin.erp.enums.BiddingFlag;
import com.dongxin.erp.enums.Status;
import com.dongxin.erp.fm.entity.PayInf;
import org.jeecg.config.mybatis.TenantContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.jeecg.common.system.base.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description: 招标企业信息表
 * @Author: jeecg-boot
 * @Date: 2020-11-25
 * @Version: V1.0
 */
@Service
public class BiddingEnterpriseService extends BaseService<BiddingEnterpriseMapper, BiddingEnterprise> {

	@Autowired
	private BiddingEnterpriseMapper biddingEnterpriseMapper;
	@Autowired
	private BiddingEnterpriseService biddingEnterpriseService;

	public List<BiddingEnterprise> selectByMainId(String mainId) {
		return biddingEnterpriseMapper.selectByMainId(mainId);
	}

	@Transactional
	public void status(ArrayList<BiddingEnterprise> biddingEnterprises) {
		Date date = new Date();
		ArrayList<BiddingEnterprise> list = new ArrayList<>();
		for (BiddingEnterprise biddingEnterprise : biddingEnterprises) {
			biddingEnterprise.setBiddingStatus(BiddingFlag.BIDWIN.getCode());
			biddingEnterprise.setBiddingDate(date);
			list.add(biddingEnterprise);
		}
		biddingEnterpriseService.saveOrUpdateBatch(list);

	}

	public List<BiddingPrice> selectBiddingPrice(String id) {
		return biddingEnterpriseMapper.selectBiddingPrice(id, TenantContext.getTenant());
	}

	/**
	 * 根据招投标号删除招标企业信息记录
	 * 
	 * @author huangheng 2020-12-23
	 * @param biddingInfId
	 */
	public void deleteByBiddingInfId(String biddingInfId) {
		QueryWrapper<BiddingEnterprise> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("bidding_inf_id", biddingInfId);
		List<BiddingEnterprise> biddingEnterprises = this.list(queryWrapper);
		for (BiddingEnterprise biddingEnterprise : biddingEnterprises) {
			this.logicDelete(biddingEnterprise);
		}
	}

	/**
	 * 根据id查询招标企业信息记录
	 * 
	 * @param id
	 * @return
	 */
	public BiddingEnterprise getBiddingEnterpriseById(String id) {
		return biddingEnterpriseMapper.getBiddingEnterpriseById(id,TenantContext.getTenant());
	}

	/**
	 * 更新删除标志为0-有效
	 * 
	 * @param id
	 */
	public void updateDelFlagById(String id) {
		biddingEnterpriseMapper.updateDelFlagById(id,TenantContext.getTenant());
	}
}
