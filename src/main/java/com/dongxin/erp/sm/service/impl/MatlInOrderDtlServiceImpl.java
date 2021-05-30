package com.dongxin.erp.sm.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dongxin.erp.bd.service.MaterialService;
import com.dongxin.erp.enums.SettleFlag;
import com.dongxin.erp.enums.Status;
import com.dongxin.erp.exception.ErpException;
import com.dongxin.erp.mm.entity.Order;
import com.dongxin.erp.mm.entity.OrderDtl;
import com.dongxin.erp.mm.entity.Settle;
import com.dongxin.erp.mm.entity.SettleDtl;
import com.dongxin.erp.mm.service.ContractService;
import com.dongxin.erp.mm.service.OrderDtlService;
import com.dongxin.erp.mm.service.OrderService;
import com.dongxin.erp.mm.service.SettleDtlService;
import com.dongxin.erp.sm.entity.MatlInOrder;
import com.dongxin.erp.sm.entity.MatlInOrderDtl;
import com.dongxin.erp.sm.entity.MatlOutOrderDtl;
import com.dongxin.erp.sm.mapper.MatlInOrderDtlMapper;
import com.dongxin.erp.sm.service.IMatlInOrderDtlService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description: 收货单明细表
 * @Author: jeecg-boot
 * @Date: 2020-11-11
 * @Version: V1.0
 */
@Service
public class MatlInOrderDtlServiceImpl extends ServiceImpl<MatlInOrderDtlMapper, MatlInOrderDtl> implements IMatlInOrderDtlService {
    @Autowired
    private MatlInOrderDtlMapper matlInOrderDtlMapper;

    @Autowired
    private MaterialService materialService;

    @Autowired
    private SettleDtlService settleDtlService;

    @Autowired
    private MatlInOrderServiceImpl matlInOrderService;

    @Autowired
    private OrderDtlService orderDtlService;

    @Autowired
    private OrderService orderService;
    @Autowired
    private ContractService contractService;


    @Override
    public List<MatlInOrderDtl> selectByMainId(String mainId) {
        List<MatlInOrderDtl> list = new ArrayList<>();
        LambdaQueryWrapper<MatlInOrderDtl> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MatlInOrderDtl::getTsmMatlInOrderId, mainId);
        List<MatlInOrderDtl> matlInOrderDtls = matlInOrderDtlMapper.selectList(wrapper);
        for (MatlInOrderDtl matlInOrderDtl : matlInOrderDtls) {
            if (matlInOrderDtl.getDelFlag() == 0) {
                list.add(matlInOrderDtl);
            }
        }

        return list;
    }

  /*  @Override
    //暂存临时字段
    public List<MatlInOrderDtl> addTempFiled(List<MatlInOrderDtl> matlInOrderDtlList) {
        for (MatlInOrderDtl matlInOrderDtl : matlInOrderDtlList) {
            //获得采购订单主表id
            OrderDtl orderDtl = orderDtlService.getById(matlInOrderDtl.getVoucherId());
            Order order = orderService.getById(orderDtl.getTmmPurchaseOrderId());
            matlInOrderDtl.setOrderNumber(order.getOrderNumber());
            matlInOrderDtl.setContractNumber(contractService.getById(matlInOrderDtl.getTmmContractId()).getContractNumber());
        }
        return matlInOrderDtlList;
    }*/

    @Override
    public List<MatlInOrderDtl> selectNoRedFlushDtlByMainId(String mainId) {
        List<MatlInOrderDtl> matlInOrderDtls = matlInOrderDtlMapper.selectByMainId(mainId, "1");
        return matlInOrderDtls;
    }

    @Override
    public List<MatlInOrderDtl> addTempField(List<MatlInOrderDtl> matlInOrderDtlList) {
        for (MatlInOrderDtl matlInOrderDtl : matlInOrderDtlList) {
            //获得采购订单主表id
            OrderDtl orderDtl = orderDtlService.getById(matlInOrderDtl.getVoucherId());
            Order order = orderService.getById(orderDtl.getTmmPurchaseOrderId());
            matlInOrderDtl.setOrderNumber(order.getOrderNumber());
            matlInOrderDtl.setContractNumber(contractService.getById(matlInOrderDtl.getTmmContractId()).getContractNumber());
        }
        return matlInOrderDtlList;
    }

    /**
     * 审核,更改结算标识
     *
     * @param settleList
     * @return
     */
    @Transactional
    public void changeSettleFlag(List<Settle> settleList) {

        for (Settle settle : settleList) {
            //已审核，跳过
            if (settle.getStatus().equals(Status.CHECK.getCode())) {
                continue;
            }
            //根据结算单id获取结算单明细
            QueryWrapper<SettleDtl> settleDtlQueryWrapper = new QueryWrapper<>();
            settleDtlQueryWrapper.in("tmm_settle_id", settle.getId());
            List<SettleDtl> settleDtlList = settleDtlService.list(settleDtlQueryWrapper);
            for (SettleDtl settleDtl : settleDtlList) {

                //获取收货单明细记录
                QueryWrapper<MatlInOrderDtl> matlInOrderDtlQueryWrapper = new QueryWrapper<>();
                matlInOrderDtlQueryWrapper.in("id", settleDtl.getTsmMatlInOrderDtlId());
                List<MatlInOrderDtl> matlInOrderDtlList = this.list(matlInOrderDtlQueryWrapper);
                for (MatlInOrderDtl matlInOrderDtl : matlInOrderDtlList) {
                    //收货单明细结算标识为1-已结算,报错
                    if (matlInOrderDtl.getSettleFlag().equals(SettleFlag.SETTLED.getCode())) {
                        throw new ErpException("操作失败,收货单明细已结算,结算单号:" + settle.getSettleNumber());
                    }

                    //只有审核通过的收货单才能对其明细表的结算标识进行修改
                    QueryWrapper<MatlInOrder> matlInOrderQueryWrapper = new QueryWrapper<>();
                    matlInOrderQueryWrapper.in("id", matlInOrderDtl.getTsmMatlInOrderId());
                    matlInOrderQueryWrapper.eq("status", Status.CHECK.getCode());
                    List<MatlInOrder> matlInOrderList = matlInOrderService.list(matlInOrderQueryWrapper);
                    if (CollectionUtil.isEmpty(matlInOrderList)) {
                        throw new ErpException("收货单尚未审核,对应结算单号:" + settle.getSettleNumber());
                    }
                    //更新为已结算
                    matlInOrderDtl.setSettleFlag(SettleFlag.SETTLED.getCode());
                    this.updateById(matlInOrderDtl);
                }

            }
        }
    }

    /**
     * 设置TmmSettleDtlId为null
     *
     * @param id
     */
    public void updateTmmSettleDtlIdById(String id) {
        matlInOrderDtlMapper.updateTmmSettleDtlIdById(id);
    }
}
