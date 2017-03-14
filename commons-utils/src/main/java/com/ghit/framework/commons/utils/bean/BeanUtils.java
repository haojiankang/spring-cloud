package com.ghit.framework.commons.utils.bean;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ghit.framework.commons.utils.lang.StringUtil;

/**
 * Java简单对象工具类.
 * <p/>
 * 注意：Bean的属性声明不能有基本类型，因为对类似int、boolean无法赋Null值.
 * 
 * @version 1.0.0
 */
@SuppressWarnings("unchecked")
public class BeanUtils {
    private static Logger log = LoggerFactory.getLogger(BeanUtils.class);
    private static final String[] SIMPLE_TYPE = new String[] { "int", "byte", "short", "long", "float", "double",
            "char", "boolean", "java.lang.Integer", "java.lang.Character", "java.lang.Long", "java.lang.Short",
            "java.lang.Byte", "java.lang.String", "java.lang.Float", "java.lang.Double", "java.lang.Boolean",
            "java.util.Date" };

    /**
     * 
     * poVo:将PO转换为VO. VO和PO中，如果使用List、Set、Map等集合对象类型需要注意：<br/>
     * 1.需要支持使用无参数构造函数创建的对象，集合类型不能为null；<br/>
     * 2.泛型为基本类型时必须明确的给出泛型的类型。<br/>
     * 3.字符类型泛型统一使用String，而不要使用StringBuilder。<br/>
     * 4.日期类型，统一使用java.util.Date.<br/>
     * 5.声明的泛型不能是接口类型，必须提供无参构造函数。</br>
     *
     * @author ren7wei
     * @param po
     * @param voClass
     * @param execFields
     * @return
     * @since JDK 1.8
     */
    public static <PO, VO> VO poVo(PO po, Class<VO> voClass, String listExper) {
        listExper = listExper == null ? "" : listExper;
        String[] execFields = listExper.split(",");
        return oToO(po, voClass, execFields);
    }

    public static <PO, VO> List<VO> poVoList(Collection<PO> pos, Class<VO> voClass, String listExper) {
        List<VO> list = new ArrayList<>();
        pos.forEach(t -> {
            list.add(poVo(t, voClass, listExper));
        });
        return list;
    }

