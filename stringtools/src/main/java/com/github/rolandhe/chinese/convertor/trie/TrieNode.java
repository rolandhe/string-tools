package com.github.rolandhe.chinese.convertor.trie;


import java.util.HashMap;
import java.util.Map;

/**
 * 描述一个字典树.树有个根节点,字典中所有的词都是根节点的子节点.
 * 每个子节点包含一个unicode code point，用于描述一个unicode字符，根节点的codepoint是-1.
 * 每个子节点都可能包含char数组,表示从根节点的子开始深度匹配到该子节点时所对应的转换的词。
 *
 *
 */
public class TrieNode {
  /**
   * 根节点的codepoint
   */
  public static  final int ROOT_CODE_POINT = -1;

  /**
   * 节点的子节点
   */
  private final Map<Integer,TrieNode> children = new HashMap<>();

  /**
   * 节点包含的unicode 字符对应的codepoint
   */
  private final int codePoint;

  /**
   * 对应转换的词
   */
  private char[] to;
  

  public TrieNode(int codePoint){
    this.codePoint = codePoint;
  }

  /**
   * 是否存在可转换的词
   *
   * @return
   */
  public boolean isComplete(){
    return to != null;
  }
  
  public char[] getTo() {
    return to;
  }

  public void setTo(char[] to) {
    this.to = to;
  }

  public int getCodePoint(){
    return codePoint;
  }

  /**
   * 添加子节点
   *
   * @param child
   * @return
   */
  public  boolean addChild(TrieNode child){
    if(children.containsKey(child.codePoint)){
      return false;
    }
    children.put(child.codePoint,child);
    return true;
  }

  /**
   * 查找某个unicode 字符是否在子节点中存在
   *
   * @param codePoint
   * @return
   */
  public TrieNode search(int codePoint){
    return children.get(codePoint);
  }

  @Override
  public  String toString(){
    if(codePoint == ROOT_CODE_POINT){
      return "ROOT";
    }
    return new String(Character.toChars(codePoint));
  }
}
