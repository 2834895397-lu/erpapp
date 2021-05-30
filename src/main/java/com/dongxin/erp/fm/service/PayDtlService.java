package com.dongxin.erp.fm.service;

import com.dongxin.erp.fm.entity.PayDtl;
import com.dongxin.erp.fm.mapper.PayDtlMapper;

import org.jeecg.common.system.base.service.BaseService;
import org.springframework.stereotype.Service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 财务付款明细信息
 * @Author: jeecg-boot
 * @Date:   2020-11-18
 * @Version: V1.0
 */
@Service
public class PayDtlService extends BaseService<PayDtlMapper, PayDtl>{
	
	@Autowired
	private PayDtlMapper payDtlMapper;

	public List<PayDtl> selectByMainId(String mainId) {
		return payDtlMapper.selectByMainId(mainId);
	}
}
