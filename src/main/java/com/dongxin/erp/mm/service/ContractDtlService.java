package com.dongxin.erp.mm.service;

import cn.hutool.core.map.MapUtil;
import com.dongxin.erp.mm.entity.ContractDtl;
import com.dongxin.erp.mm.mapper.ContractDtlMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeecg.common.system.base.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 采购合同明细表
 * @Author: jeecg-boot
 * @Date:   2020-11-18
 * @Version: V1.0
 */
@Service
public class ContractDtlService extends BaseService<ContractDtlMapper, ContractDtl> {
	
	@Autowired
	private ContractDtlMapper contractDtlMapper;

	public List<ContractDtl> selectByMainId(String mainId) {
		return contractDtlMapper.selectByMainId(mainId);
	}
//显示编号,存ID
	public Map<String,String> selectPlanNumber(){
		List<Map<String, String>> maps = contractDtlMapper.selectPlanNumber();
		Map <String,String> idAndNumberOfMaps = new HashMap<>();
		for(Map<String,String> map :maps){
			idAndNumberOfMaps.put(MapUtil.getStr(map,"id"),MapUtil.getStr(map,"plan_number"));
		}
		return idAndNumberOfMaps;
	}

	public void setPlanNumber(List<ContractDtl> contractDtlList){
		Map<String, String> idAndNumberOfMaps = this.selectPlanNumber();
		for (ContractDtl contractDtl :contractDtlList){
			contractDtl.setPlanNumber(MapUtil.getStr(idAndNumberOfMaps,contractDtl.getTmmPurchasePlanDtlId()));
		}
	}

//	public void checkMessage(List<ContractDtl> contractDtlList){
//		Map<String, String> messageOfMaps = thisns.checkMessage();
//		for (ContractDtl contractDtl :contractDtlList){
//			contractDtl.checkMessage(MapUtil.getStr(messageOfMapss,contractDtl.getTmmPurchasePlanDtlId()));
//		}
//	}

    public BigDecimal selectprice(String id) {
		BigDecimal sum=new BigDecimal(0);
	List<ContractDtl> price= contractDtlMapper.selectPrice(id);
         for(ContractDtl contractDtl:price){
         	sum= sum.add(BigDecimal.valueOf(contractDtl.getMatlPrice()*contractDtl.getMatlQty()));
		 }
		return sum;
    }
}
