package com.dongxin.erp.sm.service;

import com.dongxin.erp.sm.entity.MatlMoveOrderDtl;
import com.dongxin.erp.sm.entity.MatlOutOrderDtl;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 领用单明细表
 * @Author: jeecg-boot
 * @Date:   2020-11-10
 * @Version: V1.0
 */
public interface IMatlOutOrderDtlService extends IService<MatlOutOrderDtl> {

	public List<MatlOutOrderDtl> selectByMainId(String mainId);

    public int deleteList(List<String> ids);

    List<MatlOutOrderDtl> addTempField(List<MatlOutOrderDtl> matlOutOrderDtlList);

    List<MatlOutOrderDtl> selectNoRedFlushDtlByMainId(String mainId);
}
