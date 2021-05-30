package com.dongxin.erp.sm.job;

import com.dongxin.erp.sm.service.MatlStockService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.config.mybatis.TenantContext;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class MatlStockJob implements Job {
    @Autowired
    MatlStockService matlStockService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        TenantContext.setTenant("8");
        try {
            matlStockService.finalStock();
            log.info(" 总结存定时任务 !  执行时间:" + new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(new Date()));
        }finally {
            TenantContext.clear();
        }
    }
}
