package com.github.rolandhe.hash;

import com.github.rolandhe.hash.cityhash.CityHash;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by hexiufeng on 2017/6/8.
 */
public class TestCase128 {


  @Test
  public void testLess16(){
    String s="中国人民";

    CityHash cityHash = new CityHash();
    Number128 hv = cityHash.hash128(s);

    System.out.println(Long.toHexString(hv.getLowValue()));
    System.out.println(Long.toHexString(hv.getHiValue()));
    Assert.assertTrue(0xdb49338f94e45026L == hv.getLowValue());
    Assert.assertTrue(0xb57c012f2ec4ee25L == hv.getHiValue());

  }


  @Test
  public void test128(){
    String s="中国人民站起来了胡扯";

    CityHash cityHash = new CityHash();
    Number128 hv = cityHash.hash128(s);

    System.out.println(Long.toHexString(hv.getLowValue()));
    System.out.println(Long.toHexString(hv.getHiValue()));
    Assert.assertTrue(0xd44fe88bbed69110L == hv.getLowValue());
    Assert.assertTrue(0x731d6d9846735ecdL == hv.getHiValue());

  }

  @Test
  public void testBig(){
    String s="我们将通过生成一个大的文件的方式来检验各种方法的执行效率因为这种方式在结束的时候需要执行文件";

    CityHash cityHash = new CityHash();
    Number128 hv = cityHash.hash128(s);

    System.out.println(Long.toHexString(hv.getLowValue()));
    System.out.println(Long.toHexString(hv.getHiValue()));
    Assert.assertTrue(0x73b63e8cd44766c5L == hv.getLowValue());
    Assert.assertTrue(0x9ed8d2c68d45b293L == hv.getHiValue());

  }

}
