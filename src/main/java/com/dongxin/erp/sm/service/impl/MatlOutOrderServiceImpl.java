package com.dongxin.erp.sm.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dongxin.erp.enums.BillProp;
import com.dongxin.erp.enums.Status;
import com.dongxin.erp.exception.ErpException;
import com.dongxin.erp.sm.entity.*;
import com.dongxin.erp.sm.mapper.MatlOutOrderDtlMapper;
import com.dongxin.erp.sm.mapper.MatlOutOrderMapper;
import com.dongxin.erp.sm.service.IMatlOutOrderService;
import com.dongxin.erp.sm.service.MatlBalanceService;
import com.dongxin.erp.sm.service.MatlStockService;
import com.dongxin.erp.sm.service.WasteBookService;
import io.netty.util.internal.StringUtil;
import org.jeecg.common.system.query.QueryGenerator;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 领用单主表
 * @Author: jeecg-boot
 * @Date: 2020-11-10
 * @Version: V1.0
 */
@Service
public class MatlOutOrderServiceImpl extends ServiceImpl<MatlOutOrderMapper, MatlOutOrder> implements IMatlOutOrderService {

    @Autowired
    private MatlOutOrderMapper matlOutOrderMapper;
    @Autowired
    private MatlOutOrderDtlMapper matlOutOrderDtlMapper;
    @Autowired
    WasteBookService wasteBookService;

    @Autowired
    MatlOutOrderServiceImpl matlOutOrderService;

    @Autowired
    MatlOutOrderDtlServiceImpl matlOutOrderDtlService;
    @Autowired
    MatlStockService matlStockService;
    @Autowired
    MatlBalanceService matlBalanceService;

    @Override
    @Transactional
    public void saveMain(MatlOutOrder matlOutOrder, List<MatlOutOrderDtl> matlOutOrderDtlList) {
        matlOutOrder.setBillProp(BillProp.BLUE.getValue());
        matlOutOrderMapper.insert(matlOutOrder);
        if (matlOutOrderDtlList != null && matlOutOrderDtlList.size() > 0) {
            for (MatlOutOrderDtl entity : matlOutOrderDtlList) {
                //外键设置
                entity.setTsmMatlOutOrderId(matlOutOrder.getId());
                matlOutOrderDtlMapper.insert(entity);
            }
        }
    }

    @Override
    public void updateMain(MatlOutOrder matlOutOrder, List<MatlOutOrderDtl> matlOutOrderDtlList) {

        List<String> receiveIds = new ArrayList<>();
        for (MatlOutOrderDtl matlOutOrderDtl : matlOutOrderDtlList) {
            matlOutOrderDtl.setTsmMatlOutOrderId(matlOutOrder.getId());
            receiveIds.add(matlOutOrderDtl.getId());
        }
        matlOutOrderDtlService.saveOrUpdateBatch(matlOutOrderDtlList);
        QueryWrapper<MatlOutOrderDtl> wrapper = new QueryWrapper<>();
        wrapper.eq("tsm_matl_out_order_id", matlOutOrder.getId());
        List<MatlOutOrderDtl> list = matlOutOrderDtlService.list(wrapper);
        updateById(matlOutOrder);
        //如果相等, 则说明没有要删除的记录
        if (list.size() == matlOutOrderDtlList.size()) {
            return;
        } else {
            //removeIds: 要删除的记录ids
            List<String> removeIds = new ArrayList<>();
            for (MatlOutOrderDtl matlOutOrderDtl : list) {
                removeIds.add(matlOutOrderDtl.getId());
            }
            removeIds.removeAll(receiveIds);
            matlOutOrderDtlMapper.deleteBatchIds(removeIds);
        }

		/*matlOutOrderMapper.updateById(matlOutOrder);
		
		//1.先删除子表数据
		matlOutOrderDtlMapper.deleteByMainId(matlOutOrder.getId());
		
		//2.子表数据重新插入
		if(matlOutOrderDtlList!=null && matlOutOrderDtlList.size()>0) {
			for(MatlOutOrderDtl entity:matlOutOrderDtlList) {
				//外键设置
				entity.setTsmMatlOutOrderId(matlOutOrder.getId());
				matlOutOrderDtlMapper.insert(entity);
			}
		}*/
    }

