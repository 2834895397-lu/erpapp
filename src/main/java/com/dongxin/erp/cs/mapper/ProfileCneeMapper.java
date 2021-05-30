package com.dongxin.erp.cs.mapper;

import java.util.List;
import com.dongxin.erp.cs.entity.ProfileCnee;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 客户收货人
 * @Author: jeecg-boot
 * @Date:   2020-11-10
 * @Version: V1.0
 */
public interface ProfileCneeMapper extends BaseMapper<ProfileCnee> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<ProfileCnee> selectByMainId(@Param("mainId") String mainId);

}
