package com.github.rolandhe.chinese.convertor;

import org.apache.commons.lang3.StringUtils;

/**
 * 金额数字转换为中文大写的工具类，整数部分支持最多20位，小数部分最多支持两位。。<br>
 * <p>
 * 整数部分算法是通过分段实现，每一段是4位，分别对应着千级、千万、千亿、千亿亿、万亿亿，分别计算后统一消除重复的"零"。
 * </p>
 *
 * @author rolandhe
 */
public class ChineseMoneyConverter {

    private ChineseMoneyConverter() {
    }


    /**
     * 金额数字支持整数和小数，最多2部分
     */
    private static final int PART_COUNT = 2;
    /**
     * 整数部分的最大长度是20位
     */
    private static final int INT_PART_MAX_LENGTH = 20;
    /**
     * 小数部分最多两位
     */
    private static final int DECIMAL_PART_MAX_LENGTH = 2;


    private static final String ZERO_STR = "0";
    private static final char ZERO_CHAR = '0';
    private static final char ZERO_CN_CHAR = '零';
    private static final String ZERO_CN_STR = "零";

    private static final String[] CHINESE_NUMBER = {ZERO_CN_STR, "壹", "貳", "叁", "肆", "伍", "陆", "柒", "捌", "玖"};
    private static final String[] UNITS = {"", "拾", "佰", "仟"};

    /**
     * 20位整数最多分5段
     */
    private static final String[] SEGMENTS = {"", "万", "亿", "万亿", "亿亿"};
    private static final String YUAN = "元";
    private static final String YUAN_END = "元整";
    private static final String[] DECIMAL_UNIT = {"角", "分"};

    /**
     * 分段长度
     */
    private static final int SEC_LENGTH = 4;

    /**
     * 把金额字符串转换成中文
     *
     * @param num 数字金额字符串
     * @return 转换完成的中文
     */
    public static String convert(String num) {
        // 清除整数之前的0
        String trimNumber = StringUtils.stripStart(num, ZERO_STR);
        if(trimNumber.length() == 0) {
            trimNumber = "0";
        }
        String[] parts = StringUtils.splitByWholeSeparatorPreserveAllTokens(trimNumber, ".");
        if (parts[0].length() > INT_PART_MAX_LENGTH) {
            throw new RuntimeException(num + " int part exceed");
        }
        if (parts.length == PART_COUNT) {
            // 消除小数右边的0
            parts[1] = StringUtils.stripEnd(parts[1], ZERO_STR);
            if (parts[1].length() > DECIMAL_PART_MAX_LENGTH) {
                throw new RuntimeException(num + " decimal part exceed");
            }
        }
        String intPart = processIntPart(parts[0]);
        String decimalPart = null;
        if (parts.length == PART_COUNT) {
            decimalPart = processDecimalPart(parts[1]);
        }

        return concat(intPart, decimalPart);
    }

    /**
     * 拼接整数与小数部分
     *
     * @param intPart     整数部分
     * @param decimalPart 小数部分
     * @return 中文结果
     */
    private static String concat(String intPart, String decimalPart) {
        if (StringUtils.isEmpty(intPart) && StringUtils.isEmpty(decimalPart)) {
            return ZERO_CN_STR + YUAN_END;
        }
        if (StringUtils.isEmpty(decimalPart)) {
            return intPart + YUAN_END;
        }
        if (StringUtils.isEmpty(intPart)) {
            return decimalPart;
        }
        return intPart + YUAN + decimalPart;
    }

    /**
     * 转换整数部分
     *
     * @param intPart 整数部分字符串
     * @return 整数部分中文串
     */
    private static String processIntPart(String intPart) {
        if (StringUtils.isEmpty(intPart)) {
            return null;
        }
        String[] segments = segmentIntPart(intPart);
        StringBuilder sb = new StringBuilder();
        StringBuilder context = new StringBuilder();
        int index = 0;
        for (String segment : segments) {
            // 每段的单位
            String unit = SEGMENTS[segments.length - 1 - index];
            processSegment(segment, context);
            sb.append(context.toString());
            if (!onlyZero(context)) {
                sb.append(unit);
            }
            index++;
        }
        clearZeroRepeat(sb);
        return sb.toString();
    }

    /**
     * 当前段转换完后是否只有"零"
     *
     * @param context 当前上下文
     * @return 是否只有"零"
     */
    private static boolean onlyZero(StringBuilder context) {
        return context.length() == 1 && context.charAt(0) == ZERO_CN_CHAR;
    }

    /**
     * 处理一个分段
     *
     * @param segment 分段
     * @param context 转换上下文
     */
    private static void processSegment(String segment, StringBuilder context) {
        if (context.length() > 0) {
            context.delete(0, context.length());
        }
        for (int i = 0; i < segment.length(); i++) {
            char c = segment.charAt(i);
            int value = (c - ZERO_CHAR);
            int unitPos = segment.length() - 1 - i;
            if (!ignoreOne(segment, unitPos, value)) {
                context.append(CHINESE_NUMBER[value]);
            }
            if (value > 0) {
                String unit = UNITS[unitPos];
                context.append(unit);
            }
        }
        clearZeroRepeat(context);
    }

    /**
     * 比如：100000直接输出是壹拾万，而拾万是更合理的，本方法是判断是否要忽略这个壹
     *
     * @param segment 分段字符串
     * @param unitPos 当前是在10位上还是个位上
     * @param value   当前位对应的值
     * @return 是否忽略壹
     */
    private static boolean ignoreOne(String segment, int unitPos, int value) {
        if (value != 1 || unitPos != 1) {
            return false;
        }
        // 如果当前端仅仅有2位时，符合丢调用"壹"的规则
        if (segment.length() == 2) {
            return true;
        }
        return false;
    }

    /**
     * 消除重复的"零"
     *
     * @param context 接收数据的上下文
     */
    private static void clearZeroRepeat(StringBuilder context) {
        int pos = 0;
        boolean hasZero = false;
        while (pos < context.length()) {
            if (context.charAt(pos) == ZERO_CN_CHAR) {
                if (hasZero) {
                    context.deleteCharAt(pos);
                    continue;
                }
                hasZero = true;
                pos++;
                continue;
            }
            hasZero = false;
            pos++;
        }
        if (context.length() > 1 && context.charAt(context.length() - 1) == ZERO_CN_CHAR) {
            context.deleteCharAt(context.length() - 1);
        }
    }

    /**
     * 把整数部分按照指定位数一段进行分解，这儿使用4位一段
     *
     * @param intPart 整数部分
     * @return 拆分处理的端
     */
    private static String[] segmentIntPart(String intPart) {
        String[] segments = new String[(intPart.length() + SEC_LENGTH - 1) / SEC_LENGTH];
        int startLen = intPart.length() - (segments.length - 1) * SEC_LENGTH;
        segments[0] = intPart.substring(0, startLen);
        int pos = startLen;
        for (int i = 1; i < segments.length; i++) {
            segments[i] = intPart.substring(pos, pos + SEC_LENGTH);
            pos += SEC_LENGTH;
        }
        return segments;
    }

    /**
     * 转换小数部分
     *
     * @param decimalPart 小数部分
     * @return 转换完的中文串
     */
    private static String processDecimalPart(String decimalPart) {
        if (StringUtils.isEmpty(decimalPart)) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < decimalPart.length(); i++) {
            String unit = DECIMAL_UNIT[i];
            char c = decimalPart.charAt(i);
            int value = c - ZERO_CHAR;
            if (value == 0) {
                continue;
            }
            sb.append(CHINESE_NUMBER[value]).append(unit);
        }

        return sb.toString();
    }
}
