package com.github.haojiankang.framework.commons.utils.lang;

import java.math.BigDecimal;

/**
 * 数字工具类.
 * 
 * @version 1.0.0
 */
public class MathUtils {

    /**
     * 判断字符串是否为数字组成
     * 
     * @param num
     *            待判定的字符串
     * @return 是数字返回true，否则返回false
     */
    public static boolean isNumber(String num) {
        final String numStr = "0123456789.+-";
        if (num == null) {
            return false;
        }
        if (num.trim().length() < 1) {
            return false;
        }
        for (int i = 0; i < num.length(); i++) {
            char tmp = num.charAt(i);
            if (numStr.indexOf(tmp) == -1) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断字符串是否为有效数字
     * 
     * @param num
     *            待判定的字符串
     * @return 整数返回0，小数返回1，不是数字返回-1
     */
    public static int getNumberType(String num) {
        if (num == null) {
            return -1;
        }
        if (num.trim().length() < 1) {
            return -1;
        }
        if (num.matches("0+[1-9]+")) {
            return -1;
        } // 可能是代码01，008等
        if (num.matches("[+-]?[0-9]+[.]?")) {
            return 0;
        }
        if (num.matches("[+-]?[0-9]+[.][0-9]+")) {
            return 1;
        }
        return -1;
    }

    /**
     * 提供精确的小数位四舍五入处理。
     * 
     * @param number
     *            需要四舍五入的数字
     * @param scale
     *            需要保留小数点后几位
     * @return 四舍五入后的结果
     */
    public static String round(String number, int scale) {
        if (number == null || number.trim().length() < 1)
            return "";
        String str = number.trim();
        if (scale < 0)
            return str;
        try {
            // BigDecimal mData = (new BigDecimal(str)).setScale(scale,
            // BigDecimal.ROUND_HALF_UP);
            BigDecimal mData = (new BigDecimal(str)).divide(new BigDecimal("1"), scale, BigDecimal.ROUND_HALF_UP);
            str = mData.toString();
        } catch (Exception e) {
        }
        return str;
    }

    /**
     * 截取小数点后几位的方法
     * 
     * @param number
     *            小数
     * @param scale
     *            需要保留小数点后几位
     * @return 舍入后的数值
     */
    public static double round(double number, int scale) {
        String str = round(String.valueOf(number), scale);
        return toDouble(str, number);
    }

    /**
     * 转换百分数
     * 
     * @param number
     *            数值
     * @return 百分值，不包括符号%
     */
    public static String percentage(String number) {
        BigDecimal decimal = new BigDecimal(number);
        decimal = decimal.multiply(new BigDecimal(100));
        return round(decimal.toString(), 2);
    }

    /**
     * 转换百分数
     * 
     * @param number
     *            数值
     * @return 百分值
     */
    public static double percentage(double number) {
        String str = percentage(String.valueOf(number));
        return toDouble(str, number);
    }

    /**
     * 字符串到整数转换
     * 
     * @param obj
     *            要转换的字符串
     * @param idefault
     *            转换不成功时返回的默认值
     * @return 返回转换后的数值
     */
    public static int toInt(Object obj, int idefault) {
        if (obj == null) {
            return idefault;
        }
        String str = String.valueOf(obj);
        if (str.trim().length() < 1) {
            return idefault;
        }
        try {
            return Integer.parseInt(str.trim());
        } catch (Exception e) {
            return idefault;
        }
    }

    /**
     * 字符串到长整数转换
     * 
     * @param obj
     *            要转换的字符串
     * @param ldefault
     *            转换不成功时返回的默认值
     * @return 返回转换后的数值
     */
    public static long toLong(Object obj, long ldefault) {
        if (obj == null) {
            return ldefault;
        }
        String str = String.valueOf(obj);
        if (str.trim().length() < 1) {
            return ldefault;
        }
        try {
            return Long.parseLong(str.trim());
        } catch (Exception e) {
            return ldefault;
        }
    }

    /**
     * 字符串到复数转换
     * 
     * @param obj
     *            要转换的字符串
     * @param dbdefault
     *            转换不成功时返回的默认值
     * @return 返回转换后的数值
     */
    public static double toDouble(Object obj, double dbdefault) {
        if (obj == null) {
            return dbdefault;
        }
        String str = String.valueOf(obj);
        if (str.trim().length() < 1) {
            return dbdefault;
        }
        try {
            return Double.parseDouble(str.trim());
        } catch (Exception e) {
            return dbdefault;
        }
    }

    /**
     * 提供精确的加法运算。
     * 
     * @param v1
     *            被加数
     * @param v2
     *            加数
     * @return 两个参数的和
     */
    public static double add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }

    /**
     * 提供精确的减法运算。
     * 
     * @param v1
     *            被减数
     * @param v2
     *            减数
     * @return 两个参数的差
     */
    public static double sub(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 提供精确的乘法运算。
     * 
     * @param v1
     *            被乘数
     * @param v2
     *            乘数
     * @return 两个参数的积
     */
    public static double mul(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }

    /**
     * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到 小数点以后2位，以后的数字四舍五入。
     * 
     * @param v1
     *            被除数
     * @param v2
     *            除数
     * @return 两个参数的商
     */
    public static double div(double v1, double v2) {
        return div(v1, v2, 2);
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。
     * 
     * @param v1
     *            被除数
     * @param v2
     *            除数
     * @param scale
     *            表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public static double div(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
