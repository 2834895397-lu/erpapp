package com.dongxin.erp.ps1.service;

import com.dongxin.erp.bd.entity.AttachedUrl;
import com.dongxin.erp.bd.mapper.AttachedUrlMapper;
import com.dongxin.erp.enums.ContractStatus;
import com.dongxin.erp.enums.ContractTaxFlag;
import com.dongxin.erp.ps.entity.Project;
import com.dongxin.erp.ps1.entity.TpsContract;
import com.dongxin.erp.ps1.entity.TpsContractFund;
import com.dongxin.erp.ps1.entity.TpsContractSave;
import com.dongxin.erp.ps1.service.TpsContractFundService;
import com.dongxin.erp.ps1.mapper.TpsContractMapper;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.jeecg.common.system.base.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 合同信息表
 * @Author: jeecg-boot
 * @Date:   2021-01-22
 * @Version: V1.0
 */
@Service
public class TpsContractService extends BaseService<TpsContractMapper, TpsContract>{

	@Autowired
	private TpsContractFundService tpsContractFundService;

	@Autowired
	private AttachedUrlMapper attachedUrlMapper;

	@Transactional
	public void delMain(String id) {
		tpsContractFundService.deleteBatchByMainId(id);
		logicDeleteById(id);
	}

	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			tpsContractFundService.deleteBatchByMainId(id.toString());
			logicDeleteById(id);
		}
	}

	@Transactional
	public void add(TpsContractSave tpsContractSave) {
		TpsContract tpsContract  = copyContract(tpsContractSave);
        tpsContract.setStatus(ContractStatus.Checking.getCode());
		this.save(tpsContract);

		// 附件新增
		String fileUrl = tpsContractSave.getUrl();
		if (!StringUtil.isNullOrEmpty(fileUrl)) {
			AttachedUrl attachedUrl = new AttachedUrl();
			attachedUrl.setRelationId(tpsContractSave.getId());
			attachedUrl.setUrl(fileUrl);
			attachedUrl.setSort(1);
			attachedUrlMapper.insert(attachedUrl);
		}
	}

	@Transactional
	public void check(List<TpsContract> tpsContractList) {
		for (TpsContract tpsContract : tpsContractList) {
			if (tpsContract.getStatus().equals(ContractStatus.Checking.getCode()))
			{
				tpsContract.setStatus(ContractStatus.CheckSuccess.getCode());  //设置审核通过
				this.updateById(tpsContract);
			}
		}

	}

    @Transactional
    public void edit(TpsContractSave tpsContractSave) {
        TpsContract tpsContract = copyContract(tpsContractSave);
        this.updateById(tpsContract);

        // 附件新增
        String fileUrl = tpsContractSave.getUrl();
        if (!StringUtil.isNullOrEmpty(fileUrl)) {
            AttachedUrl attachedUrl = new AttachedUrl();
            attachedUrl.setRelationId(tpsContractSave.getId());
            attachedUrl.setUrl(fileUrl);
            attachedUrl.setSort(1);
            attachedUrlMapper.updateById(attachedUrl);
        }
    }

    public TpsContract copyContract(TpsContractSave tpsContractSave){
        TpsContract tpsContract = new TpsContract();
        BeanUtils.copyProperties(tpsContractSave, tpsContract);

        if (true == tpsContractSave.getTaxFlag() ){
            tpsContract.setTaxFlag(ContractTaxFlag.TaxFlagSuccess.getCode());
        }else {
            tpsContract.setTaxFlag(ContractTaxFlag.TaxFlagFail.getCode());
        }
        return tpsContract;
    }
}
