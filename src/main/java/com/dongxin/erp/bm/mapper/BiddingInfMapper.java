package com.dongxin.erp.bm.mapper;

import java.util.Date;
import java.util.List;

import com.dongxin.erp.bm.entity.CompanyOffer;
import org.apache.ibatis.annotations.Param;
import com.dongxin.erp.bm.entity.BiddingInf;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 招标信息表
 * @Author: jeecg-boot
 * @Date:   2020-11-25
 * @Version: V1.0
 */
public interface BiddingInfMapper extends BaseMapper<BiddingInf> {
  String selectUser(String id);

  /*List<BiddingInf> selectBiddingInf(@Param("biddingTitle") String biddingTitle,  @Param("biddingDepaterment") String biddingDepaterment);*/


  List<BiddingInf> biddingQuery( @Param("beginTime") Date beginTime,@Param("endTime") Date endTime,@Param("bTime") Date bTime, @Param("eTime") Date eTime,@Param("biddingTitle") String biddingTitle,@Param("biddingDepaterment") String biddingDepaterment,@Param("tenant") String tenant);


}
