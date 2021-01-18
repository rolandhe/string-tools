package com.github.rolandhe.chinese.convertor;

import org.apache.commons.lang3.StringUtils;

/**
 * 金额数字转换为中文大写的工具类，整数部分支持最多20位，小数部分最多支持两位。<br>
 * <p>
 * 整数部分算法是通过分段实现，每一段是4位，分别对应着千级、千万、千亿、千亿亿、万亿亿，分别计算后统一消除重复的"零"。
 * </p>
 * 遵循小学数字读取法则知识：<br>
 * <ul>
 *     <li>1. 四位以内的数，按照数位顺序，从最高位读起</li>
 *     <li>2. 四位以上的数，先从右向左四位分级，然后从最高级起，依次读亿级、万级、个级。读出各级里的数和它们的级名。亿级里的数，按照个级的数的读法来读，再在后面加上一个“亿”字。万级里的数，同样按照个级的数的读法来读，再在后面加上一个“万”字；</li>
 *     <li>3. 每级末尾不管有几个“0”，都不读，其他数位上有一个“0”或几个“0”，都只读一个零。</li>
 * </ul>
 *
 * @author rolandhe
 * @date 2020-12-10 14:30
 */
public class ChineseMoneyConverter {
    /**
     * 私有构造器，防止被实例化
     */
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

    /**
     * 0字符串
     */
    private static final String ZERO_STR = "0";

    /**
     * 零字符
     */
    private static final char ZERO_CHAR = '0';

    /**
     * 零字符串
     */
    private static final char ZERO_CN_CHAR = '零';

    /**
     * 零字符
     */
    private static final String ZERO_CN_STR = "零";

    /**
     * 亿级别单位
     */
    private static final char YI_UNIT = '亿';

    /**
     * 金额中数字对应中文
     */
    private static final String[] CHINESE_NUMBER = {ZERO_CN_STR, "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"};

    /**
     * 每个分段对应的4个位，个位不需要展示
     */
    private static final String[] UNITS = {"", "拾", "佰", "仟"};

    /**
     * 20位整数最多分5段,元级别不需要展现
     */
    private static final String[] SEGMENTS = {"", "万", "亿", "万亿", "亿亿"};

    /**
     * 分段级别中的"亿"级别
     */
    public static final int SEGMENT_LEVEL_YI = 3;

    /**
     * 分段的4位都是0时,是否需要忽略，对应{@link #SEGMENTS}, 其中亿级别需要忽略
     */
    private static final boolean[] SEGMENTS_FULL_ZERO_IGNORE = {false, false, true, false, false};

    /**
     * 元
     */
    private static final String YUAN = "元";
    /**
     * 没有小数的金额结尾
     */
    private static final String YUAN_END = "元整";

    /**
     * 金额小数部分单位
     */
    private static final String[] DECIMAL_UNIT = {"角", "分"};

    /**
     * 分段长度
     */
    private static final int SEC_LENGTH = 4;

    /**
     * 小数点字符
     */
    private static final String DECIMAL_POINT = ".";


    /**
     * 金额超长异常
     */
    public static class ExceedException extends RuntimeException {
        /**
         * 异常构造器
         *
         * @param message 异常信息
         */
        public ExceedException(String message) {
            super(message);
        }
    }

    /**
     * 把金额字符串转换成中文
     *
     * @param amount 数字金额字符串
     * @return 转换完成的中文
     * @throws ExceedException 可能抛出小数或者整数部分的超长异常
     */
    public static String convert(String amount) throws ExceedException {
        // 清除整数之前的0
        String trimNumber = StringUtils.stripStart(amount, ZERO_STR);
        // 全是零时不能消除成null了
        if (trimNumber.length() == 0) {
            trimNumber = ZERO_STR;
        }
        String[] parts = StringUtils.splitByWholeSeparatorPreserveAllTokens(trimNumber, DECIMAL_POINT);
        if (parts[0].length() > INT_PART_MAX_LENGTH) {
            throw new ExceedException(amount + " int part exceed");
        }
        if (parts.length == PART_COUNT) {
            // 消除小数右边的0
            parts[1] = StringUtils.stripEnd(parts[1], ZERO_STR);
            if (parts[1].length() > DECIMAL_PART_MAX_LENGTH) {
                throw new ExceedException(amount + " decimal part exceed");
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
            int segmentPos = segments.length - 1 - index;
            boolean ignoreZero = SEGMENTS_FULL_ZERO_IGNORE[segmentPos];
            processSegment(segment, context);
            boolean fullZero = onlyZero(context);
            if (!fullZero || !ignoreZero) {
                sb.append(context.toString());
            }
            if (!fullZero) {
                // 每段的单位
                String unit = SEGMENTS[segmentPos];
                sb.append(unit);
            }
            index++;
        }
        clearZeroRepeat(sb);
        if (segments.length > SEGMENT_LEVEL_YI) {
            normalizeUnit(sb);
        }
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
            context.append(CHINESE_NUMBER[value]);
            if (value > 0) {
                String unit = UNITS[unitPos];
                context.append(unit);
            }
        }
        clearZeroRepeat(context);
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
     * "万亿"、"亿亿"级别时，当还存在"亿"级别段，需要把"万亿"、"亿亿"后面的亿消除掉
     *
     * @param sb 转换金额上下文
     */
    private static void normalizeUnit(StringBuilder sb) {
        boolean metYi = false;
        for (int i = sb.length() - 1; i >= 0; i--) {
            char c = sb.charAt(i);
            if (c != YI_UNIT) {
                continue;
            }
            if (!metYi) {
                metYi = true;
                continue;
            }
            sb.deleteCharAt(i);
            if (i > 0 && sb.charAt(i - 1) == YI_UNIT) {
                i--;
            }
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
            // 零分、零角都不需要展示
            if (value == 0) {
                continue;
            }
            sb.append(CHINESE_NUMBER[value]).append(unit);
        }

        return sb.toString();
    }
}