package com.dongxin.erp.sm.job;

import com.dongxin.erp.sm.service.MatlBalanceService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;
/**
 *
 * @author jacklu
 * @return null
 */
@Slf4j
public class MatlBalanceJob implements Job {

    @Autowired
    MatlBalanceService matlBalanceService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        matlBalanceService.addRecords();
        log.info(" 日结存定时任务 !  执行时间:" + new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(new Date()));
    }
}
