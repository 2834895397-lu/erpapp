package com.dongxin.erp.cs.service;

import cn.hutool.core.map.MapUtil;
import com.dongxin.erp.cs.entity.ProfileBelong;
import com.dongxin.erp.cs.mapper.ProfileBelongMapper;
import org.jeecg.common.system.base.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 客商归属信息表
 * @Author: jeecg-boot
 * @Date: 2020-11-10
 * @Version: V1.0
 */
@Service
public class ProfileBelongService extends BaseService<ProfileBelongMapper, ProfileBelong> {
    @Autowired
    ProfileBelongMapper profileBelongMapper;

    public List<ProfileBelong> selectByMainId(String mainId) {
        return baseMapper.selectByMainId(mainId);
    }

    public void deleteBatchByMainId(String id) {
        List<ProfileBelong> details = selectByMainId(id);
        for (ProfileBelong detail : details) {
            logicDelete(detail);
        }
    }

    //将从数据库中拿到的所有id，parent_id， id,depart_name放到map集合中 id作key ，parent_id，depart_name作value
    public Map<String,Map<String,String>> getDepartIdPidAndName() {

        Map <String,Map<String,String>> maps = new HashMap<>();

        Map<String, String> idAndNameOfMaps = new HashMap<>();
        Map<String, String> idAndPidOfMaps = new HashMap<>();

        List<Map<String, String>> idAndPidAndNameOfLists = this.profileBelongMapper.getDepartIdPidAndName();

        for (Map map : idAndPidAndNameOfLists) {
            idAndNameOfMaps.put(MapUtil.getStr(map,"id"),MapUtil.getStr(map,"depart_name"));
            idAndPidOfMaps.put(MapUtil.getStr(map,"id"),MapUtil.getStr(map,"parent_id"));

        }

        maps.put("depart_name",idAndNameOfMaps);
        maps.put("parent_id",idAndPidOfMaps);

        return maps;
    }

    //翻译 拼接 归属组织
    public List<ProfileBelong> translateGsCompany(List<ProfileBelong> profileBelongs){
        Map<String, String> idAndNameOfMaps = this.getDepartIdPidAndName().get("depart_name");
        Map<String, String> idAndPidOfMaps = this.getDepartIdPidAndName().get("parent_id");


        for (ProfileBelong belong : profileBelongs) {
            String gs_no = belong.getGsCompany();
            String gs_pid = MapUtil.getStr(idAndPidOfMaps, gs_no);
            String gs_name = MapUtil.getStr(idAndNameOfMaps, gs_no);
            int num =0;
            while (gs_pid != null) {
                if (num==0 && MapUtil.getStr(idAndNameOfMaps, gs_pid) != null){
                    gs_name = MapUtil.getStr(idAndNameOfMaps, gs_pid) + "/" + gs_name;
                }
                gs_pid = MapUtil.getStr(idAndPidOfMaps, gs_pid);
                if (MapUtil.getStr(idAndNameOfMaps, gs_pid) != null) {
                    gs_name = MapUtil.getStr(idAndNameOfMaps, gs_pid) + "/" + gs_name;
                    num=1;
                }
            }
            belong.setGsCompany(gs_name);
        }
        return profileBelongs;
    }



}