    /**
     * 
     * poVo:将PO转换为VO. VO和PO中，如果使用List、Set、Map等集合对象类型需要注意：<br/>
     * 1.需要支持使用无参数构造函数创建的对象，集合类型不能为null；<br/>
     * 2.泛型为基本类型时必须明确的给出泛型的类型。<br/>
     * 3.字符类型泛型统一使用String，而不要使用StringBuilder。<br/>
     * 4.日期类型，统一使用java.util.Date.<br/>
     * 5.声明的泛型不能是接口类型，必须提供无参构造函数。</br>
     *
     * @author ren7wei
     * @param po
     * @param voClass
     * @param execFields
     * @return
     * @since JDK 1.8
     */
    @SuppressWarnings("rawtypes")
    private static <S, D> D oToO(S po, Class<D> voClass, String[] execFields) {
        Class<S> poClass = (Class<S>) po.getClass();
        try {
            D vo = voClass.newInstance();
            Field[] voFields = getAllFields(voClass);
            Field[] poFields = getAllFields(poClass);
            accessible(voFields);
            accessible(poFields);
            for (Field poField : poFields) {
                Field voField = findFieldByName(voFields, poField.getName());
                if (voField != null) {
                    if (isSimpleType(poField.getType())) {
                        // 简单类型直接赋值
                        copyField(poField, po, voField, vo);
                    } else {
                        // 判断当前属性是否在需要处理的属性列表中
                        if (StringUtil.in(poField.getName(), execFields)) {
                            // 集合类型处理
                            if (isObjects(poField.getType())) {
                                Type genericType = voField.getGenericType();
                                ParameterizedType paramType = (ParameterizedType) genericType;
                                Type[] types = paramType.getActualTypeArguments();
                                // map类型暂不处理。
                                if (Map.class.isAssignableFrom(poField.getType())) {
                                    // Collection类型处理
                                } else if (Collection.class.isAssignableFrom(poField.getType())) {
                                    Collection voColle = (Collection<?>) voField.get(vo);
                                    Collection poColle = (Collection<?>) poField.get(po);
                                    // 简单类型直接拷贝
                                    if (isSimpleType((Class) types[0])) {
                                        voColle.addAll(poColle);
                                        // 复杂类型递归拷贝
                                    } else {
                                        if (poColle.size() > 0) {
                                            poColle.forEach(o -> {
                                                Object oToO = oToO(o, (Class) types[0], execFields);
                                                voColle.add(oToO);
                                            });
                                        }
                                    }
                                }
                                // 非集合类型处理
                            } else {
                                voField.set(vo, oToO(poField.get(po), voField.getType(), execFields));
                            }
                        }
                    }

                }
            }
            return vo;
        } catch (InstantiationException | IllegalAccessException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    public static Field[] getAllFields(Class<?> cls) {
        List<Field> list = new ArrayList<>();
        while (cls != Object.class) {
            Field[] fs = cls.getDeclaredFields();
            for (Field f : fs) {
                if (Modifier.isStatic(f.getModifiers()) || Modifier.isFinal(f.getModifiers()))
                    continue;
                list.add(f);
            }
            cls = cls.getSuperclass();
        }
        return list.toArray(new Field[list.size()]);
    }

    /**
     * 
     * tryCopyField:尝试复制属性值到指定对象中.
     *
     * @author ren7wei
     * @param srcField
     * @param src
     * @param disFields
     * @param dis
     * @return
     * @since JDK 1.8
     */
    public static boolean tryCopyField(Field srcField, Object src, Field[] disFields, Object dis) {
        Field disField = findFieldByName(disFields, srcField.getName());
        if (disField == null)
            return false;
        try {
            copyField(srcField, src, disField, dis);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            log.error(e.getMessage(), e);
        }
        return true;
    }

    /**
     * 
     * copyField:复制属性值到目标对象中.
     *
     * @author ren7wei
     * @param srcField
     * @param src
     * @param disField
     * @param dis
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @since JDK 1.8
     */
    public static void copyField(Field srcField, Object src, Field disField, Object dis)
            throws IllegalArgumentException, IllegalAccessException {
        disField.set(dis, srcField.get(src));
    }

    /**
     * 3 findFieldByName:根据属性名称查找属性.
     *
     * @author ren7wei
     * @param fields
     * @param name
     * @return
     * @since JDK 1.8
     */
    public static Field findFieldByName(Field[] fields, String name) {
        for (int i = 0; i < fields.length; i++) {
            if (fields[i].getName().equals(name))
                return fields[i];
        }
        return null;
    }

    /**
     * 
     * accessible:修改属性的可见范围.
     *
     * @author ren7wei
     * @param fields
     * @since JDK 1.8
     */
    public static void accessible(Field... fields) {
        for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(true);
        }
    }

    /**
     * 
     * isSimpleType:判断属性是否为简单类型.
     *
     * @author ren7wei
     * @param field
     * @return
     * @since JDK 1.8
     */
    public static boolean isSimpleType(Class<?> classType) {
        return StringUtil.in(classType.getName(), SIMPLE_TYPE);
    }

    /**
     * 
     * isObjects:判断是否是集合类型，包含Map和Collection.
     *
     * @author ren7wei
     * @param field
     * @return
     * @since JDK 1.8
     */
    public static boolean isObjects(Class<?> clas) {
        return Collection.class.isAssignableFrom(clas) || Map.class.isAssignableFrom(clas);
    }

    /**
     * 非null值域去除空白字符，null值域返回null。 依赖于apache.commons.lang3
     * 
     * @param bean
     *            实例对象
     */
    public static void trimStringField(Object bean) {
        if (bean == null) {
            return;
        }
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields)
            try {
                if (field.getType() == String.class && field.get(bean) != null)
                    FieldUtils.writeField(field, bean, ((String) (field.get(bean))).trim(), true);
            } catch (Exception e) {
            }
    }

