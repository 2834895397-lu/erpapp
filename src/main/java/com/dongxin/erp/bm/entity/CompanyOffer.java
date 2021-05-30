package com.dongxin.erp.bm.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.Date;

@Data
public class CompanyOffer {
    private String id;
    private String biddingDetailId;
    private String biddingEnterpriseId;
    private String materielNo;
    private String materielName;
    private String measureNum;
    private String measureUnit;
    private Double offerPrice;
    private  Double offerNum;
    private Date offerDate;
    @TableField(exist=false)
    private java.lang.String  MeasureUnitName;

}
