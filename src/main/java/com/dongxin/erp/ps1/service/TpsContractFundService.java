package com.dongxin.erp.ps1.service;

import com.dongxin.erp.enums.ContractFundStatus;
import com.dongxin.erp.ps1.entity.TpsContractFund;
import com.dongxin.erp.ps1.mapper.TpsContractFundMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import org.jeecg.common.system.base.service.BaseService;

import javax.transaction.Transactional;

/**
 * @Description: 合同资金表
 * @Author: jeecg-boot
 * @Date:   2021-01-22
 * @Version: V1.0
 */
@Service
public class TpsContractFundService extends BaseService<TpsContractFundMapper, TpsContractFund>{

	public List<TpsContractFund> selectByMainId(String mainId) {
		return baseMapper.selectByMainId(mainId);
	}

	public void deleteBatchByMainId(String id) {
        List<TpsContractFund> details = selectByMainId(id);
        for(TpsContractFund detail : details){
            logicDelete(detail);
        }
    }

    @Transactional
    public void check(List<TpsContractFund> tpsContractFundList) {
        for (TpsContractFund tpsContractFund : tpsContractFundList) {
            if (tpsContractFund.getStatus().equals(ContractFundStatus.Checking.getCode()))
            {
                tpsContractFund.setStatus(ContractFundStatus.CheckSuccess.getCode());  //设置审核通过
                this.updateById(tpsContractFund);
            }
        }

    }

    @Transactional
    public void add(TpsContractFund tpsFund) {
        tpsFund.setStatus(ContractFundStatus.Checking.getCode());
        this.save(tpsFund);
    }

}
