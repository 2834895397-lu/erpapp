package com.dongxin.erp.bd.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import com.dongxin.erp.bd.entity.PurchaseOrg;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 采购组织表
 * @Author: jeecg-boot
 * @Date:   2021-01-14
 * @Version: V1.0
 */
public interface PurchaseOrgMapper extends BaseMapper<PurchaseOrg> {

    Map<String, String> selectDepart(@Param("id") String id);

    String selectPurchaseOrg(@Param("ids") String ids);

    String selectDepartName(@Param("ids") String ids);
}
