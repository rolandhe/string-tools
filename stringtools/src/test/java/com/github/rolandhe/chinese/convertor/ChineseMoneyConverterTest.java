package com.github.rolandhe.chinese.convertor;

import org.junit.Assert;
import org.junit.Test;

/**
 * ChineseMoneyConverter测试用例类
 *
 * @author rolandhe
 * @date 2020-12-10 21:01
 */
public class ChineseMoneyConverterTest {
    @Test
    public void testConvert() {
        String[][] dict = {
                {"0", "零元整"},
                {"1", "壹元整"},
                {"10", "壹拾元整"},
                {"11", "壹拾壹元整"},
                {"100", "壹佰元整"},
                {"101", "壹佰零壹元整"},
                {"110", "壹佰壹拾元整"},
                {"111", "壹佰壹拾壹元整"},
                {"1000", "壹仟元整"},
                {"1001", "壹仟零壹元整"},
                {"1010", "壹仟零壹拾元整"},
                {"1100", "壹仟壹佰元整"},
                {"1011", "壹仟零壹拾壹元整"},
                {"1101", "壹仟壹佰零壹元整"},
                {"1110", "壹仟壹佰壹拾元整"},
                {"10000", "壹万元整"},
                {"10001", "壹万零壹元整"},
                {"10010", "壹万零壹拾元整"},
                {"10100", "壹万零壹佰元整"},
                {"11000", "壹万壹仟元整"},
                {"11001", "壹万壹仟零壹元整"},
                {"10101", "壹万零壹佰零壹元整"},
                {"10111", "壹万零壹佰壹拾壹元整"},
                {"11011", "壹万壹仟零壹拾壹元整"},
                {"100000", "壹拾万元整"},
                {"100001", "壹拾万零壹元整"},
                {"110000", "壹拾壹万元整"},
                {"110001", "壹拾壹万零壹元整"},
                {"101001", "壹拾万壹仟零壹元整"},
                {"1001001", "壹佰万壹仟零壹元整"},
                {"1010001", "壹佰零壹万零壹元整"},
                {"1011001", "壹佰零壹万壹仟零壹元整"},
                {"10011001", "壹仟零壹万壹仟零壹元整"},
                {"10000000", "壹仟万元整"},
                {"10000001", "壹仟万零壹元整"},
                {"10000101", "壹仟万零壹佰零壹元整"},
                {"10000111", "壹仟万零壹佰壹拾壹元整"},
                {"100000000", "壹亿元整"},
                {"100000001", "壹亿零壹元整"},
                {"100001000", "壹亿零壹仟元整"},
                {"110000000", "壹亿壹仟万元整"},
                {"110000001", "壹亿壹仟万零壹元整"},
                {"1000000001", "壹拾亿零壹元整"},
                {"1010000001", "壹拾亿壹仟万零壹元整"},
                {"1010010001", "壹拾亿壹仟零壹万零壹元整"},
                {"1010011001", "壹拾亿壹仟零壹万壹仟零壹元整"},
                {"2010011001", "贰拾亿壹仟零壹万壹仟零壹元整"},
                {"10010011001", "壹佰亿壹仟零壹万壹仟零壹元整"},
                {"10001011001", "壹佰亿零壹佰零壹万壹仟零壹元整"},
                {"10000000000", "壹佰亿元整"},
                {"10000000001", "壹佰亿零壹元整"},
                {"10000010001", "壹佰亿零壹万零壹元整"},
                {"100000000000", "壹仟亿元整"},
                {"100000000001", "壹仟亿零壹元整"},
                {"100000000010", "壹仟亿零壹拾元整"},
                {"100010000010", "壹仟亿壹仟万零壹拾元整"},
                {"1234567890", "壹拾贰亿叁仟肆佰伍拾陆万柒仟捌佰玖拾元整"},
                {"100001010011", "壹仟亿零壹佰零壹万零壹拾壹元整"},
                {"999910000010", "玖仟玖佰玖拾玖亿壹仟万零壹拾元整"},
                {"999901000010", "玖仟玖佰玖拾玖亿零壹佰万零壹拾元整"},
                {"999901000010.11", "玖仟玖佰玖拾玖亿零壹佰万零壹拾元壹角壹分"},
                {"1000010000010", "壹万亿壹仟万零壹拾元整"},
                {"1000110000010", "壹万零壹亿壹仟万零壹拾元整"},
                {"1100110000010", "壹万壹仟零壹亿壹仟万零壹拾元整"},
                {"1100010000010", "壹万壹仟亿壹仟万零壹拾元整"},
                {"10100010000010", "壹拾万壹仟亿壹仟万零壹拾元整"},
                {"100100010000010", "壹佰万壹仟亿壹仟万零壹拾元整"},
                {"101100010000010", "壹佰零壹万壹仟亿壹仟万零壹拾元整"},
                {"110100010000010", "壹佰壹拾万壹仟亿壹仟万零壹拾元整"},
                {"1000100010000010", "壹仟万壹仟亿壹仟万零壹拾元整"},
                {"10000100010000010", "壹亿零壹仟亿壹仟万零壹拾元整"},
                {"100000100010000010", "壹拾亿零壹仟亿壹仟万零壹拾元整"},
                {"101000100010000010", "壹拾亿壹仟万壹仟亿壹仟万零壹拾元整"},
                {"1000000100010000010", "壹佰亿零壹仟亿壹仟万零壹拾元整"},
                {"1010000100010000010", "壹佰零壹亿零壹仟亿壹仟万零壹拾元整"},
                {"10010000100010000010", "壹仟零壹亿零壹仟亿壹仟万零壹拾元整"},
                {"10000000100010000010", "壹仟亿零壹仟亿壹仟万零壹拾元整"},
                {".01", "壹分"},
                {"1.01", "壹元壹分"},
                {"1.10", "壹元壹角"},
                {"1.11", "壹元壹角壹分"},
                {"0.0", "零元整"},
                {"0.", "零元整"},
                {".0", "零元整"},
                {"10000000100010000010.0", "壹仟亿零壹仟亿壹仟万零壹拾元整"},
                {"10000000100010000010.1", "壹仟亿零壹仟亿壹仟万零壹拾元壹角"},
                {"1234567890.35", "壹拾贰亿叁仟肆佰伍拾陆万柒仟捌佰玖拾元叁角伍分"},
        };
        for (String[] pair : dict) {
            String value = ChineseMoneyConverter.convert(pair[0]);
            Assert.assertEquals(pair[1], value);
        }
//        String[] array = {"a","b","c"};
//
//        try{
//            throwMsg();
//        }catch (RuntimeException e) {
//            e.printStackTrace();;
//        }

    }

    private void throwMsg(){
        throw new RuntimeException("message");
    }

    @Test(expected = ChineseMoneyConverter.ExceedException.class)
    public void testConvertForExpOfExceedIntPart() {
        ChineseMoneyConverter.convert("100000000100010000010");
    }
    @Test(expected = ChineseMoneyConverter.ExceedException.class)
    public void testConvertForExpOfExceedDecimalPart() {
        ChineseMoneyConverter.convert("1.234");
    }

    @Test(expected = ChineseMoneyConverter.ExceedException.class)
    public void testConvertForExpOfExceed() {
        ChineseMoneyConverter.convert("100000000100010000010.234");
    }
}
