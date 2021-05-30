package com.dongxin.erp;

import org.junit.Test;

import java.text.DecimalFormat;

/**
 * @author Succy
 * create on 2020/12/9
 */
public class StringFormatTest {
    @Test
    public void testFormat() {
        String format = String.format("%04d", 99);
        System.out.println(format);

        DecimalFormat f = new DecimalFormat("0000000000");
        System.out.println(f.format(1));
    }
}
