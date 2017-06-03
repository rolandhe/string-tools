package com.github.rolandhe.chinese.convertor;

import com.github.rolandhe.chinese.convertor.trie.TrieNode;

/**
 * 繁体简体互转抽象实现
 */
public abstract class AbstractChineseConvertor implements  ChineseConvertor{

    /**
     * 获取字典树
     *
     * @return
     */
    protected  abstract TrieNode getRoot();

    @Override
    public String convert(String raw) {
        StringBuilder sb = new StringBuilder(raw.length());
        char[] rawArray = raw.toCharArray();

        // 从根节点开始匹配，深度匹配
        MatchContext matchContext = new MatchContext(getRoot());

        for(int i = 0; i < rawArray.length;){
            int codePoint = Character.codePointAt(rawArray,i);
            TrieNode search = matchContext.nextNode.search(codePoint);
            // 匹配到一个词的前缀
            if(search != null){
                matchContext.accept(i, search);
                i += Character.charCount(codePoint);
                // 可能到了字符串的最后一个字符，需要尝试转换已经匹配到的词
                if(i == rawArray.length){
                    i = collectMatched(sb,matchContext);
                }
                continue;
            }

            // 匹配中断，尝试转换已经匹配到的词
            if(matchContext.hasMatched()){
                i = collectMatched(sb,matchContext);
                continue;
            }
            // 没有匹配到任何词的前缀
            sb.append(Character.toChars(codePoint));
            i += Character.charCount(codePoint);
        }

        return sb.toString();
    }

    /**
     * 转换已经匹配到的词并reset上下文
     *
     * @param sb
     * @param context
     * @return
     */
    private int collectMatched(final StringBuilder sb, final MatchContext context){
        MatchItem matchedItem = context.last;
        // 匹配到一个完整的词
        if(matchedItem != null) {
            sb.append(matchedItem.node.getTo());
        } else {
            // 没有匹配到完整的词，此时需要跳过第一匹配的字符，从下一个继续和字典树匹配
            matchedItem = context.begin;
            sb.append(Character.toChars(matchedItem.node.getCodePoint()));
        }
        context.reset(getRoot());
        return matchedItem.index + Character.charCount(matchedItem.node.getCodePoint());
    }

    /**
     * 匹配上下文
     */
    private static  class MatchContext {
        /**
         * 字符串中某个字符和字典中的某词第一字符匹配信息,匹配到之后会继续深度匹配
         */
        MatchItem begin;
        /**
         * 记录深度匹配过程中已经匹配到的词
         */
        MatchItem last;
        /**
         * 下一字符需要匹配的字典节点，即在nextNode的子中继续匹配下个字符，初始一定是Root
         */
        TrieNode nextNode;

        MatchContext(TrieNode nextNode){
            this.nextNode = nextNode;
        }

        /**
         * 某个词匹配完后，需要继续匹配新词时重置上下文信息
         *
         * @param root 必须是Root
         */
        void reset(TrieNode root){
            this.begin = null;
            this.last = null;
            this.nextNode = root;
        }

        /**
         * 是否匹配到某个词的前缀或所有字符
         *
         * @return
         */
        boolean hasMatched(){
            return  begin != null;
        }

        /**
         * 接收已经匹配到的字符和字典节点
         *
         * @param index
         * @param node
         */
        void accept(int index,TrieNode node){
            nextNode = node;
            if(begin == null){
                begin = new MatchItem(index,node);
                if(begin.node.isComplete()) {
                    last = begin;
                }
                return;
            }
            if(node.isComplete()){
                last = new MatchItem(index,node);
            }
        }
    }

    /**
     * 描述字符串与字典匹配过程中已经匹配到的字符和对应的字典节点信息
     */
    private  static class MatchItem{
        MatchItem(int index,TrieNode node){
            this.index = index;
            this.node = node;
        }

        /**
         * 匹配到的字符在字符串中的位置,指char[]中的位置
         */
        final int index;
        /**
         * 对应字典中的节点
         */
        final TrieNode node;
    }

}
