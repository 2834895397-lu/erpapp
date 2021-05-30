package com.dongxin.erp.bm.mapper;

		import java.util.Date;
		import java.util.List;
		import java.util.Map;

		import com.dongxin.erp.bm.entity.BiddingDtl;
		import com.baomidou.mybatisplus.core.mapper.BaseMapper;
		import com.dongxin.erp.bm.entity.BiddingEnterprise;
		import com.dongxin.erp.bm.entity.CompanyOffer;
		import org.apache.ibatis.annotations.Param;

/**
 * @Description: 招标明细信息表
 * @Author: jeecg-boot
 * @Date:   2020-11-25
 * @Version: V1.0
 */
public interface BiddingDtlMapper extends BaseMapper<BiddingDtl> {

	boolean deleteByMainId(@Param("mainId") String mainId);

	List<BiddingDtl> selectByMainId(@Param("mainId") String mainId,@Param("tenant") String tenant);

	List<CompanyOffer> selectDtl(@Param("biddingEnterprise") BiddingEnterprise biddingEnterprise);

	List <Map<String,String>> selectUnit(@Param("tenant") String tenant);

	List<BiddingDtl> selctId(@Param("id") String id, @Param("beginTime") Date beginTime,
							 @Param("endTime") Date endTime);

	BiddingDtl selectOneByBiddingInfIdAndMaterielNo(@Param("biddingInfId") String biddingInfId,
			@Param("materielNo") String materielNo,@Param("tenant") String tenant);

	BiddingDtl getBiddingDtlById(@Param("id") String id,@Param("tenant") String tenant);

	void updateDelFlagById(@Param("id") String id,@Param("tenant") String tenant);
}
