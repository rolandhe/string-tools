package com.github.rolandhe.chinese.convertor;

import org.junit.Test;

/**
 * Created by hexiufeng on 2017/6/3.
 */
public class Trad2SimpConvertorTest {
    @Test
    public void  test(){
        ChineseConvertor convertor = new Trad2SimpConvertor();
        String raw = "中乾斷情有獨鍾挪移";
        System.out.println( convertor.convert(raw));
    }
}
