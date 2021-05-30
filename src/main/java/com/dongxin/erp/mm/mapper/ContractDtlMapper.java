package com.dongxin.erp.mm.mapper;

import java.util.List;
import java.util.Map;

import com.dongxin.erp.mm.entity.ContractDtl;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @Description: 采购合同明细表
 * @Author: jeecg-boot
 * @Date:   2020-11-18
 * @Version: V1.0
 */
@Repository
public interface ContractDtlMapper extends BaseMapper<ContractDtl> {

	boolean deleteByMainId(@Param("mainId") String mainId);
    
	List<ContractDtl> selectByMainId(@Param("mainId") String mainId);

	List<Map<String,String>> selectPlanNumber();

	List<Map<String,String>> checkMessage();

	List<ContractDtl> selectPrice(String id);
}
