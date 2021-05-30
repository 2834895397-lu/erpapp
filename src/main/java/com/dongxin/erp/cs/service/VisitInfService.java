package com.dongxin.erp.cs.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.dongxin.erp.cs.entity.ProfileInf;
import com.dongxin.erp.cs.entity.VisitInf;
import com.dongxin.erp.cs.mapper.ProfileInfMapper;
import com.dongxin.erp.cs.mapper.VisitInfMapper;
import org.jeecg.common.system.base.service.BaseService;
import org.jeecg.config.mybatis.TenantContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description: 顾客拜访登记
 * @Author: jeecg-boot
 * @Date: 2020-11-10
 * @Version: V1.0
 */
@Service
public class VisitInfService extends BaseService<VisitInfMapper, VisitInf> {

    @Autowired
    ProfileInfService profileInfService;
    @Autowired
    VisitInfMapper visitInfMapper;
    @Autowired
    ProfileInfMapper profileInfMapper;

    /*//拿到cs_id的值后将cs_id重新设置为null
    public String getCsName(VisitInf visitInf){

        return visitInf.getCsId();
    }*/

    //以下为翻译拜访省市
    public void tranlateProvinceOrCity(List<VisitInf> visitInfs) {
        Map<String, String[]> idAndProvincesOrCitiesAndPid = profileInfService.getIdAndProvincesOrCitiesAndPid();

        if (CollUtil.isNotEmpty(idAndProvincesOrCitiesAndPid)) {
            for (VisitInf visitInf : visitInfs) {
                if (visitInf.getVisPlace() != null) {
                    String provinceOrCity = idAndProvincesOrCitiesAndPid.get(visitInf.getVisPlace())[0];
                    String pid = idAndProvincesOrCitiesAndPid.get(visitInf.getVisPlace())[1];
                    if (visitInf.getVisPlace() != null && pid.equals("0")) {
                        visitInf.setProvincesAndCities(provinceOrCity);
                    } else {
                        visitInf.setProvincesAndCities(idAndProvincesOrCitiesAndPid.get(pid)[0] + "/" + provinceOrCity);
                    }
                }
            }
        }
    }

    //拜访时间范围查询
    public List<VisitInf> visitTimeRangeQuery(VisitInf visitInf, List<VisitInf> visitInfs) {

        if (visitInf.getEndTime() != null || visitInf.getBeginTime() != null) {

            return visitInfMapper.visitTimeRangeQuery(visitInf.getBeginTime(), visitInf.getEndTime(),TenantContext.getTenant());
        }

        return visitInfs;

    }

    //客商名称模糊查询
    public List<VisitInf> fuzzyQueryByCsName(String cs_id, List<VisitInf> visitInfs) {

        //如果cs_id 不为null 则执行
        if (cs_id != null) {

            //通过cs_id 模糊查询主表中的数据    这么做的目的是因为拜访表中的客商名称是存id显示名称的
            List<ProfileInf> profileInfs = profileInfMapper.fuzzyQueryByCsName(cs_id, TenantContext.getTenant());
            List<String> idList = new ArrayList<>();
            List<VisitInf> visitInfLists = new ArrayList<>();

            //将拿到的所有数据的id 放到一个List集合中
            for (ProfileInf profileInf :profileInfs){
                idList.add(profileInf.getId());
            }

            //如何拜访表的数据的cs_id在上面的id的集合中存在则将拜访表的整条数据存到集合中
            for (VisitInf visitInf : visitInfs) {
                if (idList.contains(visitInf.getCsId())){
                    visitInfLists.add(visitInf);
                }
            }

            return visitInfLists;
        }

        //如果cs_id为null则直接返回原来表单的数据
        return visitInfs;

    }

}
