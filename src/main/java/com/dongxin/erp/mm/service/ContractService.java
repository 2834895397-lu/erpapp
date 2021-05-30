package com.dongxin.erp.mm.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dongxin.erp.enums.ContEndFlag;
import com.dongxin.erp.enums.Status;
import com.dongxin.erp.mm.entity.Contract;
import com.dongxin.erp.mm.entity.ContractDtl;
import com.dongxin.erp.mm.mapper.ContractDtlMapper;
import com.dongxin.erp.mm.mapper.ContractMapper;
import org.springframework.stereotype.Service;
import org.jeecg.common.system.base.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.*;

/**
 * @Description: 采购合同主表
 * @Author: jeecg-boot
 * @Date:   2020-11-18
 * @Version: V1.0
 */
@Service
public class ContractService extends BaseService<ContractMapper, Contract> {

	@Autowired
	private ContractMapper contractMapper;
	@Autowired
	private ContractDtlMapper contractDtlMapper;

	@Autowired
	ContractService contractService;

	@Autowired
	ContractDtlService contractDtlService;

	@Transactional
	public void saveMain(Contract contract, List<ContractDtl> contractDtlList) {
		contractMapper.insert(contract);
		if(contractDtlList!=null && contractDtlList.size()>0) {
			for(ContractDtl entity:contractDtlList) {
//				//短文本校验
//				if(entity.getTbdMaterialId()!=null){
//					entity.setShortText(entity.getTbdMaterialId());
//				}
				//外键设置
				entity.setTmmContractId(contract.getId());
				contractDtlMapper.insert(entity);
			}
		}
	}

	@Transactional
	public void updateMain(Contract contract,List<ContractDtl> contractDtlList) {
		List<String> receiveIds = new ArrayList<>();
		for (ContractDtl contractDtl : contractDtlList) {
			contractDtl.setTmmContractId(contract.getId());
			receiveIds.add(contractDtl.getId());
		}
		contractDtlService.saveOrUpdateBatch(contractDtlList);
		QueryWrapper<ContractDtl> wrapper = new QueryWrapper<>();
		wrapper.eq("tmm_contract_id", contract.getId());
		List<ContractDtl> list = contractDtlService.list(wrapper);
		updateById(contract);
		//如果相等, 则说明没有要删除的记录
		if (list.size() == contractDtlList.size()) {
			return;
		} else {
			//removeIds: 要删除的记录ids
			List<String> removeIds = new ArrayList<>();
			for (ContractDtl contractDtl : list) {
				removeIds.add(contractDtl.getId());
			}
			removeIds.removeAll(receiveIds);
			contractDtlMapper.deleteBatchIds(removeIds);
		}

//
//		//1.先删除子表数据
//		contractDtlMapper.deleteByMainId(contract.getId());
//
//		//2.子表数据重新插入
//		if(contractDtlList!=null && contractDtlList.size()>0) {
//			for(ContractDtl entity:contractDtlList) {
//				//外键设置
//				entity.setTmmContractId(contract.getId());
//				contractDtlMapper.insert(entity);
//			}
//		}
	}

	@Transactional
	public String delMain(String id) {
		contractDtlMapper.deleteByMainId(id);
		int i = contractMapper.deleteById(id);
		if (i == 0) {
			return "请选择未审核的记录删除";
		}
		return "已删除" + i + "条记录";
	}

	@Transactional
	public int delBatchMain(Collection<? extends Serializable> idList) {
		int i = contractMapper.deleteBatchIds(idList);
		QueryWrapper<ContractDtl> wrapper = new QueryWrapper<>();
		wrapper.in("tmm_contract_id",idList);
		contractDtlMapper.delete(wrapper);
		return i;
	}
//	审核按钮
	@Transactional
	public void check(ArrayList<Contract> contracts) {
		Date date = new Date();
		ArrayList<Contract> list = new ArrayList<>();
		for (Contract contract : contracts) {
			contract.setStatus(Status.CHECK.getCode());
			contract.setVoucherTime(date);
			list.add(contract);
		}
		 contractService.saveOrUpdateBatch(list);
//  完结按钮
	}
	@Transactional
	public void over(ArrayList<Contract> contracts) {
		ArrayList<Contract> list = new ArrayList<>();
		for (Contract contract : contracts) {
			contract.setContEndFlag(ContEndFlag.OVER.getCode());
			list.add(contract);
		}
		contractService.saveOrUpdateBatch(list);
	}

}
