package com.haojiankang.framework.commons.utils.lang;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Description:日期时间工具类.
 * 
 * @author lengzhihui
 * @version 1.0
 * 
 *          <pre>
 * Modification History:
 * Date         Author       Version     Description
 * ------------------------------------------------------------------
 * 2016-05-02      lengzhihui    1.0        1.0 Version
 *          </pre>
 */
public class DateTimeUtil {

    protected static final Log LOGGER = LogFactory.getLog(DateTimeUtil.class);

    private static final String TIME_FORMAT_SHORT = "yyyyMMddHHmmss";

    private static final String TIME_FORMAT_NORMAL = "yyyy-MM-dd HH:mm:ss";

    private static final String TIME_FORMAT_ENGLISH = "MM/dd/yyyy HH:mm:ss";

    private static final String TIME_FORMAT_CHINA = "yyyy年MM月dd日HH时mm分ss秒";

    private static final String TIME_FORMAT_CHINA_S = "yyyy年MM月dd日HH时mm分ss秒";

    private static final String DATE_FORMAT_SHORT = "yyyyMMdd";

    private static final String DATE_FORMAT_NORMAL = "yyyy-MM-dd";

    private static final String DATE_FORMAT_ENGLISH = "MM/dd/yyyy";

    private static final String DATE_FORMAT_CHINA = "yyyy年MM月dd日";

    private static final String MONTH_FORMAT = "yyyyMM";

    private static final String DATE_FORMAT_MINUTE = "yyyyMMddHHmm";

    private static final String MONTH_DAY_FORMAT = "MM-dd";

    private static final SimpleDateFormat FORMAT = new SimpleDateFormat(DATE_FORMAT_NORMAL);

    /**
     * 改变时间 譬如： changeDate(new Date(),Calendar.DATE, 2) 就是把当前时间加两天
     * 
     * @param originDate
     *            原始时间
     * @param field
     *            改变类型
     * @param distance
     *            长度
     * @return 改变后的时间
     * @since 0.1
     */
    public static Date changeDate(Date originDate, int field, int distance) {
        Calendar c = Calendar.getInstance(Locale.CHINA);
        c.setTime(originDate);
        c.add(field, distance);
        return c.getTime();
    }

    /**
     * 把日期字符串转换为日期类型
     * 
     * @param dateObj
     *            日期对象
     * @return 日期
     * @since 0.1
     */
    public static Date convertAsDate(Object dateObj) {
        if (dateObj == null) return null;
        Date dtReturn = null;
        try {
            dtReturn = Date.class.cast(dateObj);
            if (dtReturn != null) {
                return dtReturn;
            }
        } catch (Exception e) {}
        String dateStr = dateObj.toString().trim();
        if (dateStr.length() < 1) {
            return null;
        }

        DateFormat fmt = null;
        if (dateStr.matches("\\d{14}")) {
            fmt = new SimpleDateFormat(TIME_FORMAT_SHORT);
        } else if (dateStr.matches("\\d{4}-\\d{1,2}-\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}")) {
            fmt = new SimpleDateFormat(TIME_FORMAT_NORMAL);
        } else if (dateStr.matches("\\d{1,2}/\\d{1,2}/\\d{4} \\d{1,2}:\\d{1,2}:\\d{1,2}")) {
            fmt = new SimpleDateFormat(TIME_FORMAT_ENGLISH);
        } else if (dateStr.matches("\\d{4}年\\d{1,2}月\\d{1,2}日 \\d{1,2}时\\d{1,2}分\\d{1,2}秒")) {
            fmt = new SimpleDateFormat(TIME_FORMAT_CHINA);
        } else if (dateStr.matches("\\d{8}")) {
            fmt = new SimpleDateFormat(DATE_FORMAT_SHORT);
        } else if (dateStr.matches("\\d{4}-\\d{1,2}-\\d{1,2}")) {
            fmt = new SimpleDateFormat(DATE_FORMAT_NORMAL);
        } else if (dateStr.matches("\\d{1,2}/\\d{1,2}/\\d{4}")) {
            fmt = new SimpleDateFormat(DATE_FORMAT_ENGLISH);
        } else if (dateStr.matches("\\d{4}年\\d{1,2}月\\d{1,2}日")) {
            fmt = new SimpleDateFormat(DATE_FORMAT_CHINA);
        } else if (dateStr.matches("\\d{4}\\d{1,2}\\d{1,2}\\d{1,2}\\d{1,2}")) {
            fmt = new SimpleDateFormat(DATE_FORMAT_MINUTE);
        }
        try {
            return fmt.parse(dateStr);
        } catch (ParseException e) {
            LOGGER.warn(e.getMessage(), e);
            throw new IllegalArgumentException("Date or Time String is invalid.");
        }
    }

