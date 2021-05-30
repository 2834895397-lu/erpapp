package com.dongxin.erp.sm.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.dongxin.erp.sm.entity.MatlBalance;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

/**
 * @Description: 日结存表
 * @Author: jeecg-boot
 * @Date: 2020-11-10
 * @Version: V1.0
 */
@Component
public interface MatlBalanceMapper extends BaseMapper<MatlBalance> {

    /**
     * 日结存
     * @param tenantId 租户id
     */
    void addRecords(@Param("tenantId") String tenantId);

    /**
     * 根据指定日期删除日结存
     */
    void delRecords(@Param("tenantId") String tenantId,@Param("list") List<Date> list);

    /**
     * 根据指定日期重新添加日结存
     * @param tenantId 租户id
     * @param list 指定日期
     */
    void reAddRecords(@Param("tenantId") String tenantId, @Param("list") List<Date> list);


    /**
     * 删除当天日结存
     * @param tenantId 租户id
     * */
    void delBalanceByNow(@Param("tenantId") String tenantId);



}
