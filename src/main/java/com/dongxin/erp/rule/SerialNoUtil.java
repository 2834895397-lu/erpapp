package com.dongxin.erp.rule;

import com.alibaba.fastjson.JSONObject;
import com.dongxin.erp.enums.SerialNoEnum;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.util.FillRuleUtil;

/**
 * @author ：melon
 * @date ：Created in 2020/12/17 13:07
 */
public class SerialNoUtil {

    //根据规则code生成流水号
    public static String getSerialNo(SerialNoEnum serialNoEnum) {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("biz_code", serialNoEnum.getBizCode());
        Object id = FillRuleUtil.executeRule(serialNoEnum.getBizCode(), jsonObject);
        if (null == id) {
            throw new JeecgBootException("流水号生成失败");
        }

        return id.toString();
    }
}
