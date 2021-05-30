package com.dongxin.erp.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.dongxin.erp.bm.entity.BiddingInf;
import com.dongxin.erp.cs.entity.ProfileInf;
import com.dongxin.erp.enums.BiddingType;
import com.dongxin.erp.enums.PayMode;
import com.dongxin.erp.enums.SerialNoEnum;
import com.dongxin.erp.fm.entity.PayInf;
import com.dongxin.erp.mm.entity.Contract;
import com.dongxin.erp.mm.entity.Order;
import com.dongxin.erp.mm.entity.Plan;
import com.dongxin.erp.mm.entity.Settle;
import com.dongxin.erp.rule.SerialNoUtil;
import com.dongxin.erp.sm.entity.MatlInOrder;
import com.dongxin.erp.sm.entity.MatlMoveOrder;
import com.dongxin.erp.sm.entity.MatlOutOrder;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

@Component
public class MybatisConfig implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        if (metaObject.getOriginalObject() instanceof MatlInOrder) {
            setFieldValByName("matlInNumber", SerialNoUtil.getSerialNo(SerialNoEnum.SM_IN_RULE), metaObject);
        }
        if(metaObject.getOriginalObject() instanceof MatlOutOrder){
            setFieldValByName("matlOutNumber", SerialNoUtil.getSerialNo(SerialNoEnum.SM_OUT_RULE), metaObject);
        }
        if(metaObject.getOriginalObject() instanceof MatlMoveOrder){
            setFieldValByName("matlMoveNumber", SerialNoUtil.getSerialNo(SerialNoEnum.SM_MV_RULE), metaObject);
        }
        if (metaObject.getOriginalObject() instanceof Plan) {
            setFieldValByName("planNumber", SerialNoUtil.getSerialNo(SerialNoEnum.MM_PLAN_RULE), metaObject);
        }
        if(metaObject.getOriginalObject() instanceof Contract){
            setFieldValByName("contractNumber", SerialNoUtil.getSerialNo(SerialNoEnum.MM_CONTRACT_RULE), metaObject);
        }
        if(metaObject.getOriginalObject() instanceof Order){
            setFieldValByName("orderNumber", SerialNoUtil.getSerialNo(SerialNoEnum.MM_ORDER_RULE), metaObject);
        }
        if(metaObject.getOriginalObject() instanceof Settle){
            setFieldValByName("settleNumber", SerialNoUtil.getSerialNo(SerialNoEnum.MM_SETTLE_RULE), metaObject);
        }

        //付款单编号
        if(metaObject.getOriginalObject() instanceof PayInf){
            if(((PayInf) metaObject.getOriginalObject()).getPayMode().equals(PayMode.PAYMODE1.getCode())){
                setFieldValByName("code", SerialNoUtil.getSerialNo(SerialNoEnum.FM_PAY1_RULE), metaObject);

            }else {
                setFieldValByName("code", SerialNoUtil.getSerialNo(SerialNoEnum.FM_PAY2_RULE), metaObject);
            }
        }
        //招投标编号
        if(metaObject.getOriginalObject() instanceof BiddingInf){
            if(((BiddingInf) metaObject.getOriginalObject()).getBiddingNo() ==null){
                if(((BiddingInf) metaObject.getOriginalObject()).getBiddingType().equals(BiddingType.BIDPENDING.getCode())){
               setFieldValByName("biddingNo", SerialNoUtil.getSerialNo(SerialNoEnum.BM_INVITE_RULE), metaObject);
            }else {
                    setFieldValByName("biddingNo", SerialNoUtil.getSerialNo(SerialNoEnum.BM_BIDDING_RULE), metaObject);
                }
           }
        }

        //客商代码
        if(metaObject.getOriginalObject() instanceof ProfileInf){
            setFieldValByName("csCode", SerialNoUtil.getSerialNo(SerialNoEnum.CS_INFO_RULE), metaObject);
        }

    }

    @Override
    public void updateFill(MetaObject metaObject) {

    }
}
