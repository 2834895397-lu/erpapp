package com.dongxin.erp.chart.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dongxin.erp.bm.entity.BiddingInf;
import com.dongxin.erp.chart.vo.Chart;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author huangzy
 * @date 2020年12月22日 10:27:53
 *
 */
public interface BmChartMapper extends BaseMapper<Chart> {


    public List<Chart> getMultiBarBiddingPrice(@Param("id") String id,@Param("tenant") String tenant);


    public List<Chart> getMultiBarBiddingAmount(@Param("id") String id);


    public List<Chart> getMultiBarBiddingNum(@Param("id") String id);

    public List<Chart> getMultiBarBiddingOfferNum(@Param("id") String id);

}
