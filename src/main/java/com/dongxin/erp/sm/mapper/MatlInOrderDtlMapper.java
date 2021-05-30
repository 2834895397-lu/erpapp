package com.dongxin.erp.sm.mapper;

import java.util.List;
import com.dongxin.erp.sm.entity.MatlInOrderDtl;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dongxin.erp.sm.entity.MatlOutOrderDtl;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * @Description: 收货单明细表
 * @Author: jeecg-boot
 * @Date:   2020-11-11
 * @Version: V1.0
 */
@Component
public interface MatlInOrderDtlMapper extends BaseMapper<MatlInOrderDtl> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<MatlInOrderDtl> selectByMainId(@Param("mainId") String mainId, @Param("redFlag") String redFlag);

	void updateTmmSettleDtlIdById(String id);

	public List<MatlInOrderDtl> selectByOriginalIds(@Param("ids") List<String> ids);
}
