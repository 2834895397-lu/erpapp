package com.dongxin.erp.cs.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dongxin.erp.cs.entity.ProfileBank;
import com.dongxin.erp.cs.entity.ProfileInf;
import com.dongxin.erp.cs.entity.ProfileTypeLevel;
import com.dongxin.erp.cs.mapper.ProfileBankMapper;
import com.dongxin.erp.cs.mapper.ProfileInfMapper;
import com.dongxin.erp.cs.mapper.ProfileTypeLevelMapper;
import org.jeecg.common.system.base.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

/**
 * @Description: 客商银行账户管理
 * @Author: jeecg-boot
 * @Date: 2020-11-10
 * @Version: V1.0
 */
@Service
public class ProfileBankService extends BaseService<ProfileBankMapper, ProfileBank> {
    @Autowired
    ProfileBankMapper profileBankMapper;
    @Autowired
    ProfileInfService profileInfService;
    @Autowired
    ProfileInfMapper profileInfMapper;
    @Autowired
    private ProfileTypeLevelService profileTypeLevelService;
    @Autowired
    private ProfileTypeLevelMapper profileTypeLevelMapper;;

    public List<ProfileBank> selectByMainId(String mainId) {
        return baseMapper.selectByMainId(mainId);
    }

    public void deleteBatchByMainId(String id) {
        List<ProfileBank> details = selectByMainId(id);
        for (ProfileBank detail : details) {
            logicDelete(detail);
        }
    }

    //支付账号、收款开户行逻辑
    public List<ProfileBank> selprofileBank(String id) {
        QueryWrapper<ProfileBank> wrapper = new QueryWrapper<>();
        wrapper.eq("cs_id", id);
        List<ProfileBank> list = list(wrapper);
        return list;

    }

    // 收款账号逻辑
    public List<ProfileBank> selprofileBankAccount(String id) {
        QueryWrapper<ProfileBank> wrapper = new QueryWrapper<>();
        wrapper.eq("opening_bank", id);
        List<ProfileBank> list = list(wrapper);
        return list;
    }

    public String generalId() {
        DecimalFormat orderNoSeqDf = new DecimalFormat("0000000000000000000");
        Long max = profileBankMapper.settleMaxId();
        if (ObjectUtil.isNull(max)) {
            max = 0L;
        }
        return orderNoSeqDf.format(max + 1);
    }

    public void insertResume(ProfileBank profileBank) {

        profileInfService.insertResumeWhenClickButton(profileBank.getCsId());

    }

    public void insertResumeWhenDeleteBetch(ProfileInf profileInf) {

        profileInfService.insertResume(profileInf);

    }

    public ProfileInf getProfileRecordWhenDeleteBetch(String ids){

        List<ProfileBank> profileBanks = this.listByIds(Arrays.asList(ids.split(",")));

        return  profileInfService.getById(profileBanks.get(0).getCsId());

    }

}
