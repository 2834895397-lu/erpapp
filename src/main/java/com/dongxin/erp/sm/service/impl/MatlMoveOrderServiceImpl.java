package com.dongxin.erp.sm.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dongxin.erp.enums.BillProp;
import com.dongxin.erp.enums.Status;
import com.dongxin.erp.exception.ErpException;
import com.dongxin.erp.sm.entity.*;
import com.dongxin.erp.sm.mapper.MatlMoveOrderDtlMapper;
import com.dongxin.erp.sm.mapper.MatlMoveOrderMapper;
import com.dongxin.erp.sm.service.IMatlMoveOrderService;
import com.dongxin.erp.sm.service.MatlBalanceService;
import com.dongxin.erp.sm.service.MatlStockService;
import com.dongxin.erp.sm.service.WasteBookService;
import io.netty.util.internal.StringUtil;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 移库单主表
 * @Author: jeecg-boot
 * @Date: 2020-11-10
 * @Version: V1.0
 */
@Service
public class MatlMoveOrderServiceImpl extends ServiceImpl<MatlMoveOrderMapper, MatlMoveOrder> implements IMatlMoveOrderService {

    @Autowired
    private MatlMoveOrderMapper matlMoveOrderMapper;
    @Autowired
    private MatlMoveOrderDtlMapper matlMoveOrderDtlMapper;
    @Autowired
    private WasteBookService wasteBookService;

    @Autowired
    MatlMoveOrderDtlServiceImpl matlMoveOrderDtlService;
    @Autowired
    MatlBalanceService matlBalanceService;
    @Autowired
    MatlStockService matlStockService;

    @Override
    @Transactional
    public void saveMain(MatlMoveOrder matlMoveOrder, List<MatlMoveOrderDtl> matlMoveOrderDtlList) {
        matlMoveOrder.setBillProp(BillProp.BLUE.getValue());
        matlMoveOrderMapper.insert(matlMoveOrder);
        if (matlMoveOrderDtlList != null && matlMoveOrderDtlList.size() > 0) {
            for (MatlMoveOrderDtl entity : matlMoveOrderDtlList) {
                //外键设置
                entity.setTsmMatlMoveOrderId(matlMoveOrder.getId());
                matlMoveOrderDtlMapper.insert(entity);
            }
        }
    }

    @Override
    @Transactional
    public void updateMain(MatlMoveOrder matlMoveOrder, List<MatlMoveOrderDtl> matlMoveOrderDtlList) {
        List<String> receiveIds = new ArrayList<>();
        for (MatlMoveOrderDtl matlMoveOrderDtl : matlMoveOrderDtlList) {
            matlMoveOrderDtl.setTsmMatlMoveOrderId(matlMoveOrder.getId());
            receiveIds.add(matlMoveOrderDtl.getId());
        }
        matlMoveOrderDtlService.saveOrUpdateBatch(matlMoveOrderDtlList);
        QueryWrapper<MatlMoveOrderDtl> wrapper = new QueryWrapper<>();
        wrapper.eq("tsm_matl_move_order_id", matlMoveOrder.getId());
        List<MatlMoveOrderDtl> list = matlMoveOrderDtlService.list(wrapper);
        updateById(matlMoveOrder);
        //如果相等, 则说明没有要删除的记录
        if (list.size() == matlMoveOrderDtlList.size()) {
            return;
        } else {
            //removeIds: 要删除的记录ids
            List<String> removeIds = new ArrayList<>();
            for (MatlMoveOrderDtl matlMoveOrderDtl : list) {
                removeIds.add(matlMoveOrderDtl.getId());
            }
            removeIds.removeAll(receiveIds);
            matlMoveOrderDtlService.removeByIds(removeIds);
        }

    /*    matlMoveOrderMapper.updateById(matlMoveOrder);

        //1.先删除子表数据
        matlMoveOrderDtlMapper.deleteByMainId(matlMoveOrder.getId());

        //2.子表数据重新插入
        if (matlMoveOrderDtlList != null && matlMoveOrderDtlList.size() > 0) {
            for (MatlMoveOrderDtl entity : matlMoveOrderDtlList) {
                //外键设置
                entity.setTsmMatlMoveOrderId(matlMoveOrder.getId());
                matlMoveOrderDtlMapper.insert(entity);
            }
        }*/
    }

