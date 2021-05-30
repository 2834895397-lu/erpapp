package com.dongxin.erp.bd.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.dongxin.erp.bd.entity.MaterialType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 物料类型表
 * @Author: jeecg-boot
 * @Date:   2021-01-14
 * @Version: V1.0
 */
public interface MaterialTypeMapper extends BaseMapper<MaterialType> {

    List<MaterialType> selectMaterialType(@Param("orgId") String orgId);
}
