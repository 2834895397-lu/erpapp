package com.dongxin.erp.cs.mapper;

import java.util.List;
import java.util.Map;

import com.dongxin.erp.cs.entity.ProfileBelong;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @Description: 客商归属信息表
 * @Author: jeecg-boot
 * @Date:   2020-11-10
 * @Version: V1.0
 */
@Repository
public interface ProfileBelongMapper extends BaseMapper<ProfileBelong> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<ProfileBelong> selectByMainId(@Param("mainId") String mainId);

	/*List<ProfileBelong> queryName();*/

	//拿到部门表的所有id，parent_id，depart_name

	List <Map<String,String>> getDepartIdPidAndName();
}
