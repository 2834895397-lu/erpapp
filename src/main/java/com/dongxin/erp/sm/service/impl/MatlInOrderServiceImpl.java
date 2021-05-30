package com.dongxin.erp.sm.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dongxin.erp.enums.BillProp;
import com.dongxin.erp.enums.Status;
import com.dongxin.erp.exception.ErpException;
import com.dongxin.erp.mm.service.OrderDtlService;
import com.dongxin.erp.sm.entity.*;
import com.dongxin.erp.sm.mapper.MatlInOrderDtlMapper;
import com.dongxin.erp.sm.mapper.MatlInOrderMapper;
import com.dongxin.erp.sm.service.*;
import io.netty.util.internal.StringUtil;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 收货单主表
 * @Author: jeecg-boot
 * @Date: 2020-11-11
 * @Version: V1.0
 */
@Service
public class MatlInOrderServiceImpl extends ServiceImpl<MatlInOrderMapper, MatlInOrder> implements IMatlInOrderService {

    @Autowired
    private MatlInOrderMapper matlInOrderMapper;
    @Autowired
    private MatlInOrderDtlMapper matlInOrderDtlMapper;

    @Autowired
    MatlInOrderDtlServiceImpl matlInOrderDtlService;
    @Autowired
    WasteBookService wasteBookService;
    @Autowired
    MatlBalanceService matlBalanceService;
    @Autowired
    private OrderDtlService orderDtlService;
    @Autowired
    MatlStockService matlStockService;


    @Override
    @Transactional
    public void saveMain(MatlInOrder matlInOrder, List<MatlInOrderDtl> matlInOrderDtlList) {
        matlInOrder.setBillProp(BillProp.BLUE.getValue());
        matlInOrderMapper.insert(matlInOrder);
        if (matlInOrderDtlList != null && matlInOrderDtlList.size() > 0) {
            for (MatlInOrderDtl entity : matlInOrderDtlList) {
                //外键设置
                entity.setTsmMatlInOrderId(matlInOrder.getId());
                matlInOrderDtlMapper.insert(entity);
            }
        }
    }

    @Transactional
    public void saveAndUpdateOrderDet(MatlInOrder matlInOrder, List<MatlInOrderDtl> matlInOrderDtlList) {
        saveMain(matlInOrder, matlInOrderDtlList);
        orderDtlService.updateVoucherDet(matlInOrderDtlList);
    }


   /* @Override
    @Transactional
    public void updateMain(MatlInOrder matlInOrder, List<MatlInOrderDtl> matlInOrderDtlList) {
        matlInOrderMapper.updateById(matlInOrder);

        //1.先删除子表数据
        matlInOrderDtlMapper.deleteByMainId(matlInOrder.getId());

        //2.子表数据重新插入
        if (matlInOrderDtlList != null && matlInOrderDtlList.size() > 0) {
            for (MatlInOrderDtl entity : matlInOrderDtlList) {
                //外键设置
                entity.setTsmMatlInOrderId(matlInOrder.getId());
                matlInOrderDtlMapper.insert(entity);
            }
        }
    }*/

   /* @Override
    @Transactional
    public void delMain(String id) {
        matlInOrderDtlMapper.deleteByMainId(id);
        matlInOrderMapper.deleteById(id);
    }*/

    /*@Override
    @Transactional
    public void delBatchMain(Collection<? extends Serializable> idList) {
        for (Serializable id : idList) {
            matlInOrderDtlMapper.deleteByMainId(id.toString());
            matlInOrderMapper.deleteById(id);
        }
    }*/


    /*@Transactional
    public void saveMain(MatlInOrder matlInOrder, List<MatlInOrderDtl> matlInOrderDtlList) {
        matlInOrderMapper.insert(matlInOrder);
        if (matlInOrderDtlList != null && matlInOrderDtlList.size() > 0) {
            for (MatlInOrderDtl entity : matlInOrderDtlList) {
                //外键设置
                entity.setTsmMatlInOrderId(matlInOrder.getId());
                matlInOrderDtlMapper.insert(entity);
            }
        }
    }*/

