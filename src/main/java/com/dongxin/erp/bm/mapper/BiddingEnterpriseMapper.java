package com.dongxin.erp.bm.mapper;

import java.util.List;
import com.dongxin.erp.bm.entity.BiddingEnterprise;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dongxin.erp.bm.entity.BiddingPrice;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 招标企业信息表
 * @Author: jeecg-boot
 * @Date: 2020-11-25
 * @Version: V1.0
 */
public interface BiddingEnterpriseMapper extends BaseMapper<BiddingEnterprise> {

	boolean deleteByMainId(@Param("mainId") String mainId);

	List<BiddingEnterprise> selectByMainId(@Param("mainId") String mainId);

	List<BiddingPrice> selectBiddingPrice(String id,String tenant);

	BiddingEnterprise getBiddingEnterpriseById(@Param("id") String id,@Param("tenant") String tenant);

	void updateDelFlagById(@Param("id") String id,@Param("tenant") String tenant);
}
