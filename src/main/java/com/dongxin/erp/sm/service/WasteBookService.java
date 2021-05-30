package com.dongxin.erp.sm.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dongxin.erp.enums.OutAndInWarehouseTypes;
import com.dongxin.erp.enums.WasteBookTypes;
import com.dongxin.erp.sm.entity.*;
import com.dongxin.erp.sm.mapper.WasteBookMapper;
import com.dongxin.erp.sm.service.impl.MatlInOrderDtlServiceImpl;
import com.dongxin.erp.sm.service.impl.MatlInOrderServiceImpl;
import com.dongxin.erp.sm.service.impl.MatlMoveOrderDtlServiceImpl;
import com.dongxin.erp.sm.service.impl.MatlOutOrderDtlServiceImpl;
import org.jeecg.config.mybatis.TenantContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.jeecg.common.system.base.service.BaseService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: 出入库流水表
 * @Author: jeecg-boot
 * @Date: 2020-11-10
 * @Version: V1.0
 */
@Service
public class WasteBookService extends BaseService<WasteBookMapper, WasteBook> {

    @Autowired
    MatlInOrderServiceImpl matlInOrderService;
    @Autowired
    MatlInOrderDtlServiceImpl matlInOrderDtlService;

    @Autowired
    MatlOutOrderDtlServiceImpl matlOutOrderDtlService;
    @Autowired
    MatlMoveOrderDtlServiceImpl matlMoveOrderDtlService;

    @Autowired
    MatlStockService matlStockService;

    /**
     * @param idsDate 收货单主表的id和Date(审核日期)
     * @return 入库流水实体或当前库存消息
     */
    public List<WasteBook> matIODToWasteBook(Map<String, Date> idsDate) {

        List<WasteBook> wasteBooks = new ArrayList<>();
        for (String id : idsDate.keySet()) {
            QueryWrapper<MatlInOrderDtl> wrapper = new QueryWrapper<>();
            wrapper.eq("tsm_matl_in_order_id", id);
            List<MatlInOrderDtl> matlInOrderDtls = matlInOrderDtlService.list(wrapper);
            for (MatlInOrderDtl inOrderDtl : matlInOrderDtls) {
                WasteBook wasteBook = new WasteBook();
                wasteBook.setToTbdNodeId(inOrderDtl.getTbdNodeId());
                //单据类型, 收货单入库
                wasteBook.setType(WasteBookTypes.INORDERCOME.getCode());
                wasteBook.setOrderId(inOrderDtl.getTsmMatlInOrderId());
                wasteBook.setToTbdNodeId(inOrderDtl.getTbdNodeId());
                wasteBook.setTbdMaterialId(inOrderDtl.getTbdMaterialId());
                //过账日期
                wasteBook.setPostTime(idsDate.get(id));
                wasteBook.setInQty(inOrderDtl.getMatlQty());
                wasteBook.setOutQty(0);
                wasteBook.setMatlPrice(inOrderDtl.getMatlPrice());
                //出入库方向  入库
                wasteBook.setMatlDirect(OutAndInWarehouseTypes.IN.getCode());
                wasteBook.setPayBb(inOrderDtl.getPayBb());
                wasteBook.setBusiId(inOrderDtl.getId());
                wasteBooks.add(wasteBook);
            }
        }
        return wasteBooks;
    }

