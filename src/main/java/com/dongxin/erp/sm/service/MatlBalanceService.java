package com.dongxin.erp.sm.service;

import com.dongxin.erp.sm.entity.MatlBalance;
import com.dongxin.erp.sm.mapper.MatlBalanceMapper;
import org.jeecg.config.mybatis.TenantContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.jeecg.common.system.base.service.BaseService;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

/**
 * @Description: 日结存表
 * @Author: jeecg-boot
 * @Date: 2020-11-10
 * @Version: V1.0
 */
@Service
public class MatlBalanceService extends BaseService<MatlBalanceMapper, MatlBalance> {

    @Autowired
    MatlBalanceMapper matlBalanceMapper;



    /**
     * 日结存, 由定时任务调用
     */
    @Transactional
    public void addRecords() {
        matlBalanceMapper.delBalanceByNow(TenantContext.getTenant());
        matlBalanceMapper.addRecords(TenantContext.getTenant());
    }

    /**
     * 根据指定日期删除日结存
     *
     * @param dates
     */
    public void delRecords(List<Date> dates) {
        matlBalanceMapper.delRecords(TenantContext.getTenant(), dates);
    }


    /**
     * 根据指定日期重新计算日结存
     *
     * @param dates
     */
    public void reAddRecords(List<Date> dates) {
        matlBalanceMapper.reAddRecords(TenantContext.getTenant(), dates);
    }


}
