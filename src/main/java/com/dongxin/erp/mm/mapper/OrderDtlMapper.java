package com.dongxin.erp.mm.mapper;

import java.util.List;
import java.util.Map;

import com.dongxin.erp.mm.entity.OrderDtl;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @Description: 采购订单明细表
 * @Author: jeecg-boot
 * @Date:   2020-11-18
 * @Version: V1.0
 */
@Repository
public interface OrderDtlMapper extends BaseMapper<OrderDtl> {

	boolean deleteByMainId(@Param("mainId") String mainId);
    
	List<OrderDtl> selectByMainId(@Param("mainId") String mainId);

	List<Map<String,String>> selectContractNumber();
}
