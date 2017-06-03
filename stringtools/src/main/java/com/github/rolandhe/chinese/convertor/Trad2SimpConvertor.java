package com.github.rolandhe.chinese.convertor;

import com.github.rolandhe.chinese.convertor.trie.TrieHelper;
import com.github.rolandhe.chinese.convertor.trie.TrieNode;

/**
 * 繁体转简体实现
 *
 */
public class Trad2SimpConvertor extends AbstractChineseConvertor implements ChineseConvertor {
  private static final TrieNode ROOT = TrieHelper.build("dict/trad_to_simp_phrases.txt",
          "dict/trad_to_simp_characters.txt");


  @Override
  protected TrieNode getRoot() {
    return ROOT;
  }
}