    /**
     * @param idsDate 出库单主表的id和审核日期
     * @return 出库流水实体或当前库存消息
     */
    public List<?> matOODToWasteBook(Map<String, Date> idsDate) {
        List<String> ids = new ArrayList<>();
        for (String id : idsDate.keySet()) {
            ids.add(id);
        }
        QueryWrapper<MatlOutOrderDtl> wrapper = new QueryWrapper<>();
        wrapper.in("tsm_matl_out_order_id", ids);
        List<MatlOutOrderDtl> matlOutOrderDtls = matlOutOrderDtlService.list(wrapper);
        String msg = "";
        List<WasteBook> wasteBooks = new ArrayList<>();
        //逐个判断库存是否充足并转成出库流水
        for (MatlOutOrderDtl matlOutOrderDtl : matlOutOrderDtls) {
            for (String id : idsDate.keySet()) {
                if (id.equals(matlOutOrderDtl.getTsmMatlOutOrderId())) {
                    //判断库存是否充足
                    String enough = matlStockService.isEnough(matlOutOrderDtl.getTbdNodeId(), matlOutOrderDtl.getTbdMaterialId(), matlOutOrderDtl.getMatlQty());
                    if (!"ok".equals(enough)) {
                        msg += enough + "  ";
                    }
                    WasteBook wasteBook = new WasteBook();
                    wasteBook.setToTbdNodeId(matlOutOrderDtl.getTbdNodeId());
                    //单据类型, 领用单出库
                    wasteBook.setType(WasteBookTypes.OUTORDEROUT.getCode());
                    wasteBook.setOrderId(matlOutOrderDtl.getTsmMatlOutOrderId());
                    wasteBook.setToTbdNodeId(matlOutOrderDtl.getTbdNodeId());
                    wasteBook.setTbdMaterialId(matlOutOrderDtl.getTbdMaterialId());
                    wasteBook.setPostTime(idsDate.get(id));
                    wasteBook.setOutQty(matlOutOrderDtl.getMatlQty());
                    wasteBook.setInQty(0);
                    wasteBook.setMatlPrice(matlOutOrderDtl.getMatlPrice());
                    //出入库方向   出库
                    wasteBook.setMatlDirect(OutAndInWarehouseTypes.OUT.getCode());
                    wasteBook.setPayBb(matlOutOrderDtl.getPayBb());
                    wasteBook.setBusiId(matlOutOrderDtl.getId());
                    wasteBooks.add(wasteBook);
                }
            }
        }
        //判断是否有重复的库存地和物料, 对其数量求和再判断库存是否充足
        List<MatlOutOrderDtl> returns = matlStockService.matOutComputedSum(matlOutOrderDtls, matlOutOrderDtls.size());
        //地址值不相等说明有重复
        if (returns != matlOutOrderDtls) {
            for (MatlOutOrderDtl aReturn : returns) {
                //判断库存是否充足
                String enough = matlStockService.isEnough(aReturn.getTbdNodeId(), aReturn.getTbdMaterialId(), aReturn.getMatlQty());
                if (!"ok".equals(enough)) {
                    msg += enough + "  ";
                }
            }
        }


        if (msg.length() != 0) {
            ArrayList<String> list = new ArrayList<>();
            list.add(msg);
            return list;
        }
        return wasteBooks;
    }

