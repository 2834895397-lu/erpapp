package com.dongxin.erp.cs.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dongxin.erp.cs.csenum.BlacklistStatus;
import com.dongxin.erp.cs.csenum.CsType;
import com.dongxin.erp.cs.entity.BlacklistInf;
import com.dongxin.erp.cs.entity.ProfileTypeLevel;
import com.dongxin.erp.cs.mapper.BlacklistInfMapper;
import org.jeecg.common.system.base.service.BaseService;
import org.jeecg.common.system.query.QueryGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @Description: 黑名单管理
 * @Author: jeecg-boot
 * @Date: 2020-11-17
 * @Version: V1.0
 */
@Repository
@Service
public class BlacklistInfService extends BaseService<BlacklistInfMapper, BlacklistInf> {
    @Autowired
    private BlacklistInfMapper blacklistInfMapper;
    @Autowired
    private ProfileTypeLevelService profileTypeLevelService;

    public String[][] queryBlacklist() {
        String[][] blacklistArray = new String[this.blacklistInfMapper.queryBlacklist().size()][3];

        List<Map> BlacklistOfLListMap = this.blacklistInfMapper.queryBlacklist();

        if (CollUtil.isNotEmpty(BlacklistOfLListMap)) {
            for (int i = 0; i < BlacklistOfLListMap.size(); i++) {
                blacklistArray[i][0] = MapUtil.getStr(BlacklistOfLListMap.get(i), "id");
                blacklistArray[i][1] = MapUtil.getStr(BlacklistOfLListMap.get(i), "cs_name");
                blacklistArray[i][2] = MapUtil.getStr(BlacklistOfLListMap.get(i), "cs_flag");
            }
        }

        return blacklistArray;
    }

    //黑名单根据cs_id以及cs_name查询类型表id
    public String queryCs(String id, String cs_flag) {
        return this.blacklistInfMapper.queryCs(id, cs_flag);
    }

    //根据id的集合查询所有数据
    public List<BlacklistInf> selectByIds(List<String> ids) {
        return this.blacklistInfMapper.selectByIds(ids);
    }

    //根据黑名单表的id和cs_id查询客商类型的id
    public ProfileTypeLevel queryTypeLevel(String id, String cs_flag) {
        return this.blacklistInfMapper.queryTypeLevel(id, cs_flag);
    }

    //黑名单在删除时 将对应的客商类型表的信息重新设置为非正式
    public void resetCsType(String id) {
        BlacklistInf blacklistInf = this.getById(id);
        String levelId = this.queryCs(id, blacklistInf.getCsFlag());
        ProfileTypeLevel profileTypeLevel = profileTypeLevelService.getById(levelId);
        profileTypeLevel.setCsType("1");
        profileTypeLevelService.saveOrUpdate(profileTypeLevel);
    }

    //黑名单在批量删除时 将对应的客商类型表的信息重新设置为非正式
    public void resetBatchCsType(String ids) {
        List<String> idList = Arrays.asList(ids.split(","));
        List<String> levelIds = new ArrayList<>();

        List<BlacklistInf> blacklistInfs = this.listByIds(idList);

        for (int i = 0; i < idList.size(); i++) {
            String levelId = this.queryCs(idList.get(i), blacklistInfs.get(i).getCsFlag());
            levelIds.add(levelId);
        }

        /*for (String id : idList){
            BlacklistInf blacklistInf = this.getById(id);
            String levelId = this.queryCs(id, blacklistInf.getCsFlag());
            levelIds.add(levelId);
        }*/

        List<ProfileTypeLevel> profileTypeLevels = profileTypeLevelService.listByIds(levelIds);
        for (ProfileTypeLevel profileTypeLevel : profileTypeLevels) {
            profileTypeLevel.setCsType("1");
        }
        this.profileTypeLevelService.saveOrUpdateBatch(profileTypeLevels);
    }

    //将选中的黑名单的对应客商类型 中的cs_type重新设置为非正式
    public String recoverBlacklist(List<String> ids) {
        List<BlacklistInf> blacklistInfs = this.selectByIds(ids);
        if (CollUtil.isNotEmpty(blacklistInfs)) {
            for (BlacklistInf blacklistInf : blacklistInfs) {
                if (blacklistInf.getStatus().equals("1")) {
                    return "该客商不在黑名单里";
                }
            }
        }

        List<ProfileTypeLevel> profileTypeLevels = new ArrayList<>();
        for (BlacklistInf blacklistInf : blacklistInfs) {
            blacklistInf.setStatus("1");
            ProfileTypeLevel A = this.queryTypeLevel(blacklistInf.getId(), blacklistInf.getCsFlag());
            profileTypeLevels.add(A);
        }
        for (ProfileTypeLevel profileTypeLevel : profileTypeLevels) {
            profileTypeLevel.setCsType("1");
        }
        this.saveOrUpdateBatch(blacklistInfs);
        profileTypeLevelService.saveOrUpdateBatch(profileTypeLevels);

        return "操作成功";
    }

    //在黑名单时不可删除
    public String canNotDelete(String id) {
        BlacklistInf blacklistInf = this.getById(id);
        if (blacklistInf.getStatus().equals("2")) {
            return "状态为黑名单时不可删除";
        }
        return "删除成功!";
    }

    //在黑名单时不可批量删除
    public String canNotBatchDelete(String ids) {
        List<String> idList = Arrays.asList(ids.split(","));
        List<BlacklistInf> blacklistInfs = listByIds(idList);
        List<String> idLists = new ArrayList<>();
        for (BlacklistInf blacklistInf :blacklistInfs){
            if (blacklistInf.getStatus().equals("2")){
                return "状态为黑名单时不可删除!";
            }
        }
        return "删除成功!";
    }

    //黑名单联动
    public void blacklistLinkage() {
        BlacklistInf blacklistInf = new BlacklistInf();
        BlacklistStatus blacklistStatus = BlacklistStatus.BLACKLIST;
        blacklistInf.setStatus(blacklistStatus.getCode());
        QueryWrapper<BlacklistInf> query = QueryGenerator.initQueryWrapper(blacklistInf, null);
        List<BlacklistInf> blacklistInfs = this.list(query);

        ProfileTypeLevel profileTypeLevel = new ProfileTypeLevel();
        QueryWrapper<ProfileTypeLevel> query1 = QueryGenerator.initQueryWrapper(profileTypeLevel, null);
        List<ProfileTypeLevel> profileTypeLevel1s = profileTypeLevelService.list(query1);

        String[][] blacklistArray = this.queryBlacklist();
        CsType csType = CsType.blacklist;

        List<String> BlacklistIds = new ArrayList<>();
        List<ProfileTypeLevel> profileTypeLevels = new ArrayList<>();

        for (int i = 0; i < blacklistArray.length; i++) {
            for (BlacklistInf record : blacklistInfs) {
                if (blacklistArray[i][1].equals(record.getCsId()) && blacklistArray[i][2].equals(record.getCsFlag())) {
                    BlacklistIds.add(blacklistArray[i][0]);
                }
            }
        }
        for (ProfileTypeLevel record : profileTypeLevel1s) {
            if (BlacklistIds.contains(record.getId())) {
                record.setCsType(csType.getCode());
                profileTypeLevels.add(record);
            }
        }
        profileTypeLevelService.updateBatchById(profileTypeLevels);
    }

}
