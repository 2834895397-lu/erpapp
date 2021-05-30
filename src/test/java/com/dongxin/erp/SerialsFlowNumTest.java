package com.dongxin.erp;

import com.dongxin.erp.rule.RuleCode;
import org.jeecg.common.util.FillRuleUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 自增流水号生成规则测试
 * @author Succy
 *
 * create on 2020/12/16
 */
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class SerialsFlowNumTest {
    @Test
    public void testGetNextId() {
        Object nextId = FillRuleUtil.executeRule(RuleCode.PAYMENT_RULE_CODE, RuleCode.PAYMENT_FORM_DATA);
        System.out.println("付款订单编号： " + nextId);
        nextId = FillRuleUtil.executeRule(RuleCode.PURCHASE_RULE_CODE, RuleCode.PURCHASE_FORM_DATA);
        System.out.println("采购订单编号： " + nextId);
    }
}
