package com.dongxin.erp.cs.service;

import cn.hutool.core.collection.CollUtil;
import com.dongxin.erp.cs.entity.ProfileCnee;
import com.dongxin.erp.cs.entity.ProfileInf;
import com.dongxin.erp.cs.mapper.ProfileCneeMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

import org.jeecg.common.system.base.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 客户收货人
 * @Author: jeecg-boot
 * @Date:   2020-11-10
 * @Version: V1.0
 */
@Service
public class ProfileCneeService extends BaseService<ProfileCneeMapper, ProfileCnee>{
    @Autowired
    ProfileInfService profileInfService;

	public List<ProfileCnee> selectByMainId(String mainId) {
		return baseMapper.selectByMainId(mainId);
	}

	public void deleteBatchByMainId(String id) {
        List<ProfileCnee> details = selectByMainId(id);
        for(ProfileCnee detail : details){
            logicDelete(detail);
        }
    }

    //客户收货人 翻译省市
    public List<ProfileCnee> translateCneeProviceOrCity(List<ProfileCnee> profileCnees){
        Map<String, String[]> idAndProvincesOrCitiesAndPid = profileInfService.getIdAndProvincesOrCitiesAndPid();
        if (CollUtil.isNotEmpty(idAndProvincesOrCitiesAndPid)) {
            for (ProfileCnee profileCnee1 : profileCnees) {
                if (profileCnee1.getDeliveryArea() != null) {
                    String provinceOrCity = idAndProvincesOrCitiesAndPid.get(profileCnee1.getDeliveryArea())[0];
                    String pid = idAndProvincesOrCitiesAndPid.get(profileCnee1.getDeliveryArea())[1];
                    if (profileCnee1.getDeliveryArea() != null && pid.equals("0")) {
                        profileCnee1.setProvincesAndCities(provinceOrCity);
                    } else {
                        profileCnee1.setProvincesAndCities(idAndProvincesOrCitiesAndPid.get(pid)[0] + "/" + provinceOrCity);
                    }
                }
            }
        }

        return  profileCnees;

    }

}
