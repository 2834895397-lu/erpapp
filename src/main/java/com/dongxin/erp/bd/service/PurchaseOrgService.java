package com.dongxin.erp.bd.service;

import cn.hutool.core.map.MapUtil;
import com.dongxin.erp.bd.entity.PurchaseOrg;
import com.dongxin.erp.bd.mapper.PurchaseOrgMapper;
import org.jeecg.config.mybatis.TenantContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.jeecg.common.system.base.service.BaseService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 采购组织表
 * @Author: jeecg-boot
 * @Date:   2021-01-14
 * @Version: V1.0
 */
@Service
public class PurchaseOrgService extends BaseService<PurchaseOrgMapper, PurchaseOrg> {
@Autowired
PurchaseOrgMapper purchaseOrgMapper;

    public Map<String, String> selectDepart(String id) {
       Map<String, String> maps = this.purchaseOrgMapper.selectDepart(id);
        return maps;
    }

    public String selectMaterialType(String ids) {
        String purchaseOrgs=this.purchaseOrgMapper.selectPurchaseOrg(ids);
        return purchaseOrgs;
    }

    public String selectDepartName(String ids) {
        String departName=this.purchaseOrgMapper.selectDepartName(ids);
        return departName;
    }
}
