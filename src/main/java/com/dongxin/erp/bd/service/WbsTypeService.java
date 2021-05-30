package com.dongxin.erp.bd.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dongxin.erp.bd.entity.WbsType;
import com.dongxin.erp.bd.mapper.WbsTypeMapper;
import com.dongxin.erp.enums.ContEndFlag;
import com.dongxin.erp.mm.entity.Contract;
import com.dongxin.erp.mm.entity.ContractDtl;
import com.dongxin.erp.ps.entity.TpsWbsModel;
import com.dongxin.erp.ps.service.TpsWbsModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.jeecg.common.system.base.service.BaseService;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 项目wbs类型
 * @Author: jeecg-boot
 * @Date:   2021-01-14
 * @Version: V1.0
 */
@Service
public class WbsTypeService extends BaseService<WbsTypeMapper, WbsType> {
    @Autowired
    TpsWbsModelService tpsWbsModelService;

    public List<String> edit(WbsType wbsType) {
        String a = wbsType.getId();
        Integer b = wbsType.getStatusFlag();
        if (b == 0) {
            QueryWrapper<TpsWbsModel> tpsWbsModelQueryWrapper = new QueryWrapper<>();
            tpsWbsModelQueryWrapper.eq("wbs_type_code", a);
            List<String> ids = tpsWbsModelService.list(tpsWbsModelQueryWrapper).stream().map(TpsWbsModel::getWbsTypeCode).collect(Collectors.toList());
            return ids;
        }
        return null;
    }

    public List<String> delete(String id) {
        QueryWrapper<TpsWbsModel> tpsWbsModelQueryWrapper = new QueryWrapper<>();
        tpsWbsModelQueryWrapper.eq("wbs_type_code",id);
        List<String> ids = tpsWbsModelService.list(tpsWbsModelQueryWrapper).stream().map(TpsWbsModel::getWbsTypeCode).collect(Collectors.toList());
        return ids;
    }

    public Set<String> deleteBatch(String ids) {
        List<String> wIds=new ArrayList<>(Arrays.asList(ids.split(",")));
        QueryWrapper<TpsWbsModel> tpsWbsModelQueryWrapper = new QueryWrapper<>();
        tpsWbsModelQueryWrapper.in("wbs_type_code",wIds);
        Set<String> wbsIds = tpsWbsModelService.list(tpsWbsModelQueryWrapper).stream().map(TpsWbsModel::getWbsTypeCode).collect(Collectors.toSet());
    return wbsIds;
    }
}
