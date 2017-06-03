package com.github.rolandhe.chinese.convertor;

import com.github.rolandhe.chinese.convertor.trie.TrieHelper;
import com.github.rolandhe.chinese.convertor.trie.TrieNode;

/**
 * 简体转繁体实现
 *
 */
public class Simp2TradConvertor extends AbstractChineseConvertor implements ChineseConvertor {
  private static final TrieNode ROOT = TrieHelper.build("dict/simp_to_trad_phrases.txt",
          "dict/simp_to_trad_characters.txt");


  @Override
  protected TrieNode getRoot() {
    return ROOT;
  }
}
