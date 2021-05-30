package com.dongxin.erp.cs.mapper;

import java.util.List;
import com.dongxin.erp.cs.entity.ProfileQualifi;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 供应商资质信息
 * @Author: jeecg-boot
 * @Date:   2020-11-10
 * @Version: V1.0
 */
public interface ProfileQualifiMapper extends BaseMapper<ProfileQualifi> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<ProfileQualifi> selectByMainId(@Param("mainId") String mainId);

}
