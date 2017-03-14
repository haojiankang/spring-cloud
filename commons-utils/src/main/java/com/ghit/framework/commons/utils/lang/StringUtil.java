package com.ghit.framework.commons.utils.lang;

import java.util.*;

/**
 * String 工具类
 */
public class StringUtil {

    /**
     * StringBuilder的初始化大小,设置的大点,
     */
    public static int SB_BUF_SIZE = 1000;

    /**
     * 判断一个值是否是空值(null或者空字符串,或trim为空字符串).
     * 
     * @param value所判断的值
     * @return 返回boolean，空为true
     */
    public static boolean isEmpty(Object value) {
        return value == null || (value instanceof String && isEmpty((String) value));
    }

    /**
     * 判断一个字符串对象是否为空,(null或者trim后为"")
     * 
     * @param value所判断的值
     * @return 返回boolean，空为true
     */
    public static boolean isEmpty(String value) {
        return value == null || "".equals(value) || "".equals(value.trim());
    }

    /**
     * 判断一个字符串数组是否为空,null或者length=0,或者第一个字符串为空
     */
    public static boolean isEmpty(String[] values) {
        return values == null || values.length == 0 || isEmpty(values[0]);
    }

    /**
     * 判断是否是一个"无意义/无效"的web值,null,空字符串或"undefined","null"
     */
    public static boolean isEmptyWebString(String value) {
        return isEmpty(value) || "undefined".equals(value) || "null".equals(value);
    }

    /**
     * 将一个list,合并为字符串
     */
    @SuppressWarnings("rawtypes")
    public static String join(Iterable list, String sep) {
        if (list == null) {
            return "";
        }
        sep = sep == null ? "" : sep;
        StringBuilder sb = new StringBuilder(SB_BUF_SIZE);
        for (Object o : list) {
            sb.append(sep).append(o);
        }
        if (sb.length() > 0 && sep.length() > 0) {
            sb.delete(0, sep.length());
        }
        return sb.toString();
    }

    /**
     * 将一个数组,合并为字符串
     */
    public static String join(Object[] objs, String sep) {
        if (objs == null || objs.length == 0) {
            return "";
        }
        sep = sep == null ? "" : sep;
        StringBuilder sb = new StringBuilder(SB_BUF_SIZE);
        for (Object o : objs) {
            sb.append(sep).append(o);
        }
        return sb.substring(sep.length());
    }

    /**
     * 将一个字符串拆分为字符串数组
     */
    public static String[] stringSplit(String value, String regex) {
        if (isEmpty(value)) {
            return null;
        }
        return value.split(regex);
    }

    /**
     * 将一个字符串拆分为字符串数组,用默认的正则表达式"|"
     */
    public static String[] stringWebSplit(String value) {
        return stringSplit(value, "\\|");
    }

    /**
     * 将字符串拆解为多个字符串,不是正则表达式处理.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static String[] split(String source, String sep) {
        String[] ret;
        int slen = sep.length();
        if (slen == 0) {
            ret = new String[source.length()];
            for (int i = 0; i < ret.length; i++) {
                ret[i] = source.substring(i, i + 1);
            }
            return ret;
        }
        List<String> arr = new ArrayList();
        int pos = 0, pos2 = 0;
        while (pos2 > -1) {
            pos2 = source.indexOf(sep, pos);
            if (pos2 > 0) {
                arr.add(source.substring(pos, pos2));
                pos = pos2 + slen;
            }
        }
        arr.add(source.substring(pos));
        return arr.toArray(new String[0]);
    }

    /**
     * 在字符串中查询子字符串,获得,则返回左边部分,否则,返回null.不区分大小写.
     */
    public static String stringLeft(String value, String sep) {
        int pos = value.toLowerCase().indexOf(sep.toLowerCase());
        return pos == -1 ? null : value.substring(0, pos);
    }

    /**
     * 在字符串中查询子字符串,获得,则返回左边部分,否则,返回null.从右边开始查询.不区分大小写.
     */
    public static String stringLeftBack(String value, String sep) {
        int pos = value.toLowerCase().lastIndexOf(sep.toLowerCase());
        return pos == -1 ? null : value.substring(0, pos);
    }

    /**
     * 在字符串中查询子字符串,获得,则返回右边部分,否则,返回null.不区分大小写.
     */
    public static String stringRight(String value, String sep) {
        int pos = value.toLowerCase().indexOf(sep.toLowerCase());
        return pos == -1 ? null : value.substring(pos + sep.length());
    }

    /**
     * 在字符串中查询子字符串,命中,则返回右边部分,否则,返回null.从右侧向左侧查询.不区分大小写.
     */
    public static String stringRightBack(String value, String sep) {
        int pos = value.toLowerCase().lastIndexOf(sep.toLowerCase());
        return pos == -1 ? null : value.substring(pos + sep.length());
    }

