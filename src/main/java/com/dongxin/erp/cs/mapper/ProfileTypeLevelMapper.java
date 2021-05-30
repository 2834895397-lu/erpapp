package com.dongxin.erp.cs.mapper;

import java.util.List;
import com.dongxin.erp.cs.entity.ProfileTypeLevel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dongxin.erp.cs.vo.ProfileTypeLevelHis;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @Description: 客商类型等级表
 * @Author: jeecg-boot
 * @Date:   2020-11-10
 * @Version: V1.0
 */
@Repository
public interface ProfileTypeLevelMapper extends BaseMapper<ProfileTypeLevel> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<ProfileTypeLevel> selectByMainId(@Param("mainId") String mainId);

	Long settleMaxId();

	//履历表客商等级表添加数据插入数据
	void insertCsTypeResume(@Param("profileTypeLevel") ProfileTypeLevel profileTypeLevel);

	//通过cs_id查询数据
	List<ProfileTypeLevel> queryCsByCsId(String cs_id);

	//查询客商等级履历表
	List<ProfileTypeLevelHis> selectResume(String cs_id,String cs_flag,String tenant_id);

}
