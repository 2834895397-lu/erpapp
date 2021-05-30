package com.dongxin.erp.sm.mapper;

import java.util.List;

import com.dongxin.erp.sm.entity.MatlInOrderDtl;
import com.dongxin.erp.sm.entity.MatlOutOrderDtl;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * @Description: 领用单明细表
 * @Author: jeecg-boot
 * @Date:   2020-11-10
 * @Version: V1.0
 */
@Component
public interface MatlOutOrderDtlMapper extends BaseMapper<MatlOutOrderDtl> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<MatlOutOrderDtl> selectByMainId(@Param("mainId") String mainId  , @Param("redFlag") String redFlag);

	public List<MatlOutOrderDtl> selectByOriginalIds(@Param("ids") List<String> ids);
}
