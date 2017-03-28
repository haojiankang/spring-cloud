package com.haojiankang.framework.commons.utils;

import java.security.SecureRandom;
import java.util.UUID;

/**
 * 封装各种生成唯一性ID算法的工具类.
 * 
 * @version 1.0.0
 */
public class IdentityHelper {

    private SecureRandom random = null;

    public IdentityHelper() {
        random = new SecureRandom();
    }

    /**
     * 生成指定区间[min-max)之间的随机数
     * 
     * @param min
     *            最小值，包含
     * @param max
     *            最大值，不包含
     * @return 最小值和最大值之间的随机数
     */
    public int randomInt(int min, int max) {
        return random.nextInt(max) % (max - min + 1) + min;
    }

    /**
     * 使用SecureRandom随机生成长整型.
     */
    public long randomLong() {
        long r = random.nextLong();
        return r == Integer.MIN_VALUE ? Integer.MAX_VALUE : r;
    }

    /**
     * 使用SecureRandom生成指定长度的随机asc字符串
     * 
     * @param length
     * @return 随机asc字符串
     */
    public String randomString(int length) {
        String strBase = "0123456789ABCDEFGHIGKLMNOPQRSTUVWXYZabcdefghigklmnopkrstuvwxyz";
        return randomString(length, strBase);
    }

    /**
     * 使用SecureRandom生成指定长度和范围的随机字符串
     * 
     * @param length
     *            随机字符串长度
     * @param base
     *            随机字符串范围
     * @return 指定长度和范围的随机字符串
     */
    public String randomString(int length, String base) {
        StringBuffer sbf = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sbf.append(base.charAt(number));
        }
        return sbf.toString();
    }

    /**
     * 封装JDK自带的UUID, 通过Random数字生成, 中间有-分割.
     */
    public static String uuid() {
        return UUID.randomUUID().toString();
    }

    /**
     * 封装JDK自带的UUID, 通过Random数字生成, 中间无-分割.
     */
    public static String uuid2() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 获得下一个流水号
     * 
     * @param current
     *            当前流水号
     * @param beginIndex
     *            流水号变化部分的起始位置，默认从0开始
     * @param endIndex
     *            流水号变化部分的截止位置(不包括)，默认最后
     * @param hex
     *            是否十六进制：true是，false否
     * @return 下一个流水号
     */
    public static String nextSerialNo(String current, Integer beginIndex, Integer endIndex, Boolean hex) {
        if (current == null)
            return null;
        int length = current.length();
        if (beginIndex == null || beginIndex.intValue() < 1 || beginIndex.intValue() >= length)
            beginIndex = 0;
        if (endIndex == null || endIndex.intValue() > length)
            endIndex = length;
        if (endIndex.intValue() <= beginIndex.intValue())
            endIndex = beginIndex.intValue() + 1;

        String before = current.substring(beginIndex, endIndex);
        int ii = (hex != null && hex.booleanValue() == true ? Integer.parseInt(before, 16) : Integer.parseInt(before))
                + 1;
        String after = hex != null && hex.booleanValue() == true ? Integer.toHexString(ii).toUpperCase()
                : String.valueOf(ii);

        StringBuffer sbf = new StringBuffer(50);
        if ((endIndex.intValue() - beginIndex.intValue()) < after.length()) {
            sbf.append("SerialNo ").append(after).append(" out of length : ")
                    .append(endIndex.intValue() - beginIndex.intValue());
            throw new RuntimeException(sbf.toString());
        }

        sbf.append(current.substring(0, beginIndex));
        length = endIndex - beginIndex - after.length();
        for (int i = 0; i < length; i++) {
            sbf.append("0");
        }
        return sbf.append(after).append(current.substring(endIndex)).toString();
    }
}
