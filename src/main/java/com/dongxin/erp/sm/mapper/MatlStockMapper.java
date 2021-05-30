package com.dongxin.erp.sm.mapper;

import java.util.List;

import com.dongxin.erp.sm.entity.CurrentStock;
import org.apache.ibatis.annotations.Param;
import com.dongxin.erp.sm.entity.MatlStock;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

/**
 * @Description: 总结存表
 * @Author: jeecg-boot
 * @Date:   2020-11-10
 * @Version: V1.0
 */
@Component
public interface MatlStockMapper extends BaseMapper<MatlStock> {

    /**
     * 删除总结存
     * @param tenantId 租户id
     */
    void delStock(@Param("tenantId") String tenantId);


    /**
     * 添加总结存
     * @param tenantId 租户id
     */
   void  finalStock(@Param("tenantId") String tenantId);

    /**
     * 获取实时库存, 根据流水表统计
     * @param tenantId 租户id
     * @return 当前库存
     */
    List<CurrentStock> wBookCurrentStock(@Param("tenantId") String tenantId);


}
