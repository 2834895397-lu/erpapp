package com.dongxin.erp.mm.service;

import cn.hutool.core.map.MapUtil;
import com.dongxin.erp.bd.service.MaterialService;
import com.dongxin.erp.exception.ErpException;
import com.dongxin.erp.mm.entity.ContractDtl;
import com.dongxin.erp.mm.entity.OrderDtl;
import com.dongxin.erp.mm.mapper.OrderDtlMapper;
import com.dongxin.erp.sm.entity.MatlInOrderDtl;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jeecg.common.system.base.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description: 采购订单明细表
 * @Author: jeecg-boot
 * @Date: 2020-11-18
 * @Version: V1.0
 */
@Service
public class OrderDtlService extends BaseService<OrderDtlMapper, OrderDtl> {

    @Autowired
    private OrderDtlMapper orderDtlMapper;

    @Autowired
    private MaterialService materialService;

    public List<OrderDtl> selectByMainId(String mainId) {
        return orderDtlMapper.selectByMainId(mainId);
    }

    //显示编号,存ID
    public Map<String, String> selectContractNumber() {
        List<Map<String, String>> maps = orderDtlMapper.selectContractNumber();
        Map<String, String> idAndNumberOfMaps = new HashMap<>();
        for (Map<String, String> map : maps) {
            idAndNumberOfMaps.put(MapUtil.getStr(map, "id"), MapUtil.getStr(map, "contract_number"));
        }
        return idAndNumberOfMaps;
    }

    public void setContractNumber(List<OrderDtl> orderDtlList) {
        Map<String, String> idAndNumberOfMaps = this.selectContractNumber();
        for (OrderDtl orderDtl : orderDtlList) {
            orderDtl.setContractNumber(MapUtil.getStr(idAndNumberOfMaps, orderDtl.getTmmContractDtlId()));
        }
    }

    /**
     * 入库之后修改订单明细
     *
     * @param matlInOrderDtlList
     */
    @Transactional
    public void updateVoucherDet(List<MatlInOrderDtl> matlInOrderDtlList) {
        for (MatlInOrderDtl matlInOrderDtl : matlInOrderDtlList) {
            OrderDtl orderDtl = this.getById(matlInOrderDtl.getVoucherId());
            if (orderDtl.getMatlQty() < matlInOrderDtl.getMatlQty()) {
                throw new ErpException(materialService.idAndName(matlInOrderDtl.getTbdMaterialId()) + "的入库数量多于订单数,入库失败!");
            }
            else {
                Integer count = orderDtl.getMatlQty() - matlInOrderDtl.getMatlQty();
                orderDtl.setMatlQty(count);
                this.updateById(orderDtl);
            }
        }
    }
}