    /**
     * 解析日期字符串
     *
     * @param indate
     *            传入字符串
     * @param parsePatterns
     *            包含日期可能格式的数组
     * @return Date 返回日期
     */
    public static Date convertAsDate(String indate, String[] parsePatterns) {
        try {
            if (indate != null)
                return org.apache.commons.lang3.time.DateUtils.parseDate(indate, parsePatterns);
        } catch (Exception e) {
            LOGGER.error("", e);
        }
        return null;
    }

    /**
     * 把 2010-02-04 12:12:34 这样的时间转换为 20100204121234
     * 
     * @param origin
     *            原始时间字符串
     * @return 转换后的字符串
     * @since 0.1
     */
    public static String convertAsShortDateString(String origin) {
        return origin == null ? origin : origin.replaceAll("[-|:|\\s|年|月|日|时|分|秒|/]", "");
    }

    /**
     * 把 20100204121234 这样的时间转换为 2010-02-04 12:12:34
     * 
     * @param origin
     *            原始时间字符串
     * @return 转换后的字符串
     * @since 0.1
     */
    public static String convertAsNormalDateString(String origin) {
        if (origin == null) {
            return origin;
        }
        try {
            if (origin.matches("\\d{8}")) {
                SimpleDateFormat fmt = new SimpleDateFormat(DATE_FORMAT_SHORT);
                return getDateNormalString(fmt.parse(origin));
            } else if (origin.matches("\\d{14}")) {
                SimpleDateFormat fmt = new SimpleDateFormat(TIME_FORMAT_SHORT);
                return getTimeNormalString(fmt.parse(origin));
            }
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
        throw new IllegalArgumentException(origin + "is invalid,String format is yyyyMMddHHmmss or yyyyMMdd");
    }

    /**
     * 得到日期字符串,格式为 yyyy年MM月dd日
     * 
     * @param date
     *            待格式化日期
     * @return 返回格式化后的日期字符串
     * @since 0.1
     */

    public static String getDateChinaString(Date date) {
        DateFormat fmt = new SimpleDateFormat(DATE_FORMAT_CHINA);
        return fmt.format(date);
    }

    /**
     * 得到日期字符串,格式为 MM/dd/yyyy
     * 
     * @param date
     *            待格式化日期
     * @return 返回格式化后的日期字符串
     * @since 0.1
     */
    public static String getDateEnglishString(Date date) {
        DateFormat fmt = new SimpleDateFormat(DATE_FORMAT_ENGLISH);
        return fmt.format(date);
    }

    /**
     * 得到日期字符串,格式为 yyyy-MM-dd
     * 
     * @param date
     *            待格式化日期
     * @return 返回格式化后的日期字符串
     * @since 0.1
     */
    public static String getDateNormalString(Date date) {
        DateFormat fmt = new SimpleDateFormat(DATE_FORMAT_NORMAL);
        return fmt.format(date);
    }

    /**
     * 得到日期字符串,格式为 MM-dd
     * 
     * @param date
     *            待格式化日期
     * @return 返回格式化后的日期字符串
     * @since 0.1
     */
    public static String getMonthDayDateNormalString(Date date) {
        DateFormat fmt = new SimpleDateFormat(MONTH_DAY_FORMAT);
        return fmt.format(date);
    }

    /**
     * 得到日期字符串,格式为 yyyyMMdd
     * 
     * @param date
     *            待格式化日期
     * @return 返回格式化后的日期字符串
     * @since 0.1
     */
    public static String getDateShortString(Date date) {
        DateFormat fmt = new SimpleDateFormat(DATE_FORMAT_SHORT);
        return fmt.format(date);
    }

    /**
     * 得到日期字符串,格式为 yyyy年MM月dd日
     * 
     * @return 返回当前日期的字符串
     */

    public static String getNowDateChinaString() {
        return getDateChinaString(new Date());
    }

    /**
     * 得到日期字符串,格式为 MM/dd/yyyy
     * 
     * @return 返回当前日期的字符串
     */
    public static String getNowDateEnglishString() {
        return getDateEnglishString(new Date());
    }

    /**
     * 得到日期字符串,格式为 yyyy-MM-dd
     * 
     * @return 返回当前日期的字符串
     */
    public static String getNowDateNormalString() {
        return getDateNormalString(new Date());
    }

    /**
     * Description:得到间隔天数 日期字符串,格式为 yyyy-MM-dd
     * 
     * @param interval
     *            间隔天数
     * @return String
     * @throws @Author
     *             hzl Create Date: 2012-12-20 下午4:44:31
     */
    public static String getNowDateNormalString(int interval) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, interval);
        return getDateNormalString(c.getTime());
    }

    /**
     * Description:得到间隔天数 日期字符串，格式为MM-dd
     * 
     * @param interval
     *            间隔天数
     * @return String
     * @throws @Author
     *             hzl Create Date: 2012-12-20 下午4:43:57
     */
    public static String getNowMonthNormalString(int interval) {
        return getNowDateNormalString(interval).substring(5, 10);
    }

    /**
     * Description:根据输入日期向后累加interval天
     * 
     * @param day
     *            日期
     * @param interval
     *            天数
     * @return String
     * @throws ParseException
     *             异常。
     * @Author hzl Create Date: 2012-12-20 下午4:43:27
     */
    public static String getIntervalDaysLater(String day, int interval) throws ParseException {
        Calendar c = Calendar.getInstance();
        c.setTime(FORMAT.parse(day));
        c.add(Calendar.DATE, interval);
        return getDateNormalString(c.getTime());
    }

    /**
     * Description:得到日期字符串,格式为 MM-dd
     * 
     * @param interval
     *            间隔天数
     * @return String
     * @throws @Author
     *             hzl Create Date: 2012-12-20 下午4:43:14
     */
    public static String getNowMonthDayDateNormalString(int interval) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, interval);
        return getMonthDayDateNormalString(c.getTime());
    }

    /**
     * 得到日期字符串,格式为 yyyyMMdd
     * 
     * @return 返回当前日期的字符串
     */
    public static String getNowDateShortString() {
        return getDateShortString(new Date());
    }

    /**
     * 得到时间字符串,格式为 yyyy年MM月dd日 HH时mm分ss秒
     * 
     * @return 返回当前时间的字符串
     */
    /*
     * public static String getNowTimeChinaString() { return
     * getTimeChinaString(new Date()); }
     */

    /**
     * 得到时间字符串,格式为 MM/dd/yyyy HH:mm:ss
     * 
     * @return 返回当前时间的字符串
     */
    public static String getNowTimeEnglishString() {
        return getTimeEnglishString(new Date());
    }

    /**
     * 得到时间字符串,格式为 yyyy-MM-dd HH:mm:ss
     * 
     * @return 返回当前时间的字符串
     */
    public static String getNowTimeNormalString() {
        return getTimeNormalString(new Date());
    }

    /**
     * 得到时间字符串,格式为 yyyyMMddHHmmss
     * 
     * @return 返回当前时间的字符串
     */
    public static String getNowTimeShortString() {
        return getTimeShortString(new Date());
    }

    /**
     * 得到时间字符串,格式为 yyyy年MM月dd日 HH:mm:ss
     * 
     * @param date
     *            待格式化日期
     * @return 返回格式化后的时间字符串
     * @since 0.1
     */
    public static String getTimeChinaString(Date date) {
        DateFormat fmt = new SimpleDateFormat(TIME_FORMAT_CHINA);
        return fmt.format(date);
    }

    /**
     * Description:根据时间，返回中文日期，带时分秒
     * 
     * @param date
     *            日期
     * @return 中文日期，带时分秒
     * @Author hzl Create Date: 2013-1-16 下午4:16:36
     */
    public static String getTimeChinaStringS(Date date) {
        DateFormat fmt = new SimpleDateFormat(TIME_FORMAT_CHINA_S);
        return fmt.format(date);
    }

    /**
     * 得到时间字符串,格式为 MM/dd/yyyy HH:mm:ss
     * 
     * @param date
     *            待格式化日期
     * @return 返回格式化后的时间字符串
     * @since 0.1
     */
    public static String getTimeEnglishString(Date date) {
        DateFormat fmt = new SimpleDateFormat(TIME_FORMAT_ENGLISH);
        return fmt.format(date);
    }

    /**
     * 得到时间字符串,格式为 yyyy-MM-dd HH:mm:ss
     * 
     * @param date
     *            待格式化日期
     * @return 返回格式化后的时间字符串
     * @since 0.1
     */
    public static String getTimeNormalString(Date date) {
        DateFormat fmt = new SimpleDateFormat(TIME_FORMAT_NORMAL);
        return fmt.format(date);
    }

    /**
     * 得到时间字符串,格式为 yyyyMMddHHmmss
     * 
     * @param date
     *            待格式化日期
     * @return 返回格式化后的时间字符串
     * @since 0.1
     */
    public static String getTimeShortString(Date date) {
        DateFormat fmt = new SimpleDateFormat(TIME_FORMAT_SHORT);
        return fmt.format(date);
    }

    /**
     * Description:得到时间字符串,格式为 yyyyMM
     * 
     * @param date
     *            时间
     * @return String
     * @throws @Author
     *             hzl Create Date: 2012-12-20 下午4:42:50
     */
    public static String getMonthString(Date date) {
        DateFormat fmt = new SimpleDateFormat(MONTH_FORMAT);
        return fmt.format(date);
    }

    /**
     * Description:得到时间字符串,格式为 M月d日
     * 
     * @param day
     *            相隔几天
     * @return 时间字符串,格式为 M月d日
     * @Author hzl Create Date: 2013-1-16 下午4:19:18
     */
    public static String getMonthDay(int day) {
        DateFormat fmt = new SimpleDateFormat("M月d日");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, day);
        return fmt.format(c.getTime());
    }

    /**
     * Description:获得日期集合
     * 
     * @param beginDate
     *            开始日期
     * @param endDate
     *            结束日期
     * @return List<String>
     * @throws ParseException
     *             异常
     * @Author hzl Create Date: 2012-12-20 下午4:42:28
     */
    public static List<String> getDateList(String beginDate, String endDate) throws ParseException {
        List<String> list = new ArrayList<String>();
        Calendar c1 = Calendar.getInstance(Locale.CHINA);
        c1.setTime(FORMAT.parse(beginDate));
        Calendar c2 = Calendar.getInstance(Locale.CHINA);
        c2.setTime(FORMAT.parse(endDate));
        long l1 = c1.getTimeInMillis();
        long l2 = c2.getTimeInMillis();
        int interval = (int) ((l2 - l1) / (1000 * 60 * 60 * 24));
        for (int i = 0; i <= interval; i++) {
            Calendar c = Calendar.getInstance();
            c.setTime(c1.getTime());
            c.add(Calendar.DATE, i);
            list.add(FORMAT.format(c.getTime()));
        }
        return list;
    }

    /**
     * Description: 获得输入日期所在周的第一天（周一为第一天）
     * 
     * @param day
     *            日期
     * @return String
     * @throws ParseException
     *             异常
     * @Author hzl Create Date: 2012-12-20 下午4:42:11
     */
    public static String getFirstDayOfWeek(String day) throws ParseException {
        Calendar c1 = Calendar.getInstance(Locale.CHINA);
        c1.setTime(FORMAT.parse(day));
        c1.setFirstDayOfWeek(Calendar.MONDAY);
        c1.set(Calendar.DAY_OF_WEEK, c1.getFirstDayOfWeek());
        return FORMAT.format(c1.getTime());
    }

    /**
     * Description:获得输入日期所在周的最后一天（周日为最后一天）
     * 
     * @param day
     *            日期
     * @return String
     * @throws ParseException
     *             异常
     * @Author hzl Create Date: 2012-12-20 下午4:41:32
     */
    public static String getLastDayOfWeek(String day) throws ParseException {
        Calendar c1 = Calendar.getInstance(Locale.CHINA);
        c1.setTime(FORMAT.parse(day));
        c1.setFirstDayOfWeek(Calendar.MONDAY);
        c1.set(Calendar.DAY_OF_WEEK, c1.getFirstDayOfWeek() + 6);
        return FORMAT.format(c1.getTime());
    }

    /**
     * Description:计算输入日期所在月的最后一天
     * 
     * @param day
     *            日期
     * @return String
     * @throws ParseException
     *             异常
     * @Author hzl Create Date: 2012-12-20 下午4:41:06
     */
    public static String getLastDayOfMonth(String day) throws ParseException {
        Calendar c1 = Calendar.getInstance(Locale.CHINA);
        c1.setTime(FORMAT.parse(day));
        c1.set(Calendar.DAY_OF_MONTH, 1);
        c1.add(Calendar.MONTH, 1);
        c1.add(Calendar.DAY_OF_MONTH, -1);
        return FORMAT.format(c1.getTime());
    }

    /**
     * Description:计算输入日期所在月的最后一天
     * 
     * @param day
     *            日期
     * @return String
     * @throws ParseException
     *             异常
     * @Author hzl Create Date: 2012-12-20 下午4:40:49
     */
    public static String getFirstDayOfMonth(String day) throws ParseException {
        Calendar c1 = Calendar.getInstance(Locale.CHINA);
        c1.setTime(FORMAT.parse(day));
        c1.set(Calendar.DAY_OF_MONTH, 1);
        return FORMAT.format(c1.getTime());
    }

    /**
     * Description:是否是当月的第一天
     * 
     * @param day
     *            日期
     * @return boolean
     * @throws ParseException
     *             异常
     * @Author hzl Create Date: 2012-12-20 下午4:40:31
     */
    public static boolean isFirstDayOfMonth(String day) throws ParseException {
        return day.equals(getFirstDayOfMonth(day)) ? true : false;
    }

    /**
     * Description:是否是当月的最后一天
     * 
     * @param day
     *            日期
     * @return boolean
     * @throws ParseException
     *             异常
     * @Author hzl Create Date: 2012-12-20 下午4:40:05
     */
    public static boolean isLastDayOfMonth(String day) throws ParseException {
        return day.equals(getLastDayOfMonth(day)) ? true : false;
    }

    /**
     * Description: 是否是当前周的第一天
     * 
     * @param day
     *            日期
     * @return boolean
     * @throws ParseException
     *             异常。
     * @Author hzl Create Date: 2012-12-20 下午4:33:51
     */
    public static boolean isFirstDayOfWeek(String day) throws ParseException {
        return day.equals(getFirstDayOfWeek(day)) ? true : false;
    }

    /**
     * Description:是否是当前周的最后一天
     * 
     * @param day
     *            日期
     * @return boolean
     * @throws ParseException
     *             异常。
     * @Author hzl Create Date: 2012-12-20 下午4:33:37
     */
    public static boolean isLastDayOfWeek(String day) throws ParseException {
        return day.equals(getLastDayOfWeek(day)) ? true : false;
    }

    /**
     * Description: 计算输入日期所在周是否跨月
     * 
     * @param day
     *            日期
     * @return boolean
     * @throws ParseException
     *             异常。
     * @Author hzl Create Date: 2012-12-20 下午4:33:21
     */
    public static boolean isDayBetweenMonthAndWeek(String day) throws ParseException {
        String firstDayOfMonth = getFirstDayOfMonth(day);
        String firstDayOfWeek = getFirstDayOfWeek(day);
        Pattern p = Pattern.compile("\\d{4}-\\d{2}-01");
        Matcher m = p.matcher(day);
        if (firstDayOfWeek.compareTo(firstDayOfMonth) < 0 && !m.matches()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Description:输入的日期是否为时间格式
     * 
     * @param input
     *            输入日期。
     * @return boolean
     * @throws @Author
     *             hzl Create Date: 2012-12-20 下午4:32:53
     */
    public static boolean isInputDate(String input) {
        return input.matches("\\d{4}-\\d{1,2}-\\d{1,2}");
    }

    /**
     * Description:
     * 
     * @param input
     *            输入日期。
     * @return boolean
     * @throws @Author
     *             hzl Create Date: 2012-12-20 下午4:32:48
     */
    public static boolean isInputDateNormalString(String input) {
        return input.matches("\\d{4}-\\d{2}-\\d{2}");
    }

    /**
     * Description:将日期字符串格式化为yyyy-MM-dd
     * 
     * @param input
     *            输入日期。
     * @return String
     * @throws IllegalArgumentException
     *             异常。
     * @Author hzl Create Date: 2012-12-20 下午4:32:25
     */
    public static String formatDateString(String input) throws IllegalArgumentException {
        String month = "";
        String day = "";
        try {
            if (!isInputDate(input)) {
                throw new IllegalArgumentException("Illegal date format!");
            }
            if (isInputDateNormalString(input)) {
                return input;
            }
            month = input.substring(input.indexOf("-") + 1, input.lastIndexOf("-"));
            if (month.length() == 1 && month.indexOf("0") != 0) {
                month = "0" + month;
            }
            day = input.substring(input.lastIndexOf("-") + 1, input.length());
            if (day.length() == 1 && day.indexOf("0") != 0) {
                day = "0" + day;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return input;
        }
        return input.substring(0, 4) + "-" + month + "-" + day;
    }

    /**
     * Description:比较两个日期相差的天数
     * 
     * @param date1
     *            日期
     * @param date2
     *            日期
     * @return int
     * @throws @Author
     *             hzl Create Date: 2012-12-20 下午4:39:11
     */
    public static int getDay(Date date1, Date date2) {
        long day = 24L * 60L * 60L * 1000L;
        long d1 = date1.getTime();
        long d2 = date2.getTime();
        return (int) ((d2 - d1) / day);
    }

    /**
     * Description:获取日期字符串 yyyy-MM-dd HH:mm:ss 格式。
     * 
     * @param dateTime
     *            日期
     * @return String
     * @throws @Author
     *             hzl Create Date: 2012-12-20 下午4:38:45
     */
    public static String getDateToString(Date dateTime) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = format.format(dateTime);
        return str;
    }

    /**
     * Description:比较两个日期天数
     * 
     * @param strBegin
     *            开始时间
     * @param strEnd
     *            结束时间
     * @return int
     * @throws @Author
     *             hzl Create Date: 2012-12-20 下午4:38:18
     */
    public int getDifferDays(String strBegin, String strEnd) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = null, date2 = null;
        int days = 0;
        try {
            date1 = f.parse(strBegin);
            date2 = f.parse(strEnd);
            days = (int) ((date2.getTime() - date1.getTime()) / 86400000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return days;
    }

    /**
     * Description:年月日加上时分秒
     * 
     * @param date
     *            日期
     * @param map
     *            map
     * @param keyName
     *            key
     * @throws ParseException
     *             转换异常。
     * @Author hzl Create Date: 2012-12-20 下午4:38:02
     */
    public static void setDateHms(String date, Map<String, Object> map, String keyName) throws ParseException {
        if (date != null && date.length() > 0) {
            date = date.trim() + " 00:00:00";
            map.put(keyName,
                    DateUtils.parseDate(date, new String[] { "yyyy-MM-dd HH:mm:ss" }));
        }
    }

    /**
     * Description:年月日加上时分秒,并且日期加一天.
     * 
     * @param date
     *            日期
     * @param map
     *            map
     * @param keyName
     *            key
     * @throws ParseException
     *             转换异常。
     * @Author hzl Create Date: 2012-12-20 下午4:36:40
     */
    public static void setDateHmsAddOneDay(String date, Map<String, Object> map, String keyName) throws ParseException {
        if (date != null && date.length() > 0) {
            date = date.trim() + " 00:00:00";
            Calendar end = Calendar.getInstance();
            end.setTime(DateUtils.parseDate(date, new String[] { "yyyy-MM-dd HH:mm:ss" }));
            end.add(Calendar.DAY_OF_MONTH, 1);
            map.put(keyName, end.getTime());
        }
    }

    /**
     * Description:比较两个日期大小
     * 
     * @param date1
     *            日期
     * @param date2
     *            日期
     * @return int
     * @throws @Author
     *             hzl Create Date: 2012-12-20 下午4:35:10
     */
    public static int compareDate(String date1, String date2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            if (date1 != null && !date1.equals("") && (date2 != null && !date2.equals(""))) {
                Date dt1 = df.parse(date1);
                Date dt2 = df.parse(date1);
                if (dt1.getTime() > dt2.getTime()) {
                    return 1;
                } else if (dt1.getTime() < dt2.getTime()) {
                    return -1;
                } else {
                    return 0;
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    /**
     * Description:获得本周开始的第一天的日期，规定一周开始的第一天为星期日
     * 
     * @param
     * @return String
     * @throws @Author
     *             hzl Create Date: 2012-12-20 下午4:35:03
     */
    public static String getFirstDayOfWeek() {
        Calendar calendar = Calendar.getInstance();
        int minus = calendar.get(GregorianCalendar.DAY_OF_WEEK);
        calendar.add(GregorianCalendar.DATE, -minus + 1);
        String begin = new java.sql.Date(calendar.getTime().getTime()).toString();
        return begin;
    }

    /**
     * Description:获得某月的最后一天
     * 
     * @param date
     *            日期
     * @return Date
     * @throws @Author
     *             hzl Create Date: 2012-12-20 下午4:34:24
     */
    public static Date lastDayOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.roll(Calendar.DAY_OF_MONTH, -1);
        return cal.getTime();
    }

    /**
     * Description:字符串转日期格式
     * 
     * @param strDate
     *            日期
     * @param pattern
     *            格式
     * @return Date
     * @throws @Author
     *             hzl Create Date: 2012-12-20 下午4:34:06
     */
    public static synchronized Date parseDateFormat(String strDate, String pattern) {
        synchronized (FORMAT) {
            Date date = null;
            FORMAT.applyPattern(pattern);
            try {
                date = FORMAT.parse(strDate);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return date;
        }
    }

    /**
     * Description:字符串转日期格式
     * 
     * @param strDate
     *            日期
     * @param pattern
     *            格式
     * @return Date
     * @throws @Author
     *             hzl Create Date: 2012-12-20 下午4:34:06
     */
    public static synchronized Date addDate(Date date, int field, int amount) {
        synchronized (FORMAT) {
            Calendar c = Calendar.getInstance(Locale.CHINA);
            c.setTime(date);
            c.add(field, amount);
            return c.getTime();
        }
    }
}
