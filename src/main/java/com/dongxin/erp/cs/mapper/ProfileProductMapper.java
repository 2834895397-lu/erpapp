package com.dongxin.erp.cs.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dongxin.erp.cs.entity.ProfileProduct;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @Description: 客商经营品种表
 * @Author: jeecg-boot
 * @Date: 2020-11-10
 * @Version: V1.0
 */
@Repository
public interface ProfileProductMapper extends BaseMapper<ProfileProduct> {

    boolean deleteByMainId(@Param("mainId") String mainId);

    List<ProfileProduct> selectByMainId(@Param("mainId") String mainId);

    List<Map<String, String>> translateProduct();

    /*List<BusiProduct>queryBusiProduct(String id);*/
    List<Map<String,String>> getIdAndPidAndName();

}