    @Override
    @Transactional
    public void delMain(String id) {
        matlMoveOrderDtlMapper.deleteByMainId(id);
        matlMoveOrderMapper.deleteById(id);
    }

    @Override
    @Transactional
    public void delBatchMain(Collection<? extends Serializable> idList) {
        for (Serializable id : idList) {
            matlMoveOrderDtlMapper.deleteByMainId(id.toString());
            matlMoveOrderMapper.deleteById(id);
        }
    }


    @Override
    @Transactional
    public String checks(ArrayList<MatlMoveOrder> matlMoveOrders) {
        //主表id和过账日期
        Map<String, Date> idsDate = new HashMap<>();
        //需要重新结存的日期
        List<Date> dates = new ArrayList<>();
        //是否需要重新进行日结存和总结存
        Boolean rerun = false;
        //需要更新的实体类s
        ArrayList<MatlMoveOrder> matlUpdates = new ArrayList<>();
        Date date = new Date();
        for (MatlMoveOrder matlMoveOrder : matlMoveOrders) {
            if (matlMoveOrder.getStatus().equals(Status.UNCHECK.getCode())) {
                idsDate.put(matlMoveOrder.getId(), matlMoveOrder.getPostingDate());
                matlMoveOrder.setStatus(Status.CHECK.getCode());
                matlMoveOrder.setVoucherTime(date);
                rerun = !DateUtil.isSameDay(matlMoveOrder.getPostingDate(), matlMoveOrder.getVoucherTime());
                matlUpdates.add(matlMoveOrder);
                //要重新执行结存的日期
                if (rerun) {
                    dates.add(matlMoveOrder.getPostingDate());
                }
            }
        }
        List<?> objects = wasteBookService.matMODToWasteBookOut(idsDate);
        if (CollectionUtil.isEmpty(objects)) {
            return "没有移库明细";
        }
        if (objects.get(0) instanceof WasteBook ? true : false) {
            List<WasteBook> wasteBooksIn = wasteBookService.matMODToWasteBookIn(idsDate);
            List<WasteBook> wasteBooksOut = (List<WasteBook>) objects;
            wasteBookService.saveBatch(wasteBooksIn);
            wasteBookService.saveBatch(wasteBooksOut);
            updateBatchById(matlUpdates);
            //重新执行日结存和总结村的统计
            if (rerun) {
                matlBalanceService.delRecords(dates);
                matlBalanceService.reAddRecords(dates);
            }
            return "审核成功!";
        } else {
            List<String> msg = (List<String>) objects;
            return msg.get(0);
        }

    }

    @Transactional
    public int myDelBatchMain(Collection<? extends Serializable> idList) {
        QueryWrapper<MatlMoveOrderDtl> wrapper = new QueryWrapper<>();
        wrapper.in("tsm_matl_move_order_id", idList);
        matlMoveOrderDtlMapper.delete(wrapper);
        int totalDel = matlMoveOrderMapper.deleteBatchIds(idList);
        return totalDel;
    }



