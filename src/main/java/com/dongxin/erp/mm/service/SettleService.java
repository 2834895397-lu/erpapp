package com.dongxin.erp.mm.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.dongxin.erp.enums.Status;
import com.dongxin.erp.exception.ErpException;
import com.dongxin.erp.mm.entity.Settle;
import com.dongxin.erp.mm.entity.SettleDtl;
import com.dongxin.erp.mm.mapper.SettleDtlMapper;
import com.dongxin.erp.mm.mapper.SettleMapper;
import com.dongxin.erp.mm.vo.SettlePage;
import com.dongxin.erp.sm.entity.MatlInOrder;
import com.dongxin.erp.sm.entity.MatlInOrderDtl;

import com.dongxin.erp.sm.service.impl.MatlInOrderDtlServiceImpl;
import com.dongxin.erp.sm.service.impl.MatlInOrderServiceImpl;
import org.jeecg.common.system.base.service.BaseService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 采购结算主表
 * @Author: jeecg-boot
 * @Date: 2020-11-23
 * @Version: V1.0
 */
@Service
public class SettleService extends BaseService<SettleMapper, Settle> {

    @Autowired
    private SettleMapper settleMapper;
    @Autowired
    private SettleDtlMapper settleDtlMapper;
    @Autowired
    private MatlInOrderDtlServiceImpl matlInOrderDtlService;
    @Autowired
    private MatlInOrderServiceImpl matlInOrderService;

    @Autowired
    private SettleDtlService settleDtlService;

    @Transactional
    public void saveMain(Settle settle, List<SettleDtl> settleDtlList) {
        settleMapper.insert(settle);
        if (null != settleDtlList && settleDtlList.size() > 0) {
            for (SettleDtl entity : settleDtlList) {
                // 外键设置
                entity.setTmmSettleId(settle.getId());
                settleDtlMapper.insert(entity);
            }
        }
    }

    /**
     * 删除采购结算记录,只有待审核状态才可删除
     *
     * @param id
     * @author huangheng 2020-01-05
     */
    @Transactional
    public void delMain(String id) {

        Settle settleEntity = this.getById(id);
        if (null == settleEntity) {
            throw new ErpException("未找到对应数据");
        }
        if (Status.CHECK.getCode().equals(settleEntity.getStatus())) {
            throw new ErpException("已审核的记录不可删除");
        }

        QueryWrapper<SettleDtl> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("tmm_settle_id", settleEntity.getId());
        List<SettleDtl> settleDtls = settleDtlService.list(queryWrapper);
        for (SettleDtl settleDtl : settleDtls) {
            settleDtlService.deleteBySettleDtl(settleDtl);
            //清空关联id
            matlInOrderDtlService.updateTmmSettleDtlIdById(settleDtl.getTsmMatlInOrderDtlId());
        }

        // 删除采购结算单记录
        logicDeleteById(id);
    }

