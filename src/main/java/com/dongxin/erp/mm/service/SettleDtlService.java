package com.dongxin.erp.mm.service;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dongxin.erp.bd.entity.Material;
import com.dongxin.erp.bm.entity.BiddingDtl;
import com.dongxin.erp.mm.entity.OrderDtl;
import com.dongxin.erp.mm.entity.Settle;
import com.dongxin.erp.mm.entity.SettleDtl;
import com.dongxin.erp.mm.mapper.SettleDtlMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeecg.common.system.base.service.BaseService;
import org.jeecg.config.mybatis.TenantContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 采购结算明细表
 * @Author: jeecg-boot
 * @Date:   2020-11-23
 * @Version: V1.0
 */
@Service
public class SettleDtlService extends BaseService<SettleDtlMapper, SettleDtl> {

	@Autowired
	private SettleDtlMapper settleDtlMapper;

	/**
	 * 通过采购结算id查询明细
	 * @param mainId
	 * @return
	 */
	public List<SettleDtl> querySettleDtlListByMainId(String mainId){
		List<SettleDtl> settleDtlList = this.selectByMainId(mainId);
		// 显示单据号，存放IDd
		//this.setContractNumber(settleDtlList);
		
		return settleDtlList;
	}
	
	/**
	 * 更加采购结算id获取有效明细列表
	 * @param mainId
	 * @return
	 */
	public List<SettleDtl> selectByMainId(String mainId) {
		return settleDtlMapper.selectByMainId(mainId,TenantContext.getTenant());
	}

	//显示单据号，数据库存对应的ID
	public Map<String,String> selectContractNumber(){
		List<Map<String, String>> maps = settleDtlMapper.selectContractNumber();
		Map <String,String> idAndNumberOfMaps = new HashMap<>();
		for(Map<String,String> map :maps){
			idAndNumberOfMaps.put(MapUtil.getStr(map,"id"),MapUtil.getStr(map,"contract_number"));
		}
		return idAndNumberOfMaps;
	}
	public void setContractNumber(List<SettleDtl> settleDtlList){
		Map<String, String> idAndNumberOfMaps = this.selectContractNumber();
		for (SettleDtl settleDtl :settleDtlList){
			settleDtl.setContractNumber(MapUtil.getStr(idAndNumberOfMaps,settleDtl.getTmmContractDtlId()));
		}
	}
	
	/**
	 * 通过采购结算主表ID删除明细
	 * @param settleId
	 */
	public void deleteBySettleId(String settleId) {
		
		QueryWrapper<SettleDtl> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("tmm_settle_id", settleId);
		List<SettleDtl> settleDtlList = this.list(queryWrapper);
		for(SettleDtl settleDtl: settleDtlList) {
			this.logicDelete(settleDtl);
		}
	}
	
	/**
	 * 删除
	 * @param settleDtl
	 */
	public void deleteBySettleDtl(SettleDtl settleDtl) {
		this.logicDelete(settleDtl);
	}
	
	
	/**
	 *  通过ID、租户查询记录
	 * @param id
	 * @return
	 */
	public SettleDtl getSettleDtlById(String id){
		return settleDtlMapper.getSettleDtlById(id,TenantContext.getTenant());
	}
	
	/**
	 * 更新删除标识为0
	 * @param id
	 */
	public void updateDelFlagById(String id){
		settleDtlMapper.updateDelFlagById(id,TenantContext.getTenant());
	}
	
	/**
	 * 获取收货单审核日期
	 * @param tsmMatlInOrderDtlId
	 * @return
	 */
	public Date getVoucherTime(String tsmMatlInOrderDtlId) {
		return settleDtlMapper.getVoucherTime(tsmMatlInOrderDtlId);
	}
}
