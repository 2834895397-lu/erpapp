package com.dongxin.erp.ps1.mapper;

import java.util.List;
import com.dongxin.erp.ps1.entity.TpsContractFund;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 合同资金表
 * @Author: jeecg-boot
 * @Date:   2021-01-22
 * @Version: V1.0
 */
public interface TpsContractFundMapper extends BaseMapper<TpsContractFund> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<TpsContractFund> selectByMainId(@Param("mainId") String mainId);

}
