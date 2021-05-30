package com.dongxin.erp.sm.mapper;

import java.util.List;

import com.dongxin.erp.sm.entity.MatlInOrderDtl;
import com.dongxin.erp.sm.entity.MatlMoveOrderDtl;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dongxin.erp.sm.entity.MatlOutOrderDtl;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * @Description: 移库单明细表
 * @Author: jeecg-boot
 * @Date:   2020-11-10
 * @Version: V1.0
 */
@Component
public interface MatlMoveOrderDtlMapper extends BaseMapper<MatlMoveOrderDtl> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<MatlMoveOrderDtl> selectByMainId(@Param("mainId") String mainId  , @Param("redFlag") String redFlag);

	public List<MatlMoveOrderDtl> selectByOriginalIds(@Param("ids") List<String> ids);
}
