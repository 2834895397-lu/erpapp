package com.dongxin.erp.cs.service;

import com.dongxin.erp.cs.entity.ProfileQualifi;
import com.dongxin.erp.cs.mapper.ProfileQualifiMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import org.jeecg.common.system.base.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 供应商资质信息
 * @Author: jeecg-boot
 * @Date:   2020-11-10
 * @Version: V1.0
 */
@Service
public class ProfileQualifiService extends BaseService<ProfileQualifiMapper, ProfileQualifi>{

	public List<ProfileQualifi> selectByMainId(String mainId) {
		return baseMapper.selectByMainId(mainId);
	}

	public void deleteBatchByMainId(String id) {
        List<ProfileQualifi> details = selectByMainId(id);
        for(ProfileQualifi detail : details){
            logicDelete(detail);
        }
    }

}
