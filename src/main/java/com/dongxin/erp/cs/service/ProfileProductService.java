package com.dongxin.erp.cs.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.dongxin.erp.cs.entity.ProfileProduct;
import com.dongxin.erp.cs.mapper.ProfileProductMapper;
import org.jeecg.common.system.base.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 客商经营品种表
 * @Author: jeecg-boot
 * @Date: 2020-11-10
 * @Version: V1.0
 */
@Service
public class ProfileProductService extends BaseService<ProfileProductMapper, ProfileProduct> {

    @Autowired
    ProfileProductMapper profileproductmapper;
    @Autowired
    ProfileInfService profileInfService;

    ProfileProduct profileproduct = new ProfileProduct();

    public List<ProfileProduct> selectByMainId(String mainId) {
        return baseMapper.selectByMainId(mainId);
    }

    public void deleteBatchByMainId(String id) {
        List<ProfileProduct> details = selectByMainId(id);
        for (ProfileProduct detail : details) {
            logicDelete(detail);
        }
    }

    //这个方法为返回  id 和 名称 的方法 经营大类 经营品种
    public Map<String,String> translateProduct() {
        List<Map<String, String>> idAndNameOfList = profileproductmapper.translateProduct();
        Map<String, String> idAndNameOfMap = new HashMap<>();
        if (CollUtil.isNotEmpty(idAndNameOfList)) {
            for (Map idAndNameOfMap1 : idAndNameOfList) {
                idAndNameOfMap.put(MapUtil.getStr(idAndNameOfMap1, "id"), MapUtil.getStr(idAndNameOfMap1, "name"));
            }
        }
        return idAndNameOfMap;
    }

    //通过经营大类以及经营品种的id 拿到名称 并设置对应字段
    public List<ProfileProduct> getNameById (List<ProfileProduct> profileProducts){

        Map<String, String> idAndNameOfMap = this.translateProduct();

        for (ProfileProduct profileProduct1 : profileProducts) {
            profileProduct1.setName(idAndNameOfMap.get(profileProduct1.getBusiProduct()));
            profileProduct1.setBusiVarietyName(idAndNameOfMap.get(profileProduct1.getBusiVariety()));
        }

        return profileProducts;
    }

    //翻译省市
    public List<ProfileProduct> translateProvinceOrCity(List<ProfileProduct> profileProducts){
        Map<String, String[]> idAndProvincesOrCitiesAndPid = profileInfService.getIdAndProvincesOrCitiesAndPid();

        if (CollUtil.isNotEmpty(idAndProvincesOrCitiesAndPid)) {
            for (ProfileProduct profileProduct1 : profileProducts) {
                if (profileProduct1.getBusiPlace() != null) {
                    String provinceOrCity = idAndProvincesOrCitiesAndPid.get(profileProduct1.getBusiPlace())[0];
                    String pid = idAndProvincesOrCitiesAndPid.get(profileProduct1.getBusiPlace())[1];
                    if (profileProduct1.getBusiPlace() != null && pid.equals("0")) {
                        profileProduct1.setProvincesAndCities(provinceOrCity);
                    } else {
                        profileProduct1.setProvincesAndCities(idAndProvincesOrCitiesAndPid.get(pid)[0] + "/" + provinceOrCity);
                    }
                }
            }
        }
        return profileProducts;
    }

    //这个方法为拿到经营大类的方法
    public Map<String,String>getIdAndPidAndName() {

        Map<String, String> idAndPidOfMaps = new HashMap<>();

        List<Map<String, String>> idAndPidAndNameOfLists = this.profileproductmapper.getIdAndPidAndName();

        for (Map map : idAndPidAndNameOfLists) {
            idAndPidOfMaps.put(MapUtil.getStr(map,"id"),MapUtil.getStr(map,"pid"));
        }


        return idAndPidOfMaps;
    }

    //这个方法是在经营品种种添加一条信息时  根据经营品种 找到经营大类
    public ProfileProduct findBusiVariety(ProfileProduct profileProduct){

        Map<String, String> idAndPidOfMaps = this.getIdAndPidAndName();
        String busi_no = profileProduct.getBusiVariety();
        String busi_pid = MapUtil.getStr(idAndPidOfMaps, busi_no);
        int num = 0;

        while (!busi_pid.equals("0")) {
            if (num == 0) {
                busi_no = busi_pid;
            }
            busi_pid = MapUtil.getStr(idAndPidOfMaps, busi_pid);
            if (!busi_pid.equals("0")) {
                busi_no = busi_pid;
                num = 1;
            }
        }

        profileProduct.setBusiProduct(busi_no);



        return profileProduct;
    }


}
