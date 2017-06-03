package com.github.rolandhe.chinese.convertor;

/**
 *
 * 繁体简体互转抽象
 *
 */
public interface ChineseConvertor {

  /**
   * 繁体简体互转
   * @param raw
   * @return
   */
  String convert(String raw);
}
