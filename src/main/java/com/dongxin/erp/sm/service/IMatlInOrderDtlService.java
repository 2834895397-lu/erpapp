package com.dongxin.erp.sm.service;

import com.dongxin.erp.sm.entity.MatlInOrderDtl;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 收货单明细表
 * @Author: jeecg-boot
 * @Date: 2020-11-11
 * @Version: V1.0
 */
public interface IMatlInOrderDtlService extends IService<MatlInOrderDtl> {

    List<MatlInOrderDtl> selectByMainId(String mainId);


    List<MatlInOrderDtl> selectNoRedFlushDtlByMainId(String mainId);


    List<MatlInOrderDtl> addTempField(List<MatlInOrderDtl> matlInOrderDtlList);
}

