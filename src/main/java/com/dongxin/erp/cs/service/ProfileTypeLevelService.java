package com.dongxin.erp.cs.service;

import cn.hutool.core.util.ObjectUtil;
import com.dongxin.erp.cs.entity.ProfileInf;
import com.dongxin.erp.cs.entity.ProfileTypeLevel;
import com.dongxin.erp.cs.mapper.ProfileTypeLevelMapper;
import com.dongxin.erp.cs.vo.ProfileTypeLevelHis;
import org.jeecg.config.mybatis.TenantContext;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.List;
import org.jeecg.common.system.base.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 客商类型等级表
 * @Author: jeecg-boot
 * @Date:   2020-11-10
 * @Version: V1.0
 */
@Service
public class ProfileTypeLevelService extends BaseService<ProfileTypeLevelMapper, ProfileTypeLevel>{

    @Autowired
    private ProfileTypeLevelMapper profileTypeLevelMapper;
    @Autowired
    private ProfileInfService profileInfService;

	public List<ProfileTypeLevel> selectByMainId(String mainId) {
		return baseMapper.selectByMainId(mainId);
	}

	public void deleteBatchByMainId(String id) {
        List<ProfileTypeLevel> details = selectByMainId(id);
        for(ProfileTypeLevel detail : details){
            logicDelete(detail);
        }
    }

    public String generalId() {
        DecimalFormat orderNoSeqDf = new DecimalFormat("0000000000000000000");
        Long max = profileTypeLevelMapper.settleMaxId();
        if (ObjectUtil.isNull(max)) {
            max = 0L;
        }
        return orderNoSeqDf.format(max + 1);
    }

    public void insertResume(ProfileTypeLevel profileTypeLevel){
        profileInfService.insertResumeWhenClickButton(profileTypeLevel.getCsId());
    }

    public List<ProfileTypeLevelHis> selectResume(String cs_id,String cs_flag){
	    return profileTypeLevelMapper.selectResume(cs_id,cs_flag, TenantContext.getTenant());
    }



}