    /**
     * 获取出库流水实体
     * @param idsDate 移库单主表的id和审核时间
     * @return 出库流水实体类集合或当前库存消息集合
     */
    public List<?> matMODToWasteBookOut(Map<String, Date> idsDate) {
        List<String> ids = new ArrayList<>();
        for (String id : idsDate.keySet()) {
            ids.add(id);
        }
        QueryWrapper<MatlMoveOrderDtl> wrapper = new QueryWrapper<>();
        wrapper.in("tsm_matl_move_order_id", ids);
        List<MatlMoveOrderDtl> matlMoveOrderDtls = matlMoveOrderDtlService.list(wrapper);
        String msg = "";
        List<WasteBook> wasteBooks = new ArrayList<>();
        for (MatlMoveOrderDtl matlMoveOrderDtl : matlMoveOrderDtls) {
            for (String id : idsDate.keySet()) {
                if (id.equals(matlMoveOrderDtl.getTsmMatlMoveOrderId())) {
                    WasteBook wasteBook = new WasteBook();
                    wasteBook.setToTbdNodeId(matlMoveOrderDtl.getTbdMaterialId());
                    //单据类型, 移库单移库
                    wasteBook.setType(WasteBookTypes.MOVEORDERMOVE.getCode());
                    wasteBook.setOrderId(matlMoveOrderDtl.getTsmMatlMoveOrderId());
                    wasteBook.setToTbdNodeId(matlMoveOrderDtl.getFromTbdNodeId());
                    wasteBook.setTbdMaterialId(matlMoveOrderDtl.getTbdMaterialId());
                    wasteBook.setPostTime(idsDate.get(id));
                    wasteBook.setOutQty(matlMoveOrderDtl.getMatlQty());
                    wasteBook.setInQty(0);
                    wasteBook.setMatlPrice(matlMoveOrderDtl.getMatlPrice());
                    //出入库方向  出库
                    wasteBook.setMatlDirect(OutAndInWarehouseTypes.OUT.getCode());
                    wasteBook.setPayBb(matlMoveOrderDtl.getPayBb());
                    wasteBook.setBusiId(matlMoveOrderDtl.getId());
                    wasteBooks.add(wasteBook);

                    String enough = matlStockService.isEnough(matlMoveOrderDtl.getFromTbdNodeId(), matlMoveOrderDtl.getTbdMaterialId(), matlMoveOrderDtl.getMatlQty());
                    if (!"ok".equals(enough)) {
                        msg += enough + " ";
                    }

                }

            }
        }
        //判断是否有重复的库存地和物料, 对其数量求和再判断库存是否充足
        List<MatlMoveOrderDtl> returns = matlStockService.matMoveComputedSum(matlMoveOrderDtls, matlMoveOrderDtls.size());
        //地址值不相等说明有重复
        if (returns != matlMoveOrderDtls) {
            for (MatlMoveOrderDtl aReturn : returns) {
                //判断库存是否充足
                String enough = matlStockService.isEnough(aReturn.getFromTbdNodeId(), aReturn.getTbdMaterialId(), aReturn.getMatlQty());
                if (!"ok".equals(enough)) {
                    msg += enough + "  ";
                }
            }
        }
        if (msg.length() != 0) {
            ArrayList<String> list = new ArrayList<>();
            list.add(msg);
            return list;
        }
        return wasteBooks;
    }

    /**
     * 获取入库流水实体
     * @param idsDate 移库单主表的id和审核时间
     * @return 入库流水实体类集合或当前库存消息集合
     */
    public List<WasteBook> matMODToWasteBookIn(Map<String, Date> idsDate) {
        List<String> ids = new ArrayList<>();
        for (String id : idsDate.keySet()) {
            ids.add(id);
        }
        QueryWrapper<MatlMoveOrderDtl> wrapper = new QueryWrapper<>();
        wrapper.in("tsm_matl_move_order_id", ids);
        List<MatlMoveOrderDtl> matlMoveOrderDtls = matlMoveOrderDtlService.list(wrapper);
        List<WasteBook> wasteBooks = new ArrayList<>();
        for (MatlMoveOrderDtl matlMoveOrderDtl : matlMoveOrderDtls) {
            for (String id : idsDate.keySet()) {
                if (id.equals(matlMoveOrderDtl.getTsmMatlMoveOrderId())) {
                    WasteBook wasteBook = new WasteBook();
                    wasteBook.setToTbdNodeId(matlMoveOrderDtl.getTbdMaterialId());
                    //单据类型, 移库单移库
                    wasteBook.setType(WasteBookTypes.MOVEORDERMOVE.getCode());
                    wasteBook.setOrderId(matlMoveOrderDtl.getTsmMatlMoveOrderId());
                    wasteBook.setToTbdNodeId(matlMoveOrderDtl.getToTbdNodeId());
                    wasteBook.setTbdMaterialId(matlMoveOrderDtl.getTbdMaterialId());
                    wasteBook.setPostTime(idsDate.get(id));
                    wasteBook.setInQty(matlMoveOrderDtl.getMatlQty());
                    wasteBook.setOutQty(0);
                    wasteBook.setMatlPrice(matlMoveOrderDtl.getMatlPrice());
                    //出入库方向  1: 入库
                    wasteBook.setMatlDirect(OutAndInWarehouseTypes.IN.getCode());
                    wasteBook.setPayBb(matlMoveOrderDtl.getPayBb());
                    wasteBook.setBusiId(matlMoveOrderDtl.getId());
                    wasteBooks.add(wasteBook);

                }
            }
        }

        return wasteBooks;
    }

}
