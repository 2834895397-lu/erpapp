package com.dongxin.erp.cs.mapper;

import java.util.List;
import com.dongxin.erp.cs.entity.ProfilePoa;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 客商委托书
 * @Author: jeecg-boot
 * @Date:   2020-11-10
 * @Version: V1.0
 */
public interface ProfilePoaMapper extends BaseMapper<ProfilePoa> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<ProfilePoa> selectByMainId(@Param("mainId") String mainId);

}
