package com.dongxin.erp.fm.mapper;

import java.util.List;
import com.dongxin.erp.fm.entity.PayDtl;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 财务付款明细信息
 * @Author: jeecg-boot
 * @Date:   2020-11-18
 * @Version: V1.0
 */
public interface PayDtlMapper extends BaseMapper<PayDtl> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<PayDtl> selectByMainId(@Param("mainId") String mainId);
}
