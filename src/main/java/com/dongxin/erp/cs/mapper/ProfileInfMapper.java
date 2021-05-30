package com.dongxin.erp.cs.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dongxin.erp.cs.entity.ProfileInf;
import com.dongxin.erp.cs.entity.ProfileInfo;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @Description: 客商基础信息
 * @Author: jeecg-boot
 * @Date: 2020-11-10
 * @Version: V1.0
 */
@Repository
public interface ProfileInfMapper extends BaseMapper<ProfileInf> {

    List<Map<String, String>> getIdAndProvincesOrCitiesAndPid();

    List<ProfileInf> selectCs(String cs_flag, String cs_code, String regis_place,
                              String cs_type, String cs_level,String cs_name,String tenant_id);

    Long settleMaxNum();
    Long settleMaxId();

   /* Map queryBusiProduct(String pid);*/

    String queryCsName(String cs_id);

    String queryId(String id);

    List<Map> queryAllMaterial();

    //根据cs_id 以及cs_flag 删除客商类型数据
    String queryByCsIdAndCsFlag(String cs_id, String cs_flag);

    //提供 给其他业务的客商查询，输出主表ID、客商CODE、客商名称 以及其他
    List<ProfileInf> provideMessage(String cs_code,String cs_name,String tenant_id);

    //根据数据的客商名称信息模糊查询  客商表的信息
    List<ProfileInf> fuzzyQueryByCsName(String cs_name,String tenant_id);

    //履历表主表插入数据
    void insertMainPageResume(@Param("profileInf") ProfileInf profileInf);
    
	//根据客商主键ID获取客商基本信息、银行账户、受托人等信息
	ProfileInfo getProfileInfoByMainId(@Param("mainId") String mainId,@Param("tenantId") String tenantId);


}
