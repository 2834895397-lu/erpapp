package com.dongxin.erp.cs.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dongxin.erp.cs.csenum.*;
import com.dongxin.erp.cs.entity.ProfileBank;
import com.dongxin.erp.cs.entity.ProfileInf;
import com.dongxin.erp.cs.entity.ProfileInfo;
import com.dongxin.erp.cs.entity.ProfileTypeLevel;
import com.dongxin.erp.cs.mapper.ProfileBankMapper;
import com.dongxin.erp.cs.mapper.ProfileInfMapper;
import com.dongxin.erp.cs.mapper.ProfileTypeLevelMapper;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.base.service.BaseService;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.config.mybatis.TenantContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.*;

/**
 * @Description: 客商基础信息
 * @Author: jeecg-boot
 * @Date: 2020-11-10
 * @Version: V1.0
 */
@Service
public class ProfileInfService extends BaseService<ProfileInfMapper, ProfileInf> {

    @Autowired
    private ProfilePoaService profilePoaService;
    @Autowired
    private ProfileCneeService profileCneeService;
    @Autowired
    private ProfileBankService profileBankService;
    @Autowired
    private ProfileBelongService profileBelongService;
    @Autowired
    private ProfileProductService profileProductService;
    @Autowired
    private ProfileQualifiService profileQualifiService;
    @Autowired
    private ProfileTypeLevelService profileTypeLevelService;
    @Autowired
    private ProfileInfMapper profileinfmapper;
    @Autowired
    private ProfileTypeLevelMapper profileTypeLevelMapper;
    @Autowired
    private ProfileBankMapper profileBankMapper;
    
    @Autowired
    private ProfileInfMapper profileInfMapper;


    @Transactional
    public void delMain(String id) {
        profilePoaService.deleteBatchByMainId(id);
        profileCneeService.deleteBatchByMainId(id);
        profileBankService.deleteBatchByMainId(id);
        profileBelongService.deleteBatchByMainId(id);
        profileProductService.deleteBatchByMainId(id);
        profileQualifiService.deleteBatchByMainId(id);
        profileTypeLevelService.deleteBatchByMainId(id);
        logicDeleteById(id);
    }

    @Transactional
    public void newDelMain(String id) {
        profilePoaService.deleteBatchByMainId(id);
        profileCneeService.deleteBatchByMainId(id);
        profileBankService.deleteBatchByMainId(id);
        profileBelongService.deleteBatchByMainId(id);
        profileProductService.deleteBatchByMainId(id);
        profileQualifiService.deleteBatchByMainId(id);
    }

    @Transactional
    public void delBatchMain(Collection<? extends Serializable> idList) {
        for (Serializable id : idList) {
            profilePoaService.deleteBatchByMainId(id.toString());
            profileCneeService.deleteBatchByMainId(id.toString());
            profileBankService.deleteBatchByMainId(id.toString());
            profileBelongService.deleteBatchByMainId(id.toString());
            profileProductService.deleteBatchByMainId(id.toString());
            profileQualifiService.deleteBatchByMainId(id.toString());
            profileTypeLevelService.deleteBatchByMainId(id.toString());
            logicDeleteById(id);
        }
    }


    //翻译省市
    public Map<String, String[]> getIdAndProvincesOrCitiesAndPid() {

        List<Map<String, String>> idAndProvinceOrCityAndPidOfList = profileinfmapper.getIdAndProvincesOrCitiesAndPid();
        Map<String, String[]> idAndProvinceAndPidOfMap = new HashMap<>();
        String[][] provinceOrCityAndPid = new String[idAndProvinceOrCityAndPidOfList.size()][2];
        if (!idAndProvinceOrCityAndPidOfList.isEmpty()) {

            for (int i = 0; i < idAndProvinceOrCityAndPidOfList.size(); i++) {
                provinceOrCityAndPid[i][0] = idAndProvinceOrCityAndPidOfList.get(i).get("province_or_city");
                provinceOrCityAndPid[i][1] = idAndProvinceOrCityAndPidOfList.get(i).get("pid");
            }

            for (int i = 0; i < idAndProvinceOrCityAndPidOfList.size(); i++) {
                idAndProvinceAndPidOfMap.put(idAndProvinceOrCityAndPidOfList.get(i).get("id"), provinceOrCityAndPid[i]);
            }
        }
        return idAndProvinceAndPidOfMap;

    }

