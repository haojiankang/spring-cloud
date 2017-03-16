package com.github.haojiankang.framework.commons.utils.bean;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.MethodUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.NumberUtils;
import org.springframework.util.ReflectionUtils;

import com.github.haojiankang.framework.commons.utils.lang.DateTimeUtil;
import com.github.haojiankang.framework.commons.utils.lang.StringUtil;

/**
 * Java对象工具类.
 * 
 * @version 1.0.0
 */
public class ObjectUtils {

    /**
     * 判断字符串是否为空：Null、零长度或仅包含空白符
     * 
     * @param o
     *            待判断字符串
     * @return 空true，非空false
     */
    public static boolean isEmpty(String o) {
        return (o == null || o.length() < 1 || o.trim().length() < 1);
    }

    /**
     * 判断StringBuffer是否为空：Null、零长度或仅包含空白符
     * 
     * @param o
     *            待判断StringBuffer
     * @return 空true，非空false
     */
    public static boolean isEmpty(StringBuffer o) {
        return (o == null || o.length() < 1 || o.toString().trim().length() < 1);
    }

    /**
     * 判断集合是否为空：Null或不包含任何元素
     * 
     * @param o
     *            集合
     * @return 空true，非空false
     */
    public static boolean isEmpty(Collection<?> o) {
        return (o == null || o.isEmpty());
    }

    /**
     * 判断Map对象是否为空：Null或不包含任何元素
     * 
     * @param o
     *            Map对象
     * @return 空返回真，否则返回假
     */
    public static boolean isEmpty(Map<?, ?> o) {
        return (o == null || o.isEmpty());
    }

    /**
     * 可以用于判断变量是否为空
     * 
     * @param o
     *            Object,String,StringBuffer,Collection,Map,Array
     * @return 空返回真，否则返回假
     */
    public static boolean isEmpty(Object o) {
        if (o == null)
            return true;
        if (o.getClass().isArray() && Array.getLength(o) < 1)
            return true;
        if (o instanceof String)
            return isEmpty((String) o);
        if (o instanceof StringBuffer)
            return isEmpty((StringBuffer) o);
        if (ClassUtils.isAssignable(o.getClass(), Collection.class))
            return isEmpty((Collection<?>) o);
        if (ClassUtils.isAssignable(o.getClass(), Map.class))
            return isEmpty((Map<?, ?>) o);
        return false;
    }

    /**
     * 实例对象类型转换(String.valueOf会导致返回字符串"NULL")
     * 
     * @param value
     *            要转换的对象
     * @param requiredType
     *            转换后的类型
     * @return 转换后的对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T convert(Object value, Class<T> requiredType) {
        if (value == null)
            return (T) null;
        if (value.getClass().equals(requiredType)) {
            return (T) value;
        } else if (java.lang.String.class.equals(requiredType)) {
            return (T) value.toString();
        } else if (java.util.Date.class.equals(requiredType)) {
            return (T) DateTimeUtil.convertAsDate(value.toString());
        } else if (java.lang.Number.class.isAssignableFrom(requiredType)) {
            if (value instanceof Number) {
                // Convert original Number to target Number class.
                return (T) NumberUtils.convertNumberToTargetClass(((Number) value), (Class<Number>) requiredType);
            } else {
                // Convert stringified value to target Number class.
                return (T) NumberUtils.parseNumber(value.toString(), (Class<Number>) requiredType);
            }
        } else if (requiredType.isEnum()) {
            try {
                Method valueOf = requiredType.getMethod("valueOf", String.class);
                return (T) ReflectionUtils.invokeMethod(valueOf, requiredType, value.toString());
            } catch (Exception e) {
                return null;
            }
        } else {
            return (T) ConvertUtils.convert(value, requiredType);
        }
    }

    /**
     * 判断是否为基本类型
     * 
     * @param clazz
     *            Class类型
     * @return 是基本类型返回true，否则返回false
     */
    public static <T> boolean isBasicTypes(Class<T> clazz) {
        if (clazz.isPrimitive() || clazz.isEnum() || clazz.equals(String.class)
                || clazz.equals(java.math.BigDecimal.class) || clazz.equals(java.util.Date.class)
                || clazz.equals(java.sql.Date.class) || clazz.equals(java.sql.Time.class)
                || clazz.equals(java.sql.Timestamp.class) || clazz.equals(Byte.class) || clazz.equals(Short.class)
                || clazz.equals(Integer.class) || clazz.equals(Long.class) || clazz.equals(Float.class)
                || clazz.equals(Double.class) || clazz.equals(Boolean.class) || clazz.equals(Character.class))
            return true;
        return false;
    }

    /**
     * 返回对象在数组中的位置
     * 
     * @param target
     *            待检索数组
     * @param o
     *            待检索对象
     * @return 对象在数组中的索引位置，不在数组中返回-1
     */
    public static int indexOfArray(Object[] target, Object o) {
        if (isEmpty(target) || isEmpty(o))
            return -1;
        for (int i = 0; i < target.length; i++) {
            if (isEmpty(target[i]))
                continue;
            if (target[i].equals(o))
                return i;
        }
        return -1;
    }