    @Transactional
    public void redFlushByIds(List<MatlMoveOrderDtl> listDtl) throws ErpException {

        List<MatlMoveOrderDtl> dtlList = matlMoveOrderDtlMapper.selectBatchIds(listDtl.stream().map(MatlMoveOrderDtl::getId).collect(Collectors.toList()));
        List<String> orderIdList = dtlList.stream().map(MatlMoveOrderDtl::getTsmMatlMoveOrderId).distinct().collect(Collectors.toList());
        if (orderIdList.size() == 1) {
            MatlMoveOrder order = matlMoveOrderMapper.selectById(orderIdList.get(0));
            if (Status.UNCHECK.getCode().equals(order.getStatus())) {
                throw new ErpException("未审核单据，冲红失败!");
            }
            if(!this.checkRedFlush(dtlList)){
                throw new ErpException("存在冲红行项目，不能重复冲红，冲红失败!");
            }
            if (order.getBillProp() == BillProp.BLUE.getValue()) {
                MatlMoveOrder redOrder = new MatlMoveOrder();
                BeanUtil.copyProperties(order, redOrder);
                redOrder.setOriginalId(order.getId());
                redOrder.setId(StringUtil.EMPTY_STRING);
                redOrder.setBillProp(BillProp.RED.getValue());
                redOrder.setStatus(Status.UNCHECK.getCode());
                redOrder.setCreateBy(StringUtil.EMPTY_STRING);
                redOrder.setCreateTime(null);
                redOrder.setUpdateBy(StringUtil.EMPTY_STRING);
                redOrder.setUpdateTime(null);
                matlMoveOrderMapper.insert(redOrder);

                for (MatlMoveOrderDtl dtl : dtlList) {
                    MatlMoveOrderDtl redDtl = new MatlMoveOrderDtl();
                    BeanUtil.copyProperties(dtl, redDtl);
                    redDtl.setMatlQty(Double.valueOf(NumberUtil.mul(redDtl.getMatlQty().intValue(), BillProp.RED.getValue().intValue())).intValue());
                    redDtl.setId(StringUtil.EMPTY_STRING);
                    redDtl.setCreateBy(StringUtil.EMPTY_STRING);
                    redDtl.setCreateTime(null);
                    redDtl.setUpdateBy(StringUtil.EMPTY_STRING);
                    redDtl.setUpdateTime(null);
                    redDtl.setOriginalId(dtl.getId());
                    redDtl.setTsmMatlMoveOrderId(redOrder.getId());
                    matlMoveOrderDtlMapper.insert(redDtl);
                }

            } else {
                throw new ErpException("红单不能冲红，冲红失败!");
            }
        } else {
            throw new ErpException("单据不存在，冲红失败!");
        }
    }

    @Transactional
    public void redFlushById(String id) {
        MatlMoveOrder order = matlMoveOrderMapper.selectById(id);
        if (Status.UNCHECK.getCode().equals(order.getStatus())) {
            throw new ErpException("未审核单据，冲红失败!");
        }
        List<MatlMoveOrderDtl> dtlList = matlMoveOrderDtlMapper.selectByMainId(id , "1");
        if(!this.checkRedFlush(dtlList)){
            throw new ErpException("存在冲红行项目，不能重复冲红，冲红失败!");
        }
        if (order.getBillProp() == BillProp.BLUE.getValue()) {
            MatlMoveOrder redOrder = new MatlMoveOrder();
            BeanUtil.copyProperties(order, redOrder);
            redOrder.setOriginalId(redOrder.getId());
            redOrder.setId(StringUtil.EMPTY_STRING);
            redOrder.setBillProp(BillProp.RED.getValue());
            redOrder.setStatus(Status.UNCHECK.getCode());
            redOrder.setCreateBy(StringUtil.EMPTY_STRING);
            redOrder.setCreateTime(null);
            redOrder.setUpdateBy(StringUtil.EMPTY_STRING);
            redOrder.setUpdateTime(null);
            matlMoveOrderMapper.insert(redOrder);
            for (MatlMoveOrderDtl dtl : dtlList) {
                MatlMoveOrderDtl redDtl = new MatlMoveOrderDtl();
                BeanUtil.copyProperties(dtl, redDtl);
                redDtl.setMatlQty(Double.valueOf(NumberUtil.mul(redDtl.getMatlQty().intValue(), BillProp.RED.getValue().intValue())).intValue());
                redDtl.setId(StringUtil.EMPTY_STRING);
                redDtl.setCreateBy(StringUtil.EMPTY_STRING);
                redDtl.setCreateTime(null);
                redDtl.setUpdateBy(StringUtil.EMPTY_STRING);
                redDtl.setUpdateTime(null);
                redDtl.setOriginalId(dtl.getId());
                redDtl.setTsmMatlMoveOrderId(redOrder.getId());
                matlMoveOrderDtlMapper.insert(redDtl);
            }
        } else {
            throw new ErpException("红单不能冲红，冲红失败!");
        }

    }

    private Boolean checkRedFlush(List<MatlMoveOrderDtl> dtlList){
        List<String> ids = dtlList.stream().map(MatlMoveOrderDtl::getId).collect(Collectors.toList());
        Boolean result = Boolean.FALSE;
        result = (matlMoveOrderDtlMapper.selectByOriginalIds(ids).size() == 0 );
        return result;
    }


}