    public void updateMain(MatlInOrder matlInOrder, List<MatlInOrderDtl> matlInOrderDtlList) {
        List<String> receiveIds = new ArrayList<>();
        for (MatlInOrderDtl matlInOrderDtl : matlInOrderDtlList) {
            matlInOrderDtl.setTsmMatlInOrderId(matlInOrder.getId());
            receiveIds.add(matlInOrderDtl.getId());
        }
        matlInOrderDtlService.saveOrUpdateBatch(matlInOrderDtlList);
        QueryWrapper<MatlInOrderDtl> wrapper = new QueryWrapper<>();
        wrapper.eq("tsm_matl_in_order_id", matlInOrder.getId());
        List<MatlInOrderDtl> list = matlInOrderDtlService.list(wrapper);
        saveOrUpdate(matlInOrder);
        //如果相等, 则说明没有要删除的记录
        if (list.size() == matlInOrderDtlList.size()) {
            return;
        } else {
            //removeIds: 要删除的记录ids
            List<String> removeIds = new ArrayList<>();
            for (MatlInOrderDtl matlInOrderDtl : list) {
                removeIds.add(matlInOrderDtl.getId());
            }
            removeIds.removeAll(receiveIds);
            matlInOrderDtlMapper.deleteBatchIds(removeIds);
        }
    }

    @Transactional
    public String delMain(String id) {
        matlInOrderDtlMapper.deleteByMainId(id);
        int i = matlInOrderMapper.deleteById(id);
        if (i == 0) {
            return "请选择未审核的记录删除";
        }
        return "已删除" + i + "条记录";
    }

    @Transactional
    public int delBatchMain(Collection<? extends Serializable> idList) {
        int i = matlInOrderMapper.deleteBatchIds(idList);
        QueryWrapper<MatlInOrderDtl> wrapper = new QueryWrapper<>();
        wrapper.in("tsm_matl_in_order_id", idList);
        matlInOrderDtlMapper.delete(wrapper);
        return i;
    }

    @Override
    public void redFlushById(String id) {

        MatlInOrder order = matlInOrderMapper.selectById(id);
        if (Status.UNCHECK.getCode().equals(order.getStatus())) {
            throw new ErpException("未审核单据，冲红失败!");
        }
        List<MatlInOrderDtl> dtlList = matlInOrderDtlMapper.selectByMainId(id, "1");
        if (!this.checkRedFlush(dtlList)) {
            throw new ErpException("存在冲红行项目，不能重复冲红，冲红失败!");
        }
        if (order.getBillProp() == BillProp.BLUE.getValue()) {
            MatlInOrder redOrder = new MatlInOrder();
            BeanUtil.copyProperties(order, redOrder);
            redOrder.setOriginalId(redOrder.getId());
            redOrder.setId(StringUtil.EMPTY_STRING);
            redOrder.setBillProp(BillProp.RED.getValue());
            redOrder.setStatus(Status.UNCHECK.getCode());
            redOrder.setCreateBy(StringUtil.EMPTY_STRING);
            redOrder.setCreateTime(null);
            redOrder.setUpdateBy(StringUtil.EMPTY_STRING);
            redOrder.setUpdateTime(null);
            matlInOrderMapper.insert(redOrder);
            for (MatlInOrderDtl dtl : dtlList) {
                MatlInOrderDtl redDtl = new MatlInOrderDtl();
                BeanUtil.copyProperties(dtl, redDtl);
                redDtl.setMatlQty(Double.valueOf(NumberUtil.mul(redDtl.getMatlQty().intValue(), BillProp.RED.getValue().intValue())).intValue());
                redDtl.setId(StringUtil.EMPTY_STRING);
                redDtl.setTsmMatlInOrderId(redOrder.getId());
                redDtl.setCreateBy(StringUtil.EMPTY_STRING);
                redDtl.setCreateTime(null);
                redDtl.setUpdateBy(StringUtil.EMPTY_STRING);
                redDtl.setUpdateTime(null);
                redDtl.setOriginalId(dtl.getId());
                matlInOrderDtlMapper.insert(redDtl);
            }
        } else {
            throw new ErpException("红单不能冲红，冲红失败!");
        }
    }

