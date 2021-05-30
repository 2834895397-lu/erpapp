package com.dongxin.erp.mm.mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.dongxin.erp.bm.entity.BiddingDtl;
import com.dongxin.erp.mm.entity.PlanDtl;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dongxin.erp.mm.entity.PurchaseDtl;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 采购申请明细表
 * @Author: jeecg-boot
 * @Date:   2020-11-18
 * @Version: V1.0
 */
public interface PlanDtlMapper extends BaseMapper<PlanDtl> {

	boolean deleteByMainId(@Param("mainId") String mainId);
    
	List<PlanDtl> selectByMainId(@Param("mainId") String mainId);

	List<BiddingDtl> selectPurchaseDetail(@Param("planNo") String planNo,@Param("tenant") String tenant);

    /*List<Map<String, String>> selectMaterial();*/
/*
	List<Map<String, String>> selectUnit();*/
}
