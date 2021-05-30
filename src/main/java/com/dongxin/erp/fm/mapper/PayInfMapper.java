package com.dongxin.erp.fm.mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import com.dongxin.erp.fm.entity.PayInf;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 财务付款信息表
 * @Author: jeecg-boot
 * @Date:   2020-11-18
 * @Version: V1.0
 */
public interface PayInfMapper extends BaseMapper<PayInf> {

    List<PayInf> payInfQuery(@Param("code") String code,@Param("payNatureName") String payNatureName,
                             @Param("contractCode") String contractCode, @Param("csNameRecept") String csNameRecept,
                             @Param("beginTime") Date beginTime, @Param("endTime") Date endTime,@Param("tenant") String tenant);

    List<Map<String, String>> selectPayNature();
}
