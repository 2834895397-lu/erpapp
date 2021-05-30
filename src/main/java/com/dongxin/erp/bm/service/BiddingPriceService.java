package com.dongxin.erp.bm.service;

import cn.hutool.core.collection.CollectionUtil;
import com.dongxin.erp.bm.entity.BiddingPrice;
import com.dongxin.erp.bm.entity.CompanyOffer;
import com.dongxin.erp.bm.mapper.BiddingPriceMapper;
import org.jeecg.common.system.base.service.BaseService;
import org.jeecg.common.util.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description: 招标报价表
 * @Author: jeecg-boot
 * @Date: 2020-11-25
 * @Version: V1.0
 */
@Service
public class BiddingPriceService extends BaseService<BiddingPriceMapper, BiddingPrice> {


    public List<BiddingPrice> CompanyOfferToBiddingPrice(List<CompanyOffer> companyOffers,String companyId, String date) throws ParseException {

        ArrayList<BiddingPrice> list = new ArrayList<>();
        for (CompanyOffer companyOffer : companyOffers) {
            BiddingPrice biddingPrice = new BiddingPrice();
            BeanUtils.copyProperties(companyOffer, biddingPrice);
            biddingPrice.setBiddingEnterpriseId(companyId);
            if("".equals(date)){
                date = DateUtils.getDate("yyyy-MM-dd");
            }
            biddingPrice.setOfferDate(new SimpleDateFormat("yyyy-MM-dd").parse(date));
            list.add(biddingPrice);
        }

        return list;
    }

    @Transactional
    public Boolean saveBiddingPrice(List<CompanyOffer> companyOffers, String companyId, String date) throws ParseException {

        //查找companyId 下的报价 修改del_flag=1

        List<BiddingPrice> biddingPrices = CompanyOfferToBiddingPrice(companyOffers,companyId, date);
        //修改前的id设置为null,重新生成id
        List<String> ids = new ArrayList<>();
        for (BiddingPrice biddingPrice : biddingPrices) {
            if(biddingPrice.getId() != null){
                ids.add(biddingPrice.getId());
            }
            biddingPrice.setId(null);
        }

        if(ids.size() != 0){
            removeByIds(ids);
        }
        boolean b = saveOrUpdateBatch(biddingPrices);
        return b;
    }

}
