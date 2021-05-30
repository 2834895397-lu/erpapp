package com.dongxin.erp.sm.service;

import com.dongxin.erp.sm.entity.MatlInOrderDtl;
import com.dongxin.erp.sm.entity.MatlMoveOrderDtl;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 移库单明细表
 * @Author: jeecg-boot
 * @Date:   2020-11-10
 * @Version: V1.0
 */
public interface IMatlMoveOrderDtlService extends IService<MatlMoveOrderDtl> {

	public List<MatlMoveOrderDtl> selectByMainId(String mainId);

    List<MatlMoveOrderDtl> addTempField(List<MatlMoveOrderDtl> matlMoveOrderDtlList);

    List<MatlMoveOrderDtl> selectNoRedFlushDtlByMainId(String mainId);
}