    /**
     * 非null值域去除空白字符，null值域返回零长度字符串 依赖于apache.commons.lang3
     * 
     * @param bean
     *            实例对象
     */
    public static void safeStringField(Object bean) {
        if (bean == null) {
            return;
        }
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields)
            try {
                if (field.getType() == String.class)
                    FieldUtils.writeField(field, bean, StringUtil.trimToEmpty(field.get(bean)), true);
            } catch (Exception e) {
            }
    }

    /**
     * 通过对象set方法设置属性值，属性名称大小写敏感，支持嵌套属性、Map属性和Array属性。
     * 
     * @param bean
     *            实例对象
     * @param propertyName
     *            eg : Simple (name), Nested (name1.name2.name3), Indexed
     *            (name[index]), Mapped (name(key)), Combined
     *            (name1.name2[index].name3(key))
     * @param value
     *            要设置的值
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @see org.apache.commons.beanutils.PropertyUtilsBean
     */
    public static void setProperty(Object bean, String propertyName, Object value)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        PropertyUtilsBean pub = new PropertyUtilsBean();
        pub.setProperty(bean, StringUtil.lowercaseFirst(propertyName), value);
    }

    /**
     * Map对象转换成类对象，对象属性必须支持set方法.
     * 
     * @param map
     *            Map对象
     * @param beanClass
     *            要生成的对象类
     * @param sensitive
     *            类属性是否大小写敏感
     * @return 实例对象
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public static <T> T map2Bean(Map<String, Object> map, Class<T> beanClass, boolean sensitive)
            throws InstantiationException, IllegalAccessException {
        T bean = beanClass.newInstance();
        Entry<String, Object> entry = null;
        Iterator<Entry<String, Object>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            entry = it.next();
            try {
                if (sensitive == true)
                    setProperty(bean, entry.getKey(), entry.getValue());
                else
                    setBeanProperty(bean, entry.getKey(), entry.getValue());
            } catch (Exception e) {
            }
        }
        return bean;
    }

    /**
     * 类对象转换成Map对象，对象属性必须支持get方法。
     * 
     * @param bean
     *            实例对象
     * @return Map对象
     * @throws IntrospectionException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    public static Map<String, Object> bean2Map(Object bean)
            throws IntrospectionException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (bean == null)
            return null;

        Method getter = null;
        String propertyName = null;
        Map<String, Object> map = new HashMap<String, Object>();
        BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

        for (PropertyDescriptor property : propertyDescriptors) {
            propertyName = property.getName();
            if (!propertyName.equals("class")) {// 过滤class属性
                getter = property.getReadMethod();// 得到property对应的getter方法
                map.put(propertyName, getter.invoke(bean, new Object[0]));
            }
        }
        return map;
    }

    /**
     * 查找对象属性的set方法，同时通过属性值区分重载方法
     * 
     * @param bean
     *            实例对象
     * @param propertyName
     *            属性名称，不区分大小写
     * @param value
     *            属性值，用于区分重载方法
     * @return 实例对象方法
     */
    public static Method findSetMethod(Object bean, String propertyName, Object value) {
        if (bean == null) {
            return null;
        }
        if (propertyName == null || propertyName.trim().length() < 1) {
            return null;
        }

        int idxNameAndParmMatch = -1;
        int idxNameMatchParmAssignable = -1;
        int idxNameSimilarParmMatch = -1;
        int idxNameSimilarParmAssignable = -1;
        int idxOnlyNameMatch = -1;
        int idxOnlyNameSimilar = -1;
        Class<?>[] classArr = null;
        Method setMethod = null;
        Method[] methods = null;
        String setMethodName = propertySetMehodName(propertyName);

        methods = bean.getClass().getMethods();
        if (methods == null) {
            return null;
        }

        for (int i = 0; i < methods.length; i++) {
            setMethod = methods[i];
            classArr = setMethod.getParameterTypes();
            if (classArr.length != 1)
                continue;// 无赋值参数的跳过

            if (setMethod.getName().equals(setMethodName)) {// 方法名匹配
                if (value == null) {
                    idxOnlyNameMatch = i;
                    break;
                } else if (classArr[0] == value.getClass()) {// 参数类型匹配
                    idxNameAndParmMatch = i;
                    break;
                } else if (ClassUtils.isAssignable(value.getClass(), classArr[0])) {// 参数类型可转化
                    idxNameMatchParmAssignable = i;
                } else {// 仅方法名称匹配
                    idxOnlyNameMatch = i;
                }

            } else if (setMethod.getName().equalsIgnoreCase(setMethodName)) {// 方法名忽略大小写匹配
                if (value == null) {
                    idxOnlyNameSimilar = i;
                } else if (classArr[0] == value.getClass()) {// 参数类型匹配
                    idxNameSimilarParmMatch = i;
                } else if (ClassUtils.isAssignable(value.getClass(), classArr[0])) {// 参数类型可转化
                    idxNameSimilarParmAssignable = i;
                } else {// 仅方法名忽略大小写匹配
                    idxOnlyNameSimilar = i;
                }
            }
        }
        if (idxNameAndParmMatch > -1) {
            setMethod = methods[idxNameAndParmMatch];
        } else if (idxNameMatchParmAssignable > -1) {
            setMethod = methods[idxNameMatchParmAssignable];
        } else if (idxNameSimilarParmMatch > -1) {
            setMethod = methods[idxNameSimilarParmMatch];
        } else if (idxNameSimilarParmAssignable > -1) {
            setMethod = methods[idxNameSimilarParmAssignable];
        } else if (idxOnlyNameMatch > -1) {
            setMethod = methods[idxOnlyNameMatch];
        } else if (idxOnlyNameSimilar > -1) {
            setMethod = methods[idxOnlyNameSimilar];
        } else {
            return null;
        }
        return setMethod;
    }

    /**
     * 查找对象属性的set方法
     * 
     * @param bean
     *            实例对象
     * @param propertyName
     *            属性名称，不区分大小写
     * @return 实例对象方法
     */
    public static Method findSetMethod(Object bean, String propertyName) {
        if (bean == null) {
            return null;
        }
        if (propertyName == null || propertyName.trim().length() < 1) {
            return null;
        }
        int idxMatch = -1;
        int idxSimilar = -1;
        PropertyDescriptor[] props = null;
        PropertyDescriptor property = null;
        String getMethodName = propertyGetMehodName(propertyName);
        try {
            props = Introspector.getBeanInfo(bean.getClass(), Object.class).getPropertyDescriptors();
            if (props != null) {
                for (int i = 0; i < props.length; i++) {
                    property = (PropertyDescriptor) props[i];
                    if (getMethodName.equals(property.getReadMethod().getName())) {
                        idxMatch = i;
                        break;
                    } else if (getMethodName.equalsIgnoreCase(property.getReadMethod().getName())) {
                        idxSimilar = i;
                    }
                }
            }
        } catch (Exception e) {
            log.error("", e);
        }
        Method setMethod = null;
        if (idxMatch > -1) {
            setMethod = ((PropertyDescriptor) props[idxMatch]).getWriteMethod();
        } else if (idxSimilar > -1) {
            setMethod = ((PropertyDescriptor) props[idxSimilar]).getWriteMethod();
        } else {
            return null;
        }
        return setMethod;
    }

    /**
     * 通过对象set方法设置属性值，属性名称大小写不敏感。不支持嵌套属性、Map属性
     * 
     * @param bean
     *            实例对象
     * @param propertyName
     *            属性名称，不区分大小写
     * @param value
     *            属性值
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    public static void setBeanProperty(Object bean, String propertyName, Object value)
            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (bean == null)
            return;
        if (propertyName == null || propertyName.trim().length() < 1)
            return;

        Method setMethod = null;
        setMethod = findSetMethod(bean, propertyName);
        if (setMethod == null) {
            setMethod = findSetMethod(bean, propertyName, value);
        }
        if (setMethod == null)
            return;
        Reflections.makeAccessible(setMethod);

        Class<?>[] classArr = setMethod.getParameterTypes();
        if (value == null || classArr[0] == value.getClass()
                || ClassUtils.isAssignable(value.getClass(), classArr[0])) {
            setMethod.invoke(bean, new Object[] { value });
        } else {
            Object[] oArray = null;
            if (value.getClass().isArray()) {
                oArray = new Object[Array.getLength(value.getClass())];
                for (int i = 0; i < oArray.length; i++) {
                    oArray[i] = ObjectUtils.convert(Array.get(value, i), classArr[0]);
                }
            } else {
                oArray = new Object[1];
                oArray[0] = ObjectUtils.convert(value, classArr[0]);
            }
            setMethod.invoke(bean, oArray);
        }
    }

    /**
     * 猜测属性的set方法名
     * 
     * @param property
     *            属性名
     * @return "set" + 属性名
     */
    private static String propertySetMehodName(String property) {
        if (property == null) {
            return null;
        }
        if (property.trim().length() < 1) {
            return "";
        }
        return new StringBuffer(property.length() + 3).append("set").append(StringUtil.uppercaseFirst(property))
                .toString();
    }

    /**
     * 猜测属性的get方法名
     * 
     * @param property
     *            属性名
     * @return "get" + 属性名
     */
    private static String propertyGetMehodName(String property) {
        if (property == null) {
            return null;
        }
        if (property.trim().length() < 1) {
            return "";
        }
        return new StringBuffer(property.length() + 3).append("get").append(StringUtil.uppercaseFirst(property))
                .toString();
    }

    /**
     * 基于对象属性的get、set方法去除属性值空格
     * 
     * @param bean
     *            实例对象
     */
    public static void trimStringProperty(Object bean) {
        String methodName = null;
        Object value = null;
        Method setMethod = null;
        Method[] methodArr = bean.getClass().getMethods();
        for (Method method : methodArr) {
            methodName = method.getName();
            if (methodName.matches("get[A-Z].*") && method.getParameterTypes().length == 0) {
                try {
                    value = method.invoke(bean, new Object[] {});
                    if (value.getClass() == String.class) {
                        setMethod = bean.getClass().getMethod("set" + methodName.substring(3),
                                new Class[] { method.getReturnType() });
                        setMethod.invoke(bean, new Object[] { StringUtil.trimToEmpty(value) });
                    }
                } catch (Exception e) {
                    continue;
                }
            }
        }
    }

    /**
     * bean2StringArray:对象属性值转换成字符串数组，对象属性必须支持get方法。
     *
     * @param bean
     *            待转换的对象
     * @param properties
     *            需要转换的属性名称和输出顺序，为空时默认对象全部属性
     * @return 按properties顺序排列的属性值数组
     * @throws IntrospectionException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @since JDK 1.7
     */
    public static String[] bean2StringArray(Object bean, String... properties)
            throws IntrospectionException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (bean == null)
            return null;

        // 整理需要输出的属性
        List<String> lstSort = new ArrayList<String>();
        if (properties != null) {
            Set<String> setProps = new HashSet<String>();
            for (String item : properties) {
                if (item != null && !setProps.contains(item)) {
                    setProps.add(item);
                    lstSort.add(item);
                }
            }
        }

        Method getter = null;
        BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

        List<String> lst = new ArrayList<String>();
        if (lstSort.size() < 1) {
            for (PropertyDescriptor property : propertyDescriptors) {
                if (!property.getName().equals("class")) {// 过滤class属性
                    getter = property.getReadMethod();// 得到property对应的getter方法
                    lst.add(StringUtil.trimToEmpty(getter.invoke(bean, new Object[0])));
                }
            }
        } else {
            for (String propertyName : lstSort) {
                lst.add(StringUtil.trimToEmpty(Reflections.invokeGetter(bean, propertyName)));
            }
        }
        return lst.toArray(new String[0]);
    }

    /**
     * objectList2StringArray:值对象集合转换成字符串数组，值对象属性必须支持get方法。
     * 对于空的值对象同样生成空字符串数组，保持记录数一致。
     *
     * @param objects
     *            待转换的对象集合
     * @param properties
     *            需要转换的属性名称和输出顺序，为空时默认对象全部属性
     * @return 按properties顺序排列的属性值数组
     * @since JDK 1.7
     */
    public static <T> String[][] objectList2StringArray(Collection<T> objects, String... properties) {
        if (ObjectUtils.isEmpty(objects))
            return null;

        List<String[]> lstArray = new ArrayList<String[]>(objects.size());
        for (T obj : objects) {
            try {
                lstArray.add(bean2StringArray(obj, properties));
            } catch (Exception e) {
                lstArray.add(new String[0]);
            }
        }
        return lstArray.toArray(new String[0][0]);
    }

    /**
     * 
     * getFieldHaveAnnotation:查找使用了指定注解的屬性
     *
     * @author ren7wei
     * @param target
     *            被查找的目标类型
     * @param annotationClass
     *            指定注解类型
     * @return target中使用了annotationClass注解的field
     * @since JDK 1.8
     */
    public static List<Field> getFieldHaveAnnotation(Class<?> target, Class<? extends Annotation> annotationClass) {
        return Arrays.stream(target.getDeclaredFields()).filter(m -> {
            return m.getAnnotation(annotationClass) != null;
        }).collect(Collectors.toList());
    }

    /**
     * 
     * getFieldNotHaveAnnotation:查找未使用指定注解的屬性
     *
     * @author ren7wei
     * @param target
     *            被查找的目标类型
     * @param annotationClass
     *            指定注解类型
     * @return target中未使用了annotationClass注解的field
     * @since JDK 1.8
     */
    public static List<Field> getFieldNotHaveAnnotation(Class<?> target, Class<? extends Annotation> annotationClass) {
        Class<?> cls = target;
        List<Field> list = new ArrayList<>();
        while (cls != null || cls != Object.class) {
            list.addAll(Arrays.stream(target.getDeclaredFields()).filter(m -> {
                return m.getAnnotation(annotationClass) == null;
            }).collect(Collectors.toList()));
            cls = cls.getSuperclass();
        }
        return list;
    }

    /**
     * 
     * getField:查找满足指定要求的属性
     *
     * @author ren7wei
     * @param target
     *            被查找的目标类型
     * @param predicates
     *            查找表达式
     * @return 满足要求的属性
     * @since JDK 1.8
     */
    public static List<Field> getFields(Class<?> target, Predicate<Field> predicates) {
        Class<?> cls = target;
        List<Field> list = new ArrayList<>();
        while (cls != null && cls != Object.class) {
            list.addAll(Arrays.stream(cls.getDeclaredFields()).filter(predicates).collect(Collectors.toList()));
            cls = cls.getSuperclass();
        }
        return list;
    }

    /**
     * 
     * getField:查找满足指定要求的属性
     *
     * @author ren7wei
     * @param target
     *            被查找的目标类型
     * @param predicates
     *            查找表达式
     * @return 满足要求的属性
     * @since JDK 1.8
     */
    public static Field getField(Class<?> target, Predicate<Field> predicates) {
        Class<?> cls = target;
        List<Field> list = new ArrayList<>();
        while (cls != null && cls != Object.class) {
            list.addAll(Arrays.stream(cls.getDeclaredFields()).filter(predicates).collect(Collectors.toList()));
            cls = cls.getSuperclass();
        }
        return list.size() > 0 ? list.get(0) : null;
    }

    /**
     * 
     * getValueByField:获取目标对象中指定属性的值
     *
     * @author ren7wei
     * @param obj
     *            目标对象
     * @param field
     *            指定的属性对象
     * @return obj.field的值
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @since JDK 1.8
     */
    public static Object getValueByField(Object obj, Field field) {
        if (!field.isAccessible())
            field.setAccessible(true);
        try {
            return field.get(obj);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            throw new RuntimeException("Error reading the " + field.getName() + " property of the  " + obj.getClass());
        }
    }

    /**
     * 
     * getValueByField:获取目标对象中指定属性的值
     *
     * @author ren7wei
     * @param obj
     *            目标对象
     * @param field
     *            指定的属性对象
     * @return obj.field的值
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchFieldException
     * @since JDK 1.8
     */
    public static Object getValueByField(Object obj, String fieldname) {
        try {
            Field field = obj.getClass().getDeclaredField(fieldname);
            if (!field.isAccessible())
                field.setAccessible(true);
            return field.get(obj);
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
            throw new RuntimeException("Error reading the " + fieldname + " property of the  " + obj.getClass());
        }

    }

    /**
     * 
     * getValueByField:获取目标对象中指定属性的值
     *
     * @author ren7wei
     * @param obj
     *            目标对象
     * @param field
     *            指定的属性对象
     * @return obj.field的值
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchFieldException
     * @since JDK 1.8
     */
    public static void setValueByField(Object obj, String fieldname, Object value) {
        try {
            Field field = obj.getClass().getDeclaredField(fieldname);
            if (!field.isAccessible())
                field.setAccessible(true);
            field.set(obj, value);
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
            throw new RuntimeException(
                    "Error to write the " + fieldname + " property of " + value + " to " + obj.getClass());
        }
    }

    public static void setValueByField(Object obj, Field field, Object value) {
        try {
            if (!field.isAccessible())
                field.setAccessible(true);
            field.set(obj, value);
        } catch (IllegalArgumentException | IllegalAccessException | SecurityException e) {
            throw new RuntimeException(
                    "Error to write the " + field.getName() + " property of " + value + " to " + obj.getClass());
        }
    }

    public static Class<?> getFieldClass(Object obj, String fieldname) {
        try {
            Field field = obj.getClass().getDeclaredField(fieldname);
            return getFieldClass(obj, field);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("Error reading the " + fieldname + " property of the  " + obj.getClass());
        }
    }

    public static Class<?> getFieldClass(Object obj, Field field) {
        try {
            if (!field.isAccessible())
                field.setAccessible(true);
            return field.getType();
        } catch (IllegalArgumentException | SecurityException e) {
            throw new RuntimeException("Error reading the " + field.getName() + " property of the  " + obj.getClass());
        }
    }

    public static <K, V> Map<K, V> map(K k, V v) {
        Map<K, V> map = new HashMap<>();
        map.put(k, v);
        return map;
    }

}
