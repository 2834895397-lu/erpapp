package com.dongxin.erp.cs.service;

import com.dongxin.erp.cs.entity.ProfilePoa;
import com.dongxin.erp.cs.mapper.ProfilePoaMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import org.jeecg.common.system.base.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 客商委托书
 * @Author: jeecg-boot
 * @Date:   2020-11-10
 * @Version: V1.0
 */
@Service
public class ProfilePoaService extends BaseService<ProfilePoaMapper, ProfilePoa>{

	public List<ProfilePoa> selectByMainId(String mainId) {
		return baseMapper.selectByMainId(mainId);
	}

	public void deleteBatchByMainId(String id) {
        List<ProfilePoa> details = selectByMainId(id);
        for(ProfilePoa detail : details){
            logicDelete(detail);
        }
    }

}
