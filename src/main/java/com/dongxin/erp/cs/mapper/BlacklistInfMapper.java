package com.dongxin.erp.cs.mapper;

import java.util.List;
import java.util.Map;

import com.dongxin.erp.cs.entity.ProfileTypeLevel;
import org.apache.ibatis.annotations.Param;
import com.dongxin.erp.cs.entity.BlacklistInf;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * @Description: 黑名单管理
 * @Author: jeecg-boot
 * @Date:   2020-11-17
 * @Version: V1.0
 */
@Repository
public interface BlacklistInfMapper extends BaseMapper<BlacklistInf> {

    /*public List<BlacklistInf> queryCsIdAndCsFlag();*/
    List<Map> queryBlacklist();

    String queryCs(String id,String cs_flag);

    /*String queryBcs(String id);*/

    List<BlacklistInf>selectByIds(List<String> ids);

    //根据黑名单表的id和cs_id查询客商类型的id
    ProfileTypeLevel queryTypeLevel(String id,String cs_flag);

}
