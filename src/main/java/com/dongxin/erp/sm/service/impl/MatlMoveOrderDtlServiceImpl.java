package com.dongxin.erp.sm.service.impl;

import com.dongxin.erp.bd.service.NodeService;
import com.dongxin.erp.sm.entity.MatlMoveOrderDtl;
import com.dongxin.erp.sm.entity.MatlOutOrderDtl;
import com.dongxin.erp.sm.mapper.MatlMoveOrderDtlMapper;
import com.dongxin.erp.sm.service.IMatlMoveOrderDtlService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 移库单明细表
 * @Author: jeecg-boot
 * @Date:   2020-11-10
 * @Version: V1.0
 */
@Service
public class MatlMoveOrderDtlServiceImpl extends ServiceImpl<MatlMoveOrderDtlMapper, MatlMoveOrderDtl> implements IMatlMoveOrderDtlService {
	
	@Autowired
	private MatlMoveOrderDtlMapper matlMoveOrderDtlMapper;
	@Autowired
	private NodeService nodeService;
	
	@Override
	public List<MatlMoveOrderDtl> selectByMainId(String mainId) {
		List<MatlMoveOrderDtl> matlMoveOrderDtls = matlMoveOrderDtlMapper.selectByMainId(mainId , "0");
		return matlMoveOrderDtls;

	}

	@Override
	public List<MatlMoveOrderDtl> addTempField(List<MatlMoveOrderDtl> matlMoveOrderDtlList) {
		for (MatlMoveOrderDtl matlMoveOrderDtl : matlMoveOrderDtlList) {
			matlMoveOrderDtl.setFromTbdNodeName(nodeService.idAndName(matlMoveOrderDtl.getFromTbdNodeId()));
		}
		return matlMoveOrderDtlList;
	}

	@Override
	public List<MatlMoveOrderDtl> selectNoRedFlushDtlByMainId(String mainId) {
		List<MatlMoveOrderDtl> matlMoveOrderDtls = matlMoveOrderDtlMapper.selectByMainId(mainId , "1");
		return matlMoveOrderDtls;
	}
}