    @Override
    public void redFlushByIds(List<MatlInOrderDtl> listDtl) {
        List<MatlInOrderDtl> dtlList = matlInOrderDtlMapper.selectBatchIds(listDtl.stream().map(MatlInOrderDtl::getId).collect(Collectors.toList()));
        List<String> orderIdList = dtlList.stream().map(MatlInOrderDtl::getTsmMatlInOrderId).distinct().collect(Collectors.toList());
        if (orderIdList.size() == 1) {
            MatlInOrder order = matlInOrderMapper.selectById(orderIdList.get(0));
            if (Status.UNCHECK.getCode().equals(order.getStatus())) {
                throw new ErpException("未审核单据，冲红失败!");
            }
            if (order.getBillProp() == BillProp.BLUE.getValue()) {
                MatlInOrder redOrder = new MatlInOrder();
                BeanUtil.copyProperties(order, redOrder);
                redOrder.setOriginalId(order.getId());
                redOrder.setId(StringUtil.EMPTY_STRING);
                redOrder.setBillProp(BillProp.RED.getValue());
                redOrder.setStatus(Status.UNCHECK.getCode());
                redOrder.setCreateBy(StringUtil.EMPTY_STRING);
                redOrder.setCreateTime(null);
                redOrder.setUpdateBy(StringUtil.EMPTY_STRING);
                redOrder.setUpdateTime(null);
                matlInOrderMapper.insert(redOrder);
                for (MatlInOrderDtl dtl : dtlList) {
                    MatlInOrderDtl redDtl = new MatlInOrderDtl();
                    BeanUtil.copyProperties(dtl, redDtl);
                    redDtl.setMatlQty(Double.valueOf(NumberUtil.mul(redDtl.getMatlQty().intValue(), BillProp.RED.getValue().intValue())).intValue());
                    redDtl.setId(StringUtil.EMPTY_STRING);
                    redDtl.setTsmMatlInOrderId(redOrder.getId());
                    redDtl.setCreateBy(StringUtil.EMPTY_STRING);
                    redDtl.setCreateTime(null);
                    redDtl.setUpdateBy(StringUtil.EMPTY_STRING);
                    redDtl.setUpdateTime(null);
                    redDtl.setOriginalId(dtl.getId());
                    matlInOrderDtlMapper.insert(redDtl);
                }
            } else {
                throw new ErpException("红单不能冲红，冲红失败!");
            }
        } else {
            throw new ErpException("单据不存在，冲红失败!");
        }
    }

    private Boolean checkRedFlush(List<MatlInOrderDtl> dtlList) {
        List<String> ids = dtlList.stream().map(MatlInOrderDtl::getId).collect(Collectors.toList());
        Boolean result = Boolean.FALSE;
        result = (matlInOrderDtlMapper.selectByOriginalIds(ids).size() == 0);
        return result;
    }


    /**
     * 审核操作, 对收货单主表进行审核并添加明细流水
     *
     * @param matlInOrders
     */
    @Transactional
    public String checks(ArrayList<MatlInOrder> matlInOrders) {
        //存放主表的id和过账日期
        Map<String, Date> idsDate = new HashMap<>();
        //需要重新结存的日期
        List<Date> dates = new ArrayList<>();
        //是否需要重新进行日结存和总结存
        Boolean rerun = false;
        //需要更新的实体类s
        ArrayList<MatlInOrder> matlUpdates = new ArrayList<>();
        Date date = new Date();
        for (MatlInOrder matlInOrder : matlInOrders) {
            if (matlInOrder.getStatus().equals(Status.UNCHECK.getCode())) {
                matlInOrder.setStatus(Status.CHECK.getCode());
                matlInOrder.setVoucherTime(date);
                idsDate.put(matlInOrder.getId(), matlInOrder.getPostingDate());
                rerun = !DateUtil.isSameDay(matlInOrder.getPostingDate(), matlInOrder.getVoucherTime());
                matlUpdates.add(matlInOrder);
                if (rerun) {
                    dates.add(matlInOrder.getPostingDate());
                }
            }
        }
        List<WasteBook> wasteBooks = wasteBookService.matIODToWasteBook(idsDate);
        if (CollectionUtil.isEmpty(wasteBooks)) {
            return "没有收货明细";
        }
        updateBatchById(matlUpdates);
        wasteBookService.saveBatch(wasteBooks);
        //重新执行日结存和总结村的统计
        if (dates.size() != 0) {
            matlBalanceService.delRecords(dates);
            matlBalanceService.reAddRecords(dates);
        }
        return "审核成功!";
    }


}
