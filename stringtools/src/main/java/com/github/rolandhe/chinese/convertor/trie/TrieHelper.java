package com.github.rolandhe.chinese.convertor.trie;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;


/**
 * 工具类，可从指定的字典文件中加载词并构建字典树
 *
 */
public  class TrieHelper {
  private TrieHelper(){
    
  }

  /**
   * 从多个文件中加载字典信息并构建树
   *
   * @param files
   * @return
   */
  public static  TrieNode build(String ...files){
    if(files == null || files.length == 0){
      return null;
    }
    TrieNode root = new TrieNode(TrieNode.ROOT_CODE_POINT);
    for(String f:files){
      loadFile(f,root);
    }
    return root;
  }

  /**
   * 加载单个字典文件
   *
   * @param file
   * @param root
   */
  private static void loadFile(String file, final TrieNode root){
    InputStream inputStream = TrieHelper.class.getClassLoader().getResourceAsStream(file);
    if(inputStream == null){
      throw new RuntimeException("can't open file of " + file);
    }
    try {
      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
      while (true) {
        String line  = bufferedReader.readLine();
        if(line == null){
          break;
        }
        line = line.trim();
        if(line.length() == 0){
          continue;
        }
        String[] array = line.split("\t");
        if(array.length == 1){
          continue;
        }
        fillChinesePair(root,array[0],array[1].split(" ")[0]);
      }

    } catch (UnsupportedEncodingException e) {
      // ignore
    } catch (IOException e) {
      throw new RuntimeException(e);
    } finally {
      quietlyClose(inputStream);
    }
  }


  /**
   * 添加词对到字典树
   *
   * @param root
   * @param search
   * @param replace
   */
  private  static  void fillChinesePair(final TrieNode root, String search,String replace){
    if(search == null || search.length() == 0){
      return;
    }
    char[] searchArray = search.toCharArray();

    TrieNode next = root;
    for(int i = 0;i < searchArray.length;){
      int codePoint = Character.codePointAt(search,i);
      TrieNode node = next.search(codePoint);
      if(node == null){
        node = new TrieNode(codePoint);
        next.addChild(node);
      }
      next = node;
      i += Character.charCount(codePoint);
    }
    next.setTo(replace.toCharArray());
  }

  /**
   * 优雅关闭文件
   *
   * @param closeable
   */
  private  static  void quietlyClose(Closeable closeable){
    try {
      closeable.close();
    } catch (IOException e) {
      // ignore
    }
  }
}