    //设置当前操作用户姓名
    public void setCheckName(ProfileInf profileInf) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        profileInf.setChecker(loginUser.getRealname());
    }

    //获取客商等级，类型以及标识
    public List<ProfileInf> getCs(String cs_flag, String cs_code, String regis_place, String cs_type, String cs_level, String cs_name,String tenant_id) {
        return this.profileinfmapper.selectCs(cs_flag, cs_code, regis_place, cs_type, cs_level, cs_name,tenant_id);
    }

    //客商代码自动生成
    public void generalCsCode(ProfileInf param) {
        if (param.getCsCode() == null) {
            DecimalFormat orderNoSeqDf = new DecimalFormat("00000");
            Long max = profileinfmapper.settleMaxNum();
            if (ObjectUtil.isNull(max)) {
                max = 0L;
            }
            param.setCsCode(orderNoSeqDf.format(max + 1));
        }
    }

    //履历表客商id自动生成
    public String generalId() {
        DecimalFormat orderNoSeqDf = new DecimalFormat("0000000000000000000");
        Long max = profileinfmapper.settleMaxId();
        if (ObjectUtil.isNull(max)) {
            max = 0L;
        }
        return orderNoSeqDf.format(max + 1);
    }

    //通过cs_id 查找cs_name
    public String queryCsName(String cs_id) {
        return this.profileinfmapper.queryCsName(cs_id);
    }

    //通过客商代码查询id
    public String queryId(String id) {
        return this.profileinfmapper.queryId(id);
    }

    //查询物料表的id，pid，name
    public Map<String, String[]> queryAllMaterial() {
        List<Map> AllMaterials = this.profileinfmapper.queryAllMaterial();
        Map<String, String[]> materials = new HashMap<>();
        String[] idAndPid = new String[2];

        for (Map material : AllMaterials) {
            idAndPid[0] = MapUtil.getStr(material, "id");
            idAndPid[1] = MapUtil.getStr(material, "pid");
            materials.put(idAndPid[0], idAndPid);
        }
        return materials;

    }

    //支付名称、供应商名称逻辑

    public List<ProfileInf> selprofileInf(String id) {
        QueryWrapper<ProfileInf> queryWrapper = new QueryWrapper();
        queryWrapper.eq("id", id);
        List<ProfileInf> profileInfs = list(queryWrapper);
        return profileInfs;

    }

    //根据cs_id 以及cs_flag 删除客商类型数据
    public String queryByCsIdAndCsFlag(String cs_id, String cs_flag) {
        return this.profileinfmapper.queryByCsIdAndCsFlag(cs_id, cs_flag);
    }

    //根据cs_id 以及客商类型查询主表数据
    public ProfileTypeLevel queryTypeByCsIdAndCsFlag(String id, String cs_flag) {

        ProfileTypeLevel profileTypeLevel = new ProfileTypeLevel();
        profileTypeLevel.setCsId(id);
        profileTypeLevel.setDelFlag(0);
        profileTypeLevel.setCsFlag(cs_flag);
        QueryWrapper<ProfileTypeLevel> query1 = QueryGenerator.initQueryWrapper(profileTypeLevel, null);
        List<ProfileTypeLevel> profileTypeLevels = profileTypeLevelService.list(query1);
        ProfileTypeLevel profileTypeLevel1 = profileTypeLevels.get(0);

        return profileTypeLevel1;

    }

    //根据客商名称查询主表数据
    public List<ProfileInf> queryMainPageByCsName(String name) {

        ProfileInf profileInf = new ProfileInf();
        profileInf.setCsName(name);

        DelFlag delFlag = DelFlag.notDeleted;
        profileInf.setDelFlag(delFlag.getCode());

        QueryWrapper<ProfileInf> query = QueryGenerator.initQueryWrapper(profileInf, null);
        List<ProfileInf> profileInfs = this.list(query);

        return profileInfs;

    }

    //主表翻译省市
    public List<ProfileInf> translateProvinceOrCity(List<ProfileInf> profileInfs) {
        Map<String, String[]> idAndProvincesOrCitiesAndPid = this.getIdAndProvincesOrCitiesAndPid();
        if (CollUtil.isNotEmpty(idAndProvincesOrCitiesAndPid))   {
            for (ProfileInf profileInf : profileInfs) {
                if (profileInf.getRegisPlace() != null) {
                    String provinceOrCity = idAndProvincesOrCitiesAndPid.get(profileInf.getRegisPlace())[0];
                    String pid = idAndProvincesOrCitiesAndPid.get(profileInf.getRegisPlace())[1];
                    if (profileInf.getRegisPlace() != null && pid.equals("0")) {
                        profileInf.setProvincesAndCities(provinceOrCity);
                    } else {
                        profileInf.setProvincesAndCities(idAndProvincesOrCitiesAndPid.get(pid)[0] + "/" + provinceOrCity);
                    }
                }
            }
        }
        return profileInfs;
    }

    //主表添加
    public void mainPageAdd(ProfileInf profileInf) {
        IsUse isUse = IsUse.yes;
        IsCheck isCheck = IsCheck.bussiness;
        profileInf.setIsUse(isUse.getCode());
        profileInf.setIsCheck(isCheck.getCode());
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        profileInf.setChecker(loginUser.getRealname());
    }

    //客户客商类型表自动添加
    public void typeAutoAdd(ProfileInf profileInf, String cs_flag) {
        //通过客商代码 找到主表数据id
        String id = this.queryId(profileInf.getCsCode());
        CsType informal = CsType.informal;
        CsLevel notYet = CsLevel.notYet;
        ProfileTypeLevel profileTypeLevel = new ProfileTypeLevel();
        profileTypeLevel.setCsId(id);
        profileTypeLevel.setCsFlag(cs_flag);
        profileTypeLevel.setCsType(informal.getCode());
        profileTypeLevel.setCsLevel(notYet.getDesc());

        //将自动生成的客商类型表的数据存到数据库
        profileTypeLevelService.save(profileTypeLevel);
    }

    //主表删除数据 如果两张表都有信息就只删除 该数据的客商类型的数据 如果只是单张表存在则直接将所有关于这表信息的的记录删除包括子表
    public void newDelete(String id, String cs_flag) {
        //通过cs_id，以及客商标志查询类型表的信息
        ProfileTypeLevel profileTypeLevel = new ProfileTypeLevel();
        profileTypeLevel.setCsId(id);

        DelFlag delFlag = DelFlag.notDeleted;
        profileTypeLevel.setDelFlag(delFlag.getCode());

        QueryWrapper<ProfileTypeLevel> query = QueryGenerator.initQueryWrapper(profileTypeLevel, null);
        List<ProfileTypeLevel> profileTypeLevels = profileTypeLevelService.list(query);

        if (profileTypeLevels.size() == 1) {
            this.delMain(id);
        } else {
            this.newDelMain(id);
            profileTypeLevelService.removeById(this.queryByCsIdAndCsFlag(id, cs_flag));
        }
    }

    //当审核状态为待审核时将审核状态设置为审核通过 并设置审核时间
    public String checkIt(ProfileInf profileInf, ProfileTypeLevel profileTypeLevel) {
        String message = "";

        //如果状态为待审核
        if (profileInf.getIsCheck().equals("2")) {
            //将审核状态设置为审核通过
            profileInf.setIsCheck("1");

            //设置审核时间
            Date date = new Date();
            profileInf.setCheckDate(date);

            //将客商类型设置为正式
            profileTypeLevel.setCsType("2");

            //将数据更新到数据库
            this.saveOrUpdate(profileInf);
            profileTypeLevelService.saveOrUpdate(profileTypeLevel);

            message = "操作成功";
            return message;
        } else {
            message = "只有待审核状态才能审核通过";
            return message;
        }
    }

    //转为非正式
    public String counterCheckIt(String id, String cs_flag) {

        //通过id拿到主表单条数据
        ProfileInf profileInf = this.getById(id);

        //通过主表id,客商标志拿到客商类型表的信息
        ProfileTypeLevel profileTypeLevel = this.queryTypeByCsIdAndCsFlag(id, cs_flag);

        CsType formal = CsType.formal;
        CsType informal = CsType.informal;
        CsType blacklist = CsType.blacklist;
        CsLevel csLevel = CsLevel.notYet;

        //如果客商类型为正式
        if (profileTypeLevel.getCsType().equals(formal.getCode())) {

            //设置时间
            Date date = new Date();
            profileTypeLevel.setEffectiveTime(date);

            //将客商类型设置为非正式  客商等级设计为暂无
            profileTypeLevel.setCsType(informal.getCode());
            profileTypeLevel.setCsLevel(csLevel.getDesc());

            //将数据更新到数据库
            profileTypeLevelService.saveOrUpdate(profileTypeLevel);

            //将数据插入履历表
            this.insertResumeWhenClickButton(id);

            return "操作成功";
        }
        if (profileTypeLevel.getCsType().equals(blacklist.getCode())) {
            return "黑名单不可转为转为非正式";
        } else {
            return "已经为非正式";
        }

    }

    //当审核状态为待审核时将审核状态设置为审核不通过 并设置审核时间
    public String checkNoPass(ProfileInf profileInf) {

        //如果审核状态为待审核
        if (profileInf.getIsCheck().equals("2")) {
            //将审核状态设置为审核不通过
            profileInf.setIsCheck("3");

            //设置审核日期
            Date date = new Date();
            profileInf.setCheckDate(date);

            //将数据更新到数据库
            this.saveOrUpdate(profileInf);

            return "操作成功";
        } else {
            return "只有待审核状态才能审核不通过";
        }
    }

    //转为正式
    public String applyRegular(String id, String cs_flag) {

        String csTypeId = this.queryByCsIdAndCsFlag(id, cs_flag);

        CsType formal = CsType.formal;
        CsType informal = CsType.informal;


        ProfileTypeLevel profileTypeLevel = profileTypeLevelService.getById(csTypeId);
        if (profileTypeLevel.getCsType().equals(informal.getCode())) {
            profileTypeLevel.setCsType(formal.getCode());
            profileTypeLevel.setEffectiveTime(new Date());
            profileTypeLevelService.saveOrUpdate(profileTypeLevel);
            //将数据插入履历表
            this.insertResumeWhenClickButton(id);
            return "操作成功";
        }
        if (profileTypeLevel.getCsType().equals(formal.getCode())) {
            return "已经为正式";
        }

        return "黑名单不可转为正式";


    }

    //翻译主表 表单省市
    public ProfileInf translateFromProvinceOrCity(List<ProfileInf> profileInfs) {
        ProfileInf profileInf = profileInfs.get(0);
        Map<String, String[]> idAndProvincesOrCitiesAndPid = this.getIdAndProvincesOrCitiesAndPid();
        String province = "";
        String message = "";
        String pid = "";
        String message1;
        if (CollUtil.isNotEmpty(idAndProvincesOrCitiesAndPid)) {
            if (profileInf.getRegisPlace() != null) {
                message = idAndProvincesOrCitiesAndPid.get(profileInf.getRegisPlace())[0];
                pid = idAndProvincesOrCitiesAndPid.get(profileInf.getRegisPlace())[1];
                province = message;
                if (!pid.equals("0")) {
                    message1 = idAndProvincesOrCitiesAndPid.get(pid)[0];
                    province = message1 + "/" + message;
                }
            }
        }

        profileInf.setRegisPlace(province);
        return profileInf;
    }

    //主表表单 查询已有按钮 点击 是 以后 生成一条客商类型的数据
    public ProfileTypeLevel generalTypeData(String name, String cs_flag) {
        ProfileInf profileInf = this.queryMainPageByCsName(name).get(0);
        String id = profileInf.getId();

        ProfileTypeLevel profileTypeLevel = new ProfileTypeLevel();

        CsType csType = CsType.informal;
        CsLevel csLevel = CsLevel.notYet;

        profileTypeLevel.setCsId(id);
        profileTypeLevel.setCsType(csType.getCode());
        profileTypeLevel.setCsLevel(csLevel.getDesc());
        profileTypeLevel.setCsFlag(cs_flag);

        return profileTypeLevel;
    }

    //将选中的数据类型转为黑名单
    public String turnToBlacklist(String id, String cs_flag) {

        String csTypeId = this.queryByCsIdAndCsFlag(id, cs_flag);

        ProfileTypeLevel profileTypeLevel = profileTypeLevelService.getById(csTypeId);

        CsType blacklist = CsType.blacklist;
        CsLevel noYet = CsLevel.notYet;

        if (!profileTypeLevel.getCsType().equals(blacklist.getCode())) {
            profileTypeLevel.setCsLevel(noYet.getDesc());
            profileTypeLevel.setCsType(blacklist.getCode());
            profileTypeLevel.setEffectiveTime(new Date());
            profileTypeLevelService.saveOrUpdate(profileTypeLevel);
            //将数据插入履历表
            this.insertResumeWhenClickButton(id);
            return "操作成功";
        }

        return "已经为黑名单";

    }

    //将选中的数据类型由黑名单转为非正式
    public String removeBlacklist(String id, String cs_flag) {

        String csTypeId = this.queryByCsIdAndCsFlag(id, cs_flag);

        ProfileTypeLevel profileTypeLevel = profileTypeLevelService.getById(csTypeId);

        CsType formal = CsType.formal;
        CsType informal = CsType.informal;
        CsType blacklist = CsType.blacklist;


        if (profileTypeLevel.getCsType().equals(blacklist.getCode())) {
            profileTypeLevel.setCsType(informal.getCode());
            profileTypeLevel.setEffectiveTime(new Date());
            profileTypeLevelService.saveOrUpdate(profileTypeLevel);

            //将数据插入履历表
            this.insertResumeWhenClickButton(id);

            return "操作成功";
        }

        return "该数据不为黑名单";

    }

    //履历表主表以及客商等级表插入数据
    public void insertResume(ProfileInf profileInf) {
        //设置id
        String resumeId = this.generalId();
        profileInf.setResumeId(resumeId);
        //履历主表插入数据
        profileinfmapper.insertMainPageResume(profileInf);
        //通过cs_id查询客商等级表数据
        List<ProfileTypeLevel> profileTypeLevels = profileTypeLevelMapper.queryCsByCsId(profileInf.getId());

        if (CollUtil.isNotEmpty(profileTypeLevels)) {
            for (ProfileTypeLevel profileTypeLevel :profileTypeLevels){
                //设置id
                profileTypeLevel.setResumeId(profileTypeLevelService.generalId());
                //获取cs_id
                profileTypeLevel.setResumeCsId(resumeId);
                //履历客商等级表插入数据
                profileTypeLevelMapper.insertCsTypeResume(profileTypeLevel);
            }
        }

        List<ProfileBank> profileBanks = profileBankMapper.queryBankByCsId(profileInf.getId());

        if (CollUtil.isNotEmpty(profileBanks)) {
            for (ProfileBank profileBank : profileBanks) {
                profileBank.setResumeId(profileBankService.generalId());
                profileBank.setResumeCsId(resumeId);
                profileBankMapper.insertBankResume(profileBank);
            }
        }
    }

    //操作按钮（转黑，解除黑，转正，转非正,删除）时 将数据插入 履历表
    public void insertResumeWhenClickButton(String id) {
        ProfileInf profileInf = this.getById(id);
        this.insertResume(profileInf);
    }
    
    /**
     * 根据客商主键ID获取客商基本信息、银行账户、受托人等信息
     * @author huangheng
     * @param id
     * @return
     */
    public ProfileInfo getProfileInfoByMainId(String id) {
    	return profileInfMapper.getProfileInfoByMainId(id,TenantContext.getTenant());
    }


}
