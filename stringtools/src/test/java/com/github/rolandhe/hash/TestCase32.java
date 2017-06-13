package com.github.rolandhe.hash;

import com.github.rolandhe.hash.cityhash.CityHash;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by hexiufeng on 2017/6/8.
 */
public class TestCase32 {
  @Test
  public void testLE4(){
    String s="中";

    CityHash cityHash = new CityHash();
    int hv = cityHash.hash32(s);
    System.out.println(hv);
    System.out.println(Integer.toHexString(hv));
    Assert.assertTrue(0x94f00d7d == hv);

  }
  @Test
  public void test5To12(){
    String s="中国";

    CityHash cityHash = new CityHash();
    int hv = cityHash.hash32(s);
    System.out.println(hv);
    System.out.println(Integer.toHexString(hv));
    Assert.assertTrue(0xb342166b == hv);

  }

  @Test
  public void test13To24(){
    String s="中国人民了";

    CityHash cityHash = new CityHash();
    int hv = cityHash.hash32(s);
    System.out.println(hv);
    System.out.println(Integer.toHexString(hv));
    Assert.assertTrue(0xbc72f21a == hv);

  }

  @Test
  public void testGE24(){
    String s="我们将通过生成一个大的文件的方式来检验各种方法的执行效率因为这种方式在结束的时候需要执行文件";

    CityHash cityHash = new CityHash();
    int hv = cityHash.hash32(s);
    System.out.println(hv);
    System.out.println(Integer.toHexString(hv));
    Assert.assertTrue(0x51020cae == hv);

  }


}
