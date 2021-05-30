package com.dongxin.erp.sm.service;

import cn.hutool.core.collection.CollUtil;
import com.dongxin.erp.bd.mapper.NodeMapper;
import com.dongxin.erp.bd.service.MaterialService;
import com.dongxin.erp.bd.service.NodeService;
import com.dongxin.erp.sm.entity.CurrentStock;
import com.dongxin.erp.sm.entity.MatlMoveOrderDtl;
import com.dongxin.erp.sm.entity.MatlOutOrderDtl;
import com.dongxin.erp.sm.entity.MatlStock;
import com.dongxin.erp.sm.mapper.MatlStockMapper;
import org.jeecg.config.mybatis.TenantContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.jeecg.common.system.base.service.BaseService;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @Description: 总结存表
 * @Author: jeecg-boot
 * @Date: 2020-11-10
 * @Version: V1.0
 */
@Service
public class MatlStockService extends BaseService<MatlStockMapper, MatlStock> {

    @Autowired
    MatlStockMapper matlStockMapper;

    @Autowired
    MaterialService materialService;

    @Autowired
    NodeService nodeService;

    @Autowired
    NodeMapper nodeMapper;

    /**
     * 总结存
     */
    @Transactional
    public void finalStock() {
        matlStockMapper.delStock(TenantContext.getTenant());
        matlStockMapper.finalStock(TenantContext.getTenant());
    }


    /**
     * 获取出入库流水表的实时库存
     * @return
     */
    public List<CurrentStock> wBookCurrentStock() {
        return matlStockMapper.wBookCurrentStock(TenantContext.getTenant());
    }

    /**
     * 根据查询条件来查询当前库存
     * @param currentStock 查询条件
     * @return 查询之后的值
     */
    public List<CurrentStock> currentStock(CurrentStock currentStock) {
        String toTbdNodeId = currentStock.getToTbdNodeId();
        String tbdMaterialId = currentStock.getTbdMaterialId();
        String tenantId = currentStock.getTenantId();
        List<CurrentStock> currentStocks = matlStockMapper.wBookCurrentStock(TenantContext.getTenant());
        if (toTbdNodeId != null) {
            List<CurrentStock> temp = new ArrayList<>();
            for (CurrentStock stock : currentStocks) {
                if (toTbdNodeId.equals(stock.getToTbdNodeId())) {
                    temp.add(stock);
                }
            }
            currentStocks = temp;
        }

        if (tbdMaterialId != null) {
            List<CurrentStock> temp = new ArrayList<>();
            for (CurrentStock stock : currentStocks) {
                if (tbdMaterialId.equals(stock.getTbdMaterialId())) {
                    temp.add(stock);
                }
            }
            currentStocks = temp;
        }
        if (tenantId != null) {
            List<CurrentStock> temp = new ArrayList<>();
            for (CurrentStock stock : currentStocks) {
                if (tenantId.equals(stock.getTenantId())) {
                    temp.add(stock);
                }
            }
            currentStocks = temp;
        }
        return currentStocks;
    }


    /**
     * @return 物料id和当前库存量的map
     */
    public Map<String, Integer> getIdCountMap() {
        Map<String, Integer> map = new HashMap<>();
        List<CurrentStock> currentStock = matlStockMapper.wBookCurrentStock(TenantContext.getTenant());
        for (CurrentStock stock : currentStock) {
            map.put(stock.getTbdMaterialId(), stock.getCount());
        }
        return map;
    }

    /**
     * 判断库存是否充足, 充足返回"ok", 不充足返回库存具体情况
     *
     * @param nodeId     库存地Id
     * @param materialId 物料id
     * @param count      数量
     * @return
     */
    public String isEnough(String nodeId, String materialId, Integer count) {
        List<CurrentStock> currentStocks = wBookCurrentStock();
        int flag = 0;
        if (CollUtil.isNotEmpty(currentStocks)) {
            for (CurrentStock stock : currentStocks) {
                if (stock.getTbdMaterialId().equals(materialId) && stock.getToTbdNodeId().equals(nodeId)) {
                    if (stock.getCount() < count) {
                        return stock.getTbdMaterialName() + " 在 " + stock.getToTbdNodeName() + "的库存不足, 库存余量:" + stock.getCount()+";";
                    }
                    return "ok";
                }
                flag = 1;
            }
        }
        if (flag == 1) {
            return nodeService.idAndName(nodeId) + " 不存在 " + materialService.idAndName(materialId);
        }
        return "库存为空!";
    }

    /**
     * 计算相同物料和库存地的物料总和
     * @param matlOutOrderDtls
     * @param size
     * @return 用==判断传进来和返回的地址值, 如果相等则说明没有相同的物料和库存地
     */
    public List<MatlOutOrderDtl> matOutComputedSum(List<MatlOutOrderDtl> matlOutOrderDtls, int size) {
            List<MatlOutOrderDtl> list = new ArrayList<>();
            for (int i = 0; i < matlOutOrderDtls.size() - 1; i++) {
                for (int j = i + 1; j < matlOutOrderDtls.size(); j++) {
                    if (matlOutOrderDtls.get(i).getTbdMaterialId().equals(matlOutOrderDtls.get(j).getTbdMaterialId()) && matlOutOrderDtls.get(i).getTbdNodeId().equals(matlOutOrderDtls.get(j).getTbdNodeId())) {
                        MatlOutOrderDtl matlOutOrderDtl = new MatlOutOrderDtl();
                        matlOutOrderDtl.setTbdNodeId(matlOutOrderDtls.get(i).getTbdNodeId());
                        matlOutOrderDtl.setTbdMaterialId(matlOutOrderDtls.get(i).getTbdMaterialId());
                        matlOutOrderDtl.setMatlQty(matlOutOrderDtls.get(i).getMatlQty() + matlOutOrderDtls.get(j).getMatlQty());
                        list.add(matlOutOrderDtl);
                    }
                }
            }
            if (list.size() == 0) {
                return matlOutOrderDtls;
            }
            return matOutComputedSum(list, list.size());
    }
    /**
     * 计算相同物料和库存地的物料总和
     * @param matlMoveOrderDtls
     * @param size
     * @return 用==判断传进来和返回的地址值, 如果相等则说明没有相同的物料和库存地
     */
    public List<MatlMoveOrderDtl> matMoveComputedSum(List<MatlMoveOrderDtl> matlMoveOrderDtls, int size) {
        List<MatlMoveOrderDtl> list = new ArrayList<>();
        for (int i = 0; i < matlMoveOrderDtls.size() - 1; i++) {
            for (int j = i + 1; j < matlMoveOrderDtls.size(); j++) {
                if (matlMoveOrderDtls.get(i).getTbdMaterialId().equals(matlMoveOrderDtls.get(j).getTbdMaterialId()) && matlMoveOrderDtls.get(i).getFromTbdNodeId().equals(matlMoveOrderDtls.get(j).getFromTbdNodeId())) {
                    MatlMoveOrderDtl matlMoveOrderDtl = new MatlMoveOrderDtl();
                    matlMoveOrderDtl.setFromTbdNodeId(matlMoveOrderDtls.get(i).getFromTbdNodeId());
                    matlMoveOrderDtl.setTbdMaterialId(matlMoveOrderDtls.get(i).getTbdMaterialId());
                    matlMoveOrderDtl.setMatlQty(matlMoveOrderDtls.get(i).getMatlQty() + matlMoveOrderDtls.get(j).getMatlQty());
                    list.add(matlMoveOrderDtl);
                }
            }
        }
        if (list.size() == 0) {
            return matlMoveOrderDtls;
        }
        return matMoveComputedSum(list, list.size());
    }

}