    /**
     * 返回字符串,在左侧加入足够的填充符
     */
    public static String stringLeftPad(Object val, int len, String filling) {
        return stringPad(val, len, filling, true);
    }

    /**
     * 返回字符串,在右侧加入足够的填充符
     */
    public static String stringRightPad(Object val, int len, String filling) {
        return stringPad(val, len, filling, false);
    }

    /**
     * 将数据进行字符填充,直到足够长度
     * 
     * @param val
     *            原数据
     * @param len
     *            最小长度
     * @param filling
     *            要填充的字符串,如果为空,则使用一个空格" "
     * @param atleft
     *            是否在左侧填充
     * @return
     */
    private static String stringPad(Object val, int len, String filling, boolean atleft) {
        String sv = val + "";
        if (sv.length() > len) {
            return sv;
        }
        if (isEmpty(filling)) {
            filling = " ";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(sv);
        while (sb.length() < len) {
            if (atleft) {
                sb.insert(0, filling);
            } else {
                sb.append(filling);
            }
        }
        if (atleft) {
            return sb.substring(sb.length() - len);
        } else {
            return sb.substring(0, len);
        }
    }

    /**
     * 将一个对象转换为字符串,如果对象为null或者是空字符串(<B>注意,空字符串和null一样当作无效值</B>),返回defaultValue
     * 
     * @param o
     * @param defaultValue
     * @return 字符串.
     */
    public static String valueOf(Object o, String defaultValue) {
        return isEmpty(o) ? defaultValue : o.toString();
    }

    /**
     * 比较字符串是否相等
     * 
     * @param src
     * @param des
     * @return
     */
    public static boolean eq(String src, String des) {
        return src == des ? true : src == null || des == null ? false : src.equals(des);
    }

    /**
     * 判断字符串是否在一个数组中
     * 
     * @param src
     * @param des
     * @return
     */
    public static boolean in(String src, String... des) {
        for (String item : des) {
            if (eq(src, item)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 截取字符串，截取内容为开始到遇到第一个出现的endstr
     * 
     * @param src
     * @param endstr
     * @return
     */
    public static String subByEndStr(String src, String endstr) {
        int end = src.indexOf(endstr);
        end = end == -1 ? src.length() : end;
        return src.substring(0, end);
    }

    /**
     * 截取字符串，截取内容为第一个出现的startstr到字符串最后
     * 
     * @param src
     * @param endstr
     * @return
     */
    public static String subByStartStr(String src, String startstr) {
        int start = src.indexOf(startstr);
        if (start == -1)
            return "";
        return src.substring(start + 1);
    }

    /**
     * 得到非null字符串，String.trim()对于空对象返回null
     * 
     * @param source
     *            待处理字符串
     * @return 非null字符串
     */
    public static String trimToEmpty(Object source) {
        return source != null ? source.toString().trim() : "";
    }

    /**
     * 把字符串的首字母大写、效率是最高的
     * 
     * @param source
     *            待处理字符串
     * @return 首字母大写的字符串
     */
    public static String uppercaseFirst(String source) {
        if (source == null) {
            return null;
        }
        if (source.trim().length() < 1) {
            return "";
        }
        byte[] items = source.getBytes();
        if (96 < items[0] && items[0] < 123)
            items[0] = (byte) ((char) items[0] - 'a' + 'A');
        return new String(items);
    }

    /**
     * 把字符串的首字母小写、效率是最高的
     * 
     * @param source
     *            待处理字符串
     * @return 首字母小写的字符串
     */
    public static String lowercaseFirst(String source) {
        if (source == null) {
            return null;
        }
        if (source.trim().length() < 1) {
            return "";
        }
        byte[] items = source.getBytes();
        if (64 < items[0] && items[0] < 91)
            items[0] = (byte) ((char) items[0] + 'a' - 'A');
        return new String(items);
    }

    /**
     * 在StringBuffer添加字符串时，StringBuffer不为空则先加上分隔符
     * 
     * @param source
     *            StringBuffer对象
     * @param append
     *            追加的字符串
     * @param separate
     *            要添加的分隔符，为null时不添加
     */
    public static void appendBuffer(StringBuffer source, String append, String separate) {
        if (source.length() > 0 && separate != null)
            source.append(separate);
        source.append(append);
    }

    /**
     * 
     * toString:转换Object对象为字符串对象
     *
     * @author ren7wei
     * @param obj
     *            被转换的对象
     * @return obj为null返回空串，否则返回obj.toString()
     * @since JDK 1.8
     */
    public static String toString(Object obj) {
        if (obj == null)
            return "";
        return obj.toString();
    }
    public static String encodeUri(String para) {
        return encodeUri("UTF-8", para);
    }

    public static String encodeUri(String lang, String para) {
        try {
            return new String(para.getBytes(lang), "ISO8859-1");
        } catch (Exception e) {
        }
        return "";
    }

}