    /**
     * 批量删除采购结算记录
     *
     * @param idList
     */
    @Transactional
    public void delBatchMain(Collection<? extends Serializable> idList) {
        List<Settle> settles = this.listByIds(idList);
        List<Settle> settleCheckedList = settles.stream().filter(e -> {
            return Status.UNCHECK.getCode().equals(e.getStatus());
        }).collect(Collectors.toList());

        for (Settle settleEntity : settleCheckedList) {
            LambdaQueryWrapper<SettleDtl> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SettleDtl::getTmmSettleId, settleEntity.getId());
            List<SettleDtl> settleDtls = settleDtlService.list(queryWrapper);
            for (SettleDtl settleDtl : settleDtls) {
                settleDtlService.deleteBySettleDtl(settleDtl);

                //清空关联id
                matlInOrderDtlService.updateTmmSettleDtlIdById(settleDtl.getTsmMatlInOrderDtlId());
            }

            // 删除采购结算单记录
            logicDeleteById(settleEntity.getId());
        }
    }

    /**
     * 采购结算审核
     *
     * @param settleList
     * @return
     */
    @Transactional
    public void check(@RequestBody List<Settle> settleList) {

        // 对收货单明细表的结算标识进行更新
        matlInOrderDtlService.changeSettleFlag(settleList);

        // 审核后，修改审核状态，修改凭证时间
        List<Settle> settleUpdates = new ArrayList<Settle>();
        Date date = new Date();
        for (Settle settle : settleList) {
            if (settle.getStatus().equals(Status.UNCHECK.getCode())) {
                settle.setStatus(Status.CHECK.getCode());// 更新审核状态
                settle.setVoucherTime(date);// 更新审核时间
                settleUpdates.add(settle);
            }
        }
        // 批量更新
        updateBatchById(settleUpdates);
    }

    public void setMaxAndMinTime(List<SettleDtl> settleDtlList, Settle settle) {
        // 根据采购明细的tsm_matl_in_order_dtl_id获取收货明细表ID
        List<String> tsmMatlInOrderDtlIdList = new ArrayList<>();// 存着收货明细表的ID
        for (SettleDtl settleDtl : settleDtlList) {
            tsmMatlInOrderDtlIdList.add(settleDtl.getTsmMatlInOrderDtlId());
        }
        // 根据收货明细表的ID去收货明细表里查出收货主表的ID
        QueryWrapper<MatlInOrderDtl> matlInOrderDtlQueryWrapper = new QueryWrapper<>();
        matlInOrderDtlQueryWrapper.in("id", tsmMatlInOrderDtlIdList);
        List<MatlInOrderDtl> matlInOrderDtlList = matlInOrderDtlService.list(matlInOrderDtlQueryWrapper);
        List<String> matlInOrderIdList = new ArrayList<>();
        for (MatlInOrderDtl matlInOrderDtl : matlInOrderDtlList) {
            matlInOrderIdList.add(matlInOrderDtl.getTsmMatlInOrderId());
        }
        // 根据收货单ID去查收货单里的voucher_time
        QueryWrapper<MatlInOrder> matlInOrderQueryWrapper = new QueryWrapper<>();
        matlInOrderQueryWrapper.in("id", matlInOrderIdList);
        List<MatlInOrder> matlInOrderList = matlInOrderService.list(matlInOrderQueryWrapper);
        List<Date> matlInOrderVoucherTimeList = new ArrayList<>();
        for (MatlInOrder matlInOrder : matlInOrderList) {
            matlInOrderVoucherTimeList.add(matlInOrder.getVoucherTime());
        }
        // 运用集合工具类取出最小最大值
        // 给结算表的起始日期赋最小值
        settle.setStartTime(Collections.min(matlInOrderVoucherTimeList));
        // 给结算表的终止日期赋最大值
        settle.setEndTime(Collections.max(matlInOrderVoucherTimeList));
        updateById(settle);
    }

    /**
     * 添加招标结算
     *
     * @param settlePage
     * @author huangheng 2021-01-04
     */
    @Transactional
    public void add(SettlePage settlePage) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");

        Settle settle = new Settle();
        BeanUtils.copyProperties(settlePage, settle);
        // 业务：主表金额通过附表单价数量计算获取
        Double totalPrice = 0.0;
        List<SettleDtl> settleDtlList = settlePage.getSettleDtlList();
        if (null == settleDtlList || 0 == settleDtlList.size()) {
            throw new ErpException("采购结算明细不可为空");
        }

        int repeatCount = settleDtlList.stream()
                .collect(Collectors.groupingBy(SettleDtl -> SettleDtl.getTsmMatlInOrderDtlId(), Collectors.counting()))
                .entrySet().stream().filter(entry -> entry.getValue() > 1).map(entry -> entry.getKey())
                .collect(Collectors.toList()).size();
        if (repeatCount > 0) {
            throw new ErpException("存在相同结算明细记录,请检查!");
        }

        List<Date> voucherTimeList = new ArrayList<>();

        // 查询入库单明细是否已被结算单绑定
        for (int i = 0; i < settleDtlList.size(); i++) {

            SettleDtl settleDtl = settleDtlList.get(i);

            MatlInOrderDtl matlInOrderDtl = matlInOrderDtlService.getById(settleDtl.getTsmMatlInOrderDtlId());
            if (null == matlInOrderDtl) {
                throw new ErpException("第" + (i + 1) + "笔未找到入库单明细记录,请检查!");
            }
            if (null != matlInOrderDtl.getTmmSettleDtlId()) {

                throw new ErpException("第" + (i + 1) + "笔入库单明细记录已绑定采购结算单,请检查!");
            }
            // 计算金额
            totalPrice += settleDtl.getMatlQty() * settleDtl.getMatlPrice();

            // 获取审核日期
            Date voucherTime = settleDtlService.getVoucherTime(settleDtl.getTsmMatlInOrderDtlId());
            if (null != voucherTime) {
                settleDtl.setVoucherTime(voucherTime);
                voucherTimeList.add(voucherTime);
            }

        }
        settle.setMatlAmount(totalPrice);

        // 记录汇总前入库单记录
        Map<Integer, List<String>> map = new HashMap<Integer, List<String>>();

        List<SettleDtl> settleDtls = new ArrayList<SettleDtl>();
        // 按物料+按入库日期合并
        for (int i = 0; i < settleDtlList.size(); i++) {
            SettleDtl settleDtl = settleDtlList.get(i);
            if (settleDtls.size() == 0) {
                settleDtls.add(settleDtl);

                List<String> list = new ArrayList<String>();
                list.add(settleDtl.getTsmMatlInOrderDtlId());
                map.put(1, list);

            } else {
                Boolean if_exist = false;
                for (int j = 0; j < settleDtls.size(); j++) {
                    SettleDtl settleEntity = settleDtls.get(j);

                    String dString1 = formatter.format(settleEntity.getVoucherTime());
                    String dString2 = formatter.format(settleDtl.getVoucherTime());

                    if (settleEntity.getTbdMaterialId().equals(settleDtl.getTbdMaterialId())
                            && dString1.equals(dString2)) {

                        settleEntity.setMatlQty(settleEntity.getMatlQty() + settleDtl.getMatlQty());

                        List<String> list = map.get(j + 1);
                        list.add(settleDtl.getTsmMatlInOrderDtlId());
                        map.put(j + 1, list);

                        if_exist = true;
                        break;
                    }
                }
                if (if_exist == false) {
                    settleDtls.add(settleDtl);

                    List<String> list = new ArrayList<String>();
                    list.add(settleDtl.getTsmMatlInOrderDtlId());
                    map.put(settleDtls.size(), list);

                }
            }
        }

        // 功能实现:1.根据收货明细表中的tsm_matl_in_order_id获取其中的voucher_time
        // 2.voucher_time存入list中，用集合工具类取出minTime,maxTime
        // 3.minTime赋值给startTime，maxTime赋值给endTime
        // this.setMaxAndMinTime(settleDtlList, settle);

        // 给结算表的起始日期赋最小值
        settle.setStartTime(Collections.min(voucherTimeList));
        // 给结算表的终止日期赋最大值
        settle.setEndTime(Collections.max(voucherTimeList));

        // this.saveMain(settle, settlePage.getSettleDtlList());

        settleMapper.insert(settle);

        if (null != settleDtls && settleDtls.size() > 0) {
            for (int j = 0; j < settleDtls.size(); j++) {

                SettleDtl entity = settleDtls.get(j);
                // 外键设置
                entity.setTmmSettleId(settle.getId());
                settleDtlMapper.insert(entity);

                List<String> list = map.get(j + 1);
                for (String dtlId : list) {
                    // 更新收货单明细表
                    MatlInOrderDtl matlInOrderDtl = new MatlInOrderDtl();
                    matlInOrderDtl.setId(dtlId);
                    matlInOrderDtl.setTmmSettleDtlId(entity.getId());
                    matlInOrderDtlService.updateById(matlInOrderDtl);
                }
            }
        }

    }

    /**
     * 采购结算-编辑
     *
     * @param settlePage
     */
    @Transactional
    public void edit(SettlePage settlePage) {
        Settle settle = new Settle();
        BeanUtils.copyProperties(settlePage, settle);

        Settle settleEntity = this.getById(settle.getId());
        if (settleEntity == null) {
            throw new ErpException("未找到对应数据");
        }
        if (Status.CHECK.getCode().equals(settleEntity.getStatus())) {
            throw new ErpException("已审核的记录不可编辑");
        }
        settleEntity.setRemark(settle.getRemark());
        settleMapper.updateById(settleEntity);
    }

    /**
     * 采购结算-编辑
     *
     * @param settlePage
     * @author huangheng 2021-01-04  有问题
     */
    @Transactional
    public void edit1(SettlePage settlePage) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");

        Settle settle = new Settle();
        BeanUtils.copyProperties(settlePage, settle);

        Settle settleEntity = this.getById(settle.getId());
        if (settleEntity == null) {
            throw new ErpException("未找到对应数据");
        }
        if (Status.CHECK.getCode().equals(settleEntity.getStatus())) {
            throw new ErpException("已审核的记录不可编辑");
        }
        Double totalPrice = 0.0;
        // 采购明细记录处理
        List<SettleDtl> settleDtlList = settlePage.getSettleDtlList();

        // 已经删除的采购结算记录
        List<SettleDtl> settleDtlNoExistList = new ArrayList<SettleDtl>();

        // 新增的采购结算记录
        List<SettleDtl> settleDtlNewList = new ArrayList<SettleDtl>();
        settleDtlNewList.addAll(settleDtlList);

        // 保留的老记录
        List<SettleDtl> settleDtlOldList = new ArrayList<SettleDtl>();
        settleDtlOldList.addAll(settleDtlList);

        // 获取原来结算明细
        QueryWrapper<SettleDtl> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("tmm_settle_id", settleEntity.getId());
        List<SettleDtl> settleDtls = settleDtlService.list(queryWrapper);
        for (int i = 0; i < settleDtls.size(); i++) {
            SettleDtl settleDtlEntity = settleDtls.get(i);
            Boolean if_exist = false;

            for (int j = 0; j < settleDtlList.size(); j++) {
                SettleDtl settleDtl = settleDtlList.get(j);
                if (settleDtl.getTsmMatlInOrderDtlId().equals(settleDtlEntity.getTsmMatlInOrderDtlId())) {
                    if_exist = true;
                    settleDtlNewList.remove(j);
                    continue;
                }
            }
            if (!if_exist) {
                settleDtlNoExistList.add(settleDtlEntity);
            }
        }

        List<Date> voucherTimeList = new ArrayList<>();
        for (SettleDtl settleDtl : settleDtlList) {
            // 计算金额
            totalPrice += settleDtl.getMatlQty() * settleDtl.getMatlPrice();

            // 获取审核日期
            Date voucherTime = settleDtlService.getVoucherTime(settleDtl.getTsmMatlInOrderDtlId());
            if (null != voucherTime) {
                settleDtl.setVoucherTime(voucherTime);
                voucherTimeList.add(voucherTime);
            }
        }

        // 给结算表的起始日期赋最小值
        settle.setStartTime(Collections.min(voucherTimeList));
        // 给结算表的终止日期赋最大值
        settle.setEndTime(Collections.max(voucherTimeList));

        settle.setMatlAmount(totalPrice);// 总金额
        settleMapper.updateById(settle);

        settleDtlOldList.removeAll(settleDtlNewList);

        List<Integer> removeIndex = new ArrayList<Integer>();
        Map<Integer, List<String>> map = new HashMap<Integer, List<String>>();
        // 新记录
        for (int i = 0; i < settleDtlNewList.size(); i++) {
            SettleDtl settleDtl = settleDtlList.get(i);

            MatlInOrderDtl matlInOrderDtl = matlInOrderDtlService.getById(settleDtl.getTsmMatlInOrderDtlId());
            if (null == matlInOrderDtl) {
                throw new ErpException("第" + (i + 1) + "笔未找到入库单明细记录,请检查!");
            }
            if (null != matlInOrderDtl.getTmmSettleDtlId()) {
                throw new ErpException("第" + (i + 1) + "笔入库单明细记录已绑定采购结算单,请检查!");
            }
            for (SettleDtl settleDtlEntity : settleDtlOldList) {

                Date voucherTime = settleDtlService.getVoucherTime(settleDtlEntity.getTsmMatlInOrderDtlId());

                String dString1 = formatter.format(voucherTime);
                String dString2 = formatter.format(settleDtl.getVoucherTime());
                if (settleDtlEntity.getTbdMaterialId().equals(settleDtl.getTbdMaterialId())
                        && dString1.equals(dString2)) {
                    settleDtlEntity.setMatlQty(settleDtlEntity.getMatlQty() + settleDtl.getMatlQty());
                    removeIndex.add(i);

                    List<String> list = map.get(i);
                    list.add(settleDtlEntity.getTsmMatlInOrderDtlId());
                    map.put(i, list);

                }
            }
        }

        // 新记录
        for (int i = 0; i < settleDtlNewList.size(); i++) {
            SettleDtl settleDtl = settleDtlList.get(i);

            MatlInOrderDtl matlInOrderDtl = matlInOrderDtlService.getById(settleDtl.getTsmMatlInOrderDtlId());
            if (null == matlInOrderDtl) {
                throw new ErpException("第" + (i + 1) + "笔未找到入库单明细记录,请检查!");
            }
            if (null != matlInOrderDtl.getTmmSettleDtlId()) {
                throw new ErpException("第" + (i + 1) + "笔入库单明细记录已绑定采购结算单,请检查!");
            }
            // 获取结算明细
            QueryWrapper<SettleDtl> queryParam = new QueryWrapper<>();
            queryParam.eq("tmm_settle_id", settleEntity.getId());
            List<SettleDtl> dtlList = settleDtlService.list(queryParam);
            if (dtlList.size() > 0) {
                for (SettleDtl settleDtlEntity : dtlList) {
                    Date voucherTime = settleDtlService.getVoucherTime(settleDtlEntity.getTsmMatlInOrderDtlId());

                    String dString1 = formatter.format(voucherTime);
                    String dString2 = formatter.format(settleDtl.getVoucherTime());
                    if (settleDtlEntity.getTbdMaterialId().equals(settleDtl.getTbdMaterialId())
                            && dString1.equals(dString2)) {
                        // 更新
                        settleDtlEntity.setMatlQty(settleDtlEntity.getMatlQty() + settleDtl.getMatlQty());
                        settleDtlService.updateById(settleDtlEntity);

                        // 更新收货单明细表
                        MatlInOrderDtl matlInOrderDtlEntity = new MatlInOrderDtl();
                        matlInOrderDtlEntity.setId(settleDtl.getTsmMatlInOrderDtlId());
                        matlInOrderDtlEntity.setTmmSettleDtlId(settleDtlEntity.getId());
                        matlInOrderDtlService.updateById(matlInOrderDtlEntity);

                    } else {
                        // 新增
                        settleDtl.setTmmSettleId(settle.getId());
                        settleDtlMapper.insert(settleDtl);

                        // 更新收货单明细表
                        MatlInOrderDtl matlInOrderDtlEntity = new MatlInOrderDtl();
                        matlInOrderDtlEntity.setId(settleDtl.getTsmMatlInOrderDtlId());
                        matlInOrderDtlEntity.setTmmSettleDtlId(settleDtl.getId());
                        matlInOrderDtlService.updateById(matlInOrderDtlEntity);
                    }
                }
            } else {
                // 新增
                settleDtl.setTmmSettleId(settle.getId());
                settleDtlMapper.insert(settleDtl);

                // 更新收货单明细表
                MatlInOrderDtl matlInOrderDtlEntity = new MatlInOrderDtl();
                matlInOrderDtlEntity.setId(settleDtl.getTsmMatlInOrderDtlId());
                matlInOrderDtlEntity.setTmmSettleDtlId(settleDtl.getId());
                matlInOrderDtlService.updateById(matlInOrderDtlEntity);
            }

        }

    }

}
