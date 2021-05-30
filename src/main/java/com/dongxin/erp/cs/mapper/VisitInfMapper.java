package com.dongxin.erp.cs.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.dongxin.erp.cs.entity.VisitInf;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import javax.xml.crypto.Data;

/**
 * @Description: 顾客拜访登记
 * @Author: jeecg-boot
 * @Date:   2020-11-10
 * @Version: V1.0
 */
@Repository
public interface VisitInfMapper extends BaseMapper<VisitInf> {

    //拜访时间范围查询
    List<VisitInf> visitTimeRangeQuery(Date begin_time, Date end_time,String tenant_id);

}
