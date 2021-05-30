package com.dongxin.erp.bd.service;

import com.dongxin.erp.bd.entity.MaterialType;
import com.dongxin.erp.bd.mapper.MaterialTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.jeecg.common.system.base.service.BaseService;

import java.util.List;

/**
 * @Description: 物料类型表
 * @Author: jeecg-boot
 * @Date:   2021-01-14
 * @Version: V1.0
 */
@Service
public class MaterialTypeService extends BaseService<MaterialTypeMapper, MaterialType> {
@Autowired
MaterialTypeMapper materialTypeMapper;
    public List<MaterialType> selectMaterialType(String orgId) {
        List<MaterialType> c=materialTypeMapper.selectMaterialType(orgId);
        return c ;
    }
}
