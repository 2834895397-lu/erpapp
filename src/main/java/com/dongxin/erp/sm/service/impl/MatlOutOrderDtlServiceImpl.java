package com.dongxin.erp.sm.service.impl;

import com.dongxin.erp.bd.service.MaterialService;
import com.dongxin.erp.sm.entity.MatlOutOrderDtl;
import com.dongxin.erp.sm.mapper.MatlOutOrderDtlMapper;
import com.dongxin.erp.sm.service.IMatlOutOrderDtlService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 领用单明细表
 * @Author: jeecg-boot
 * @Date:   2020-11-10
 * @Version: V1.0
 */
@Service
public class MatlOutOrderDtlServiceImpl extends ServiceImpl<MatlOutOrderDtlMapper, MatlOutOrderDtl> implements IMatlOutOrderDtlService {
	
	@Autowired
	private MatlOutOrderDtlMapper matlOutOrderDtlMapper;
	@Autowired
	private MaterialService materialService;
	
	@Override
	public List<MatlOutOrderDtl> selectByMainId(String mainId) {
		List<MatlOutOrderDtl> matlOutOrderDtls = matlOutOrderDtlMapper.selectByMainId(mainId , "0");
		return matlOutOrderDtls;
	}

	@Override
	public int deleteList(List<String> ids) {
		int i = matlOutOrderDtlMapper.deleteBatchIds(ids);
		return i;
	}

	//添加临时字段
	@Override
	public List<MatlOutOrderDtl> addTempField(List<MatlOutOrderDtl> matlOutOrderDtlList) {
		for (MatlOutOrderDtl matlOutOrderDtl : matlOutOrderDtlList) {
			matlOutOrderDtl.setTbdMaterialName(materialService.idAndName(matlOutOrderDtl.getTbdMaterialId()));
		}
		return matlOutOrderDtlList;
	}

	@Override
	public List<MatlOutOrderDtl> selectNoRedFlushDtlByMainId(String mainId) {
		List<MatlOutOrderDtl> matlOutOrderDtls = matlOutOrderDtlMapper.selectByMainId(mainId , "1");
		return matlOutOrderDtls;
	}
}
