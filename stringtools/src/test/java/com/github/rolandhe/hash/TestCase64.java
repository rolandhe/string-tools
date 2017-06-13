package com.github.rolandhe.hash;

import com.github.rolandhe.hash.cityhash.CityHash;

import org.junit.Assert;
import org.junit.Test;


/**
 * Created by hexiufeng on 2017/6/8.
 */
public class TestCase64 {

  @Test
  public void testLess4(){
    String s="中";

    CityHash cityHash = new CityHash();
    long hv = cityHash.hash64(s);
    System.out.println(hv);
    System.out.println(Long.toHexString(hv));
    Assert.assertTrue(0xac065168c34af7feL == hv);

  }
  @Test
  public void test4To8(){
    String s="中国";

    CityHash cityHash = new CityHash();
    long hv = cityHash.hash64(s);
    System.out.println(hv);
    System.out.println(Long.toHexString(hv));
    Assert.assertTrue(0xe4499ba8daad0087L == hv);

  }

  @Test
  public void test8To16(){
    String s="中国人民";

    CityHash cityHash = new CityHash();
    long hv = cityHash.hash64(s);
    System.out.println(hv);
    System.out.println(Long.toHexString(hv));
    Assert.assertTrue(0x155f37b6451fcd43L == hv);

  }

  @Test
  public void test17To32(){
    String s="中国人民共和国";

    CityHash cityHash = new CityHash();
    long hv = cityHash.hash64(s);
    System.out.println(hv);
    System.out.println(Long.toHexString(hv));
    Assert.assertTrue(0xe0665a552991a515L == hv);

  }

  @Test
  public void test33To64(){
    String s="中国人民共和国站起来了鼓掌";

    CityHash cityHash = new CityHash();
    long hv = cityHash.hash64(s);
    System.out.println(hv);
    System.out.println(Long.toHexString(hv));
    Assert.assertTrue(0xaf216c93667b7d75L == hv);

  }

  @Test
  public void testGt64(){
    String s="我们将通过生成一个大的文件的方式来检验各种方法的执行效率因为这种方式在结束的时候需要执行文件";

    CityHash cityHash = new CityHash();
    long hv = cityHash.hash64(s);
    System.out.println(hv);
    System.out.println(Long.toHexString(hv));
    Assert.assertTrue(0x8db857dd3d4b22e1L == hv);

  }
}