    @Override
    @Transactional
    public void delMain(String id) {
        matlOutOrderDtlMapper.deleteByMainId(id);
        matlOutOrderMapper.deleteById(id);
    }

    @Override
    @Transactional
    public int delBatchMain(Collection<? extends Serializable> idList) {

        int totalDel = matlOutOrderMapper.deleteBatchIds(idList);

        QueryWrapper<MatlOutOrderDtl> wrapper = new QueryWrapper<>();
        wrapper.in("tsm_matl_out_order_id", idList);
        matlOutOrderDtlMapper.delete(wrapper);
        return totalDel;
    }

    /**
     * 审核操作, 对领用单表进行审核并添加明细流水
     *
     * @param matlOutOrders
     */
    @Transactional
    public String checks(ArrayList<MatlOutOrder> matlOutOrders) {
        //存放主表的id和过账日期
        Map<String, Date> idsDate = new HashMap<>();
        //是否需要重新进行日结存和总结存
        Boolean rerun = false;
        //需要重新结存的日期
        List<Date> dates = new ArrayList<>();
        //需要更新的实体类s
        ArrayList<MatlOutOrder> matlUpdates = new ArrayList<>();
        Date date = new Date();
        for (MatlOutOrder matlOutOrder : matlOutOrders) {
            if (matlOutOrder.getStatus().equals(Status.UNCHECK.getCode())) {
                matlOutOrder.setStatus(Status.CHECK.getCode());
                matlOutOrder.setVoucherTime(date);
                idsDate.put(matlOutOrder.getId(), matlOutOrder.getPostingDate());
                rerun = !DateUtil.isSameDay(matlOutOrder.getPostingDate(), matlOutOrder.getVoucherTime());
                matlUpdates.add(matlOutOrder);
                //要重新执行结存的日期
                if (rerun) {
                    dates.add(matlOutOrder.getPostingDate());
                }
            }
        }
        List<?> objects = wasteBookService.matOODToWasteBook(idsDate);
        if(CollectionUtil.isEmpty(objects)){
            return "没有出库明细";
        }
        if (objects.get(0) instanceof WasteBook ? true : false) {
            updateBatchById(matlUpdates);
            List<WasteBook> wasteBooks = (List<WasteBook>) objects;
            wasteBookService.saveBatch(wasteBooks);
            //重新执行日结存和总结存的统计
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



    @Override
    @Transactional
    public void redFlushByIds(List<MatlOutOrderDtl> listDtl) throws ErpException{
        List<MatlOutOrderDtl> dtlList = matlOutOrderDtlMapper.selectBatchIds(listDtl.stream().map(MatlOutOrderDtl::getId).collect(Collectors.toList()));
        List<String> orderIdList = dtlList.stream().map(MatlOutOrderDtl::getTsmMatlOutOrderId).distinct().collect(Collectors.toList());
        if(orderIdList.size()==1){
            MatlOutOrder order =  matlOutOrderMapper.selectById(orderIdList.get(0));
            if(Status.UNCHECK.getCode().equals(order.getStatus())){
                throw new ErpException("未审核单据，冲红失败!");
            }
            if(order.getBillProp() == BillProp.BLUE.getValue()){
                MatlOutOrder redOrder = new MatlOutOrder();
                BeanUtil.copyProperties(order , redOrder);
                redOrder.setOriginalId(order.getId());
                redOrder.setId(StringUtil.EMPTY_STRING);
                redOrder.setBillProp(BillProp.RED.getValue());
                redOrder.setStatus(Status.UNCHECK.getCode());
                redOrder.setCreateBy(StringUtil.EMPTY_STRING);
                redOrder.setCreateTime(null);
                redOrder.setUpdateBy(StringUtil.EMPTY_STRING);
                redOrder.setUpdateTime(null);
                matlOutOrderMapper.insert(redOrder);
                for(MatlOutOrderDtl dtl : dtlList){
                    MatlOutOrderDtl redDtl = new MatlOutOrderDtl();
                    BeanUtil.copyProperties(dtl , redDtl);
                    redDtl.setMatlQty(Double.valueOf(NumberUtil.mul(redDtl.getMatlQty().intValue() , BillProp.RED.getValue().intValue())).intValue());
                    redDtl.setId(StringUtil.EMPTY_STRING);
                    redDtl.setTsmMatlOutOrderId(redOrder.getId());
                    redDtl.setCreateBy(StringUtil.EMPTY_STRING);
                    redDtl.setCreateTime(null);
                    redDtl.setUpdateBy(StringUtil.EMPTY_STRING);
                    redDtl.setUpdateTime(null);
                    redDtl.setOriginalId(dtl.getId());
                    matlOutOrderDtlMapper.insert(redDtl);
                }
            }else{
                throw new ErpException("红单不能冲红，冲红失败!");
            }
        }else{
            throw new ErpException("单据不存在，冲红失败!");
        }
    }

    @Override
    @Transactional
    public void redFlushById(String id){
        MatlOutOrder order =  matlOutOrderMapper.selectById(id);
        if(Status.UNCHECK.getCode().equals(order.getStatus())){
            throw new ErpException("未审核单据，冲红失败!");
        }
        List<MatlOutOrderDtl> dtlList = matlOutOrderDtlMapper.selectByMainId(id ,"1");
        if(!this.checkRedFlush(dtlList)){
            throw new ErpException("存在冲红行项目，不能重复冲红，冲红失败!");
        }
        if(order.getBillProp() == BillProp.BLUE.getValue()){
            MatlOutOrder redOrder = new MatlOutOrder();
            BeanUtil.copyProperties(order , redOrder);
            redOrder.setOriginalId(redOrder.getId());
            redOrder.setId(StringUtil.EMPTY_STRING);
            redOrder.setBillProp(BillProp.RED.getValue());
            redOrder.setStatus(Status.UNCHECK.getCode());
            redOrder.setCreateBy(StringUtil.EMPTY_STRING);
            redOrder.setCreateTime(null);
            redOrder.setUpdateBy(StringUtil.EMPTY_STRING);
            redOrder.setUpdateTime(null);
            matlOutOrderMapper.insert(redOrder);
            for(MatlOutOrderDtl dtl : dtlList){
                MatlOutOrderDtl redDtl = new MatlOutOrderDtl();
                BeanUtil.copyProperties(dtl , redDtl);
                redDtl.setMatlQty(Double.valueOf(NumberUtil.mul(redDtl.getMatlQty().intValue() , BillProp.RED.getValue().intValue())).intValue());
                redDtl.setId(StringUtil.EMPTY_STRING);
                redDtl.setTsmMatlOutOrderId(redOrder.getId());
                redDtl.setCreateBy(StringUtil.EMPTY_STRING);
                redDtl.setCreateTime(null);
                redDtl.setUpdateBy(StringUtil.EMPTY_STRING);
                redDtl.setUpdateTime(null);
                redDtl.setOriginalId(dtl.getId());
                matlOutOrderDtlMapper.insert(redDtl);
            }
        }else{
            throw new ErpException("红单不能冲红，冲红失败!");
        }

    }

    private Boolean checkRedFlush(List<MatlOutOrderDtl> dtlList){
        List<String> ids = dtlList.stream().map(MatlOutOrderDtl::getId).collect(Collectors.toList());
        Boolean result = Boolean.FALSE;
        result = (matlOutOrderDtlMapper.selectByOriginalIds(ids).size() == 0 );
        return result;
    }


}
