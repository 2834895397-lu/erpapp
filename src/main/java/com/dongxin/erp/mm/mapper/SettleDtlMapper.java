package com.dongxin.erp.mm.mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.dongxin.erp.mm.entity.SettleDtl;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * @Description: 采购结算明细表
 * @Author: jeecg-boot
 * @Date:   2020-11-23
 * @Version: V1.0
 */
@Component
public interface SettleDtlMapper extends BaseMapper<SettleDtl> {

	//boolean deleteByMainId(@Param("mainId") String mainId);

	List<SettleDtl> selectByMainId(@Param("mainId") String mainId,@Param("tenant") String tenant);

	List<Map<String,String>> selectContractNumber();
	
	SettleDtl getSettleDtlById(@Param("id") String id,@Param("tenant") String tenant);
	
	void updateDelFlagById(@Param("id") String id,@Param("tenant") String tenant);
	
	Date getVoucherTime(@Param("id") String id);
}