    /**
     * List&lt;Integer&gt;转为int[]
     * 
     * @param lst
     *            整数集合
     * @return 整数数组
     */
    public static int[] integerList2IntArray(List<Integer> lst) {
        if (lst == null || lst.isEmpty())
            return null;
        int[] intTypes = new int[lst.size()];
        for (int i = 0; i < intTypes.length; i++)
            intTypes[i] = lst.get(i).intValue();
        return intTypes;
    }

    /**
     * 实例类方法
     * 
     * @param bean
     *            类实例
     * @param method
     *            方法名
     * @param parameters
     *            方法参数
     * @param parameterTypes
     *            参数类型
     * @return
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    public static Object getObjectByBean(Object bean, String method, Object[] parameters, Class[] parameterTypes)
            throws Exception {
        if (parameters != null && parameterTypes != null && parameters.length == parameterTypes.length)
            return MethodUtils.invokeMethod(bean, StringUtil.trimToEmpty(method), parameters, parameterTypes);
        else
            return MethodUtils.invokeMethod(bean, StringUtil.trimToEmpty(method), parameters);
    }

    public static Object getObjectByBean(Object bean, String method, Object[] parameters, String[] parameterTypes)
            throws Exception {
        return getObjectByBean(bean, method, parameters, classForNames(parameterTypes));
    }

    /**
     * 类静态方法
     * 
     * @param clazz
     *            类
     * @param method
     *            方法名
     * @param parameters
     *            方法参数
     * @param parameterTypes
     *            参数类型
     * @return
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    public static Object getObjectByClass(Class<?> clazz, String method, Object[] parameters, Class[] parameterTypes)
            throws Exception {
        if (parameters != null && parameterTypes != null && parameters.length == parameterTypes.length)
            return MethodUtils.invokeStaticMethod(clazz, StringUtil.trimToEmpty(method), parameters, parameterTypes);
        else
            return MethodUtils.invokeStaticMethod(clazz, StringUtil.trimToEmpty(method), parameters);
    }

    public static Object getObjectByClass(Class<?> clazz, String method, Object[] parameters, String[] parameterTypes)
            throws Exception {
        return getObjectByClass(clazz, method, parameters, classForNames(parameterTypes));
    }

    /**
     * 实例类方法
     * 
     * @param bean
     *            类实例
     * @param method
     *            方法名
     * @param parameters
     *            方法参数
     * @param parameterTypes
     *            参数类型
     * @return
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    public static Object[] getArrayByBean(Object bean, String method, Object[] parameters, Class[] parameterTypes)
            throws Exception {
        Object result = getObjectByBean(bean, method, parameters, parameterTypes);
        if (result instanceof Collection)
            return ((Collection<?>) result).toArray();
        else if (result.getClass().isArray())
            return (Object[]) result;
        else
            throw new Exception((new StringBuffer("错误的返回值: ")).append(result.getClass()).toString());
    }

    public static Object[] getArrayByBean(Object bean, String method, Object[] parameters, String[] parameterTypes)
            throws Exception {
        return getArrayByBean(bean, method, parameters, classForNames(parameterTypes));
    }

    /**
     * 类静态方法
     * 
     * @param clazz
     *            类
     * @param method
     *            方法名
     * @param parameters
     *            方法参数
     * @param parameterTypes
     *            参数类型
     * @return
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    public static Object[] getArrayByClass(Class<?> clazz, String method, Object[] parameters, Class[] parameterTypes)
            throws Exception {
        Object result = getObjectByClass(clazz, method, parameters, parameterTypes);
        if (result instanceof Collection)
            return ((Collection<?>) result).toArray();
        else if (result.getClass().isArray())
            return (Object[]) result;
        else
            throw new Exception((new StringBuffer("错误的返回值: ")).append(result.getClass()).toString());
    }

    public static Object[] getArrayByClass(Class<?> clazz, String method, Object[] parameters, String[] parameterTypes)
            throws Exception {
        return getArrayByClass(clazz, method, parameters, classForNames(parameterTypes));
    }

    /**
     * 从类名得到Class类
     */
    @SuppressWarnings("rawtypes")
    private static Class[] classForNames(String[] classNames) throws ClassNotFoundException {
        Class[] clazz = null;
        if (classNames != null) {
            clazz = new Class[classNames.length];
            for (int i = 0; i < classNames.length; i++)
                clazz[i] = Class.forName(classNames[i]);
        }
        return clazz;
    }

    /**
     * 比较字符串是否相等
     * 
     * @param src
     * @param des
     * @return
     */
    public static boolean eq(Object src, Object des) {
        return src == des ? true : src == null || des == null ? false : src.equals(des);
    }
}